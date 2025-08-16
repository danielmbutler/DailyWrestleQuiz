package com.dbtechprojects.dailywrestlequiz.data.viewmodels

import com.dbtechprojects.dailywrestlequiz.data.di.ArgPersistence
import com.dbtechprojects.dailywrestlequiz.data.di.StubArgPersistence
import com.dbtechprojects.dailywrestlequiz.data.model.Question
import com.dbtechprojects.dailywrestlequiz.data.model.Quiz
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuestionUseCaseStub
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuestionsUseCase
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuizUseCase
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuizUseCaseStub
import com.dbtechprojects.dailywrestlequiz.data.usecase.TimerUtils
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuestionViewModel.Companion.ARG_QUESTION_COUNT
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuestionViewModel.Companion.ARG_QUIZ_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


interface QuestionViewModel {
    val state: StateFlow<Question?>
    val questionsAmount: StateFlow<Int>
    val currentQuestionNumber: StateFlow<Int>
    val timeRemainingText: StateFlow<String>
    val progress: StateFlow<Float>
    val selectedAnswer: StateFlow<Int?>
    val currentScore: StateFlow<Int>

    val quizName: StateFlow<String>

    val isGameOver: StateFlow<Boolean>

    val isLoading: StateFlow<Boolean>

    val customEndMessage: StateFlow<String?>

    val streak: StateFlow<Int>

    fun setAnswer(answer: Int)

    fun requestNextQuestion()

    companion object {
        const val ARG_QUIZ_ID = "quizId"
        const val ARG_QUESTION_COUNT = "questionCount"
    }
}

class QuestionViewModelImpl(
    private val questionsUseCase: QuestionsUseCase,
    private val quizUseCase: QuizUseCase,
    private val args: ArgPersistence<Int?>,
    private val timerUtils: TimerUtils,
) : BaseViewModel(), QuestionViewModel {

    private var questionsList = emptyList<Question>()

    private val _state = MutableStateFlow<Question?>(null)
    override val state: StateFlow<Question?> = _state.asStateFlow()

    private val _questionsAmount = MutableStateFlow(0)
    override val questionsAmount: StateFlow<Int> = _questionsAmount

    private val _currentQuestionNumber = MutableStateFlow(1)
    override val currentQuestionNumber: StateFlow<Int> = _currentQuestionNumber

    private val _timeRemainingText = MutableStateFlow("")
    override val timeRemainingText: StateFlow<String> = _timeRemainingText.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    override val progress: StateFlow<Float> = _progress.asStateFlow()

    private val _selectedAnswer = MutableStateFlow<Int?>(null)
    override val selectedAnswer: StateFlow<Int?>
        get() = _selectedAnswer

    private val _currentScore = MutableStateFlow(0)
    override val currentScore: StateFlow<Int>
        get() = _currentScore

    private val _isGameOver = MutableStateFlow(false)
    override val isGameOver: StateFlow<Boolean>
        get() = _isGameOver

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean>
        get() = _isLoading


    private val _quizName = MutableStateFlow("")
    override val quizName: StateFlow<String>
        get() = _quizName

    private val _customEndMessage = MutableStateFlow<String?>(null)
    override val customEndMessage: StateFlow<String?>
        get() = _customEndMessage

    private val _elapsedTime = MutableStateFlow(0)

    private var answered = false

    private var quiz: Quiz? = null

    private val _streak = MutableStateFlow(0)
    override val streak: StateFlow<Int> get() = _streak


    private fun getQuestionsAmount(quiz: Quiz): Int {
        args.get(ARG_QUESTION_COUNT).let {
            return if (it == 0) {
                quiz.questions
            } else {
                it ?: 1
            }
        }
    }

    private fun getQuizName(quiz: Quiz): String {
        args.get(ARG_QUESTION_COUNT).let {
            return if (it == 0) {
                quiz.name
            } else {
                "Daily Wrestling Trivia"
            }
        }
    }

    private suspend fun setCustomMessage() {
        _customEndMessage.value =
            if (args.get(ARG_QUESTION_COUNT) == 0) {
                null
            } else if (_streak.value == 0) {
                var text = "Unlucky!"
                text += "\n You lost your streak !"
                text
            } else {
                var text = "Congratulations!"
                text += "\n Your Streak is now ${questionsUseCase.getStreak()} Days !"
                text
            }

    }


    init {
        _isLoading.value = true
        viewModelScope.launch {
            args.get(ARG_QUIZ_ID)?.let {
                quizUseCase.getQuiz(it)?.let { quiz ->
                    requestQuestions(quiz)
                    _state.value = questionsList.firstOrNull()
                    _questionsAmount.value = getQuestionsAmount(quiz)
                    _quizName.value = getQuizName(quiz)
                    this@QuestionViewModelImpl.quiz = quiz
                    _isLoading.value = false
                    startTimer(quiz.timeLimit)
                }
            }
        }
    }

    override fun setAnswer(answer: Int) {
        if (!answered) {
            answered = true
            if (answer == state.value?.answer) {
                // correct answer
                updateScore()
                _state.value?.question_id?.let {
                    updateTimesAnswered(it)
                }
            }
            _selectedAnswer.value = answer

            if (_currentQuestionNumber.value == _questionsAmount.value) {
                viewModelScope.launch {
                    saveScore()
                    _streak.value = questionsUseCase.getStreak()
                    setCustomMessage()
                    _isGameOver.value = true
                }
            }
        }
    }

    private suspend fun saveScore() {
        quiz?.let {
            var quizId = it.id
            if (args.get(ARG_QUESTION_COUNT) == 1) {
                quizId = Quiz.DAILY_TRIVIA
            }
            questionsUseCase.saveScore(quizId, _currentScore.value)
        }
    }

    /**
     * score for getting it right in the first five seconds +150
     * score for getting it right between 5 to 10 seconds + 100
     * score for getting it right between 11 and 15 seconds + 50
     */
    private fun updateScore() {
        _elapsedTime.value.let {
            val remaining = quiz?.timeLimit?.minus(it) ?: return
            if (remaining >= 15) {
                _currentScore.value += 150
            } else if (remaining > 10) {
                _currentScore.value += 100
            } else if (remaining > 5) {
                _currentScore.value += 50
            }
        }
    }

    private fun updateTimesAnswered(questionId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            questionsUseCase.updateTimesAnswered(questionId)
        }
    }

    override fun requestNextQuestion() {
        _state.value = questionsList[currentQuestionNumber.value - 1]
        resetState()
    }

    private fun resetState() {
        answered = false
        _selectedAnswer.value = null
        _currentQuestionNumber.value += 1
        _progress.value = 0f
        _elapsedTime.value = 0
        viewModelScope.launch {
            quiz?.timeLimit?.let { startTimer(it) }
        }
    }

    private suspend fun requestQuestions(quiz: Quiz) {
        questionsList = quiz.let {
            questionsUseCase.getQuestions(
                it,
                args.get(
                    ARG_QUESTION_COUNT
                )
            )
        }
    }

    private suspend fun startTimer(
        totalTime: Int,
    ) {
        for (elapsed in 0..totalTime) {
            _elapsedTime.value = elapsed
            if (_selectedAnswer.value == null) {
                _progress.value = timerUtils.calculateLinearProgress(elapsed, totalTime)
                _timeRemainingText.value =
                    timerUtils.getTimeRemainingText(elapsed, quiz?.timeLimit ?: 0)
                delay(1000)
                continue
            }
            return // question has been answered in allotted time
        }
        // time is over
        setAnswer(-1)
    }

    companion object {
        fun stub(): QuestionViewModel {
            return QuestionViewModelImpl(
                questionsUseCase = QuestionUseCaseStub(),
                args = StubArgPersistence<Int>(null),
                timerUtils = TimerUtils(),
                quizUseCase = QuizUseCaseStub(),
            )
        }
    }
}
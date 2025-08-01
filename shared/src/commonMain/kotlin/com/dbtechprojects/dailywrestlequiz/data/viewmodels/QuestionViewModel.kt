package com.dbtechprojects.dailywrestlequiz.data.viewmodels

import com.dbtechprojects.dailywrestlequiz.data.di.ArgPersistence
import com.dbtechprojects.dailywrestlequiz.data.di.StubArgPersistence
import com.dbtechprojects.dailywrestlequiz.data.model.Question
import com.dbtechprojects.dailywrestlequiz.data.model.Quiz
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuestionUseCaseStub
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuestionsUseCase
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuestionsUseCaseImpl
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuizUseCase
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuizUseCaseStub
import com.dbtechprojects.dailywrestlequiz.data.usecase.TimerUtils
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
    val currentScore:  StateFlow<Int>

    fun setAnswer(answer: Int)

    fun requestNextQuestion()

    companion object {
        const val ARG_QUIZ_ID = "quizId"
    }
}

class QuestionViewModelImpl(
    private val questionsUseCase: QuestionsUseCase,
    private val quizUseCase: QuizUseCase,
    private val quizId: ArgPersistence<Int>,
    private val timerUtils: TimerUtils
) : BaseViewModel(), QuestionViewModel {


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

    private val _elapsedTime = MutableStateFlow(0)

    private var answered = false

    private var quiz: Quiz? = null

    init {
        requestQuestion()
        viewModelScope.launch {
            quizId.get(ARG_QUIZ_ID)?.let {
                quizUseCase.getQuiz(it)?.let { quiz ->
                    _questionsAmount.value = quiz.questions
                    this@QuestionViewModelImpl.quiz = quiz
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
        requestQuestion()
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

    private fun requestQuestion() {
        viewModelScope.launch {
            _state.value = questionsUseCase.getRandomQuestion()
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
                quizId = StubArgPersistence<Int>(Quiz.getQuiz().first().id),
                timerUtils = TimerUtils(),
                quizUseCase = QuizUseCaseStub()
            )
        }
    }
}
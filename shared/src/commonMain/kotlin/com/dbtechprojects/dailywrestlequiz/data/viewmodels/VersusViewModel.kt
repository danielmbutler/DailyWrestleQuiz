package com.dbtechprojects.dailywrestlequiz.data.viewmodels

import com.dbtechprojects.dailywrestlequiz.data.di.ArgPersistence
import com.dbtechprojects.dailywrestlequiz.data.model.Question
import com.dbtechprojects.dailywrestlequiz.data.model.Quiz
import com.dbtechprojects.dailywrestlequiz.data.model.VersusMode
import com.dbtechprojects.dailywrestlequiz.data.model.VersusMode.Companion.guessCorrect
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuestionsUseCase
import com.dbtechprojects.dailywrestlequiz.data.usecase.TimerUtils
import com.dbtechprojects.dailywrestlequiz.data.usecase.VersusModeUseCase
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.VersusViewModel.Companion.ARG_QUIZ_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface VersusViewModel {
    val versusModes: StateFlow<List<VersusMode>>
    val question: StateFlow<Question?>
    val questionsAmount: StateFlow<Int>
    val currentQuestionNumber: StateFlow<Int>
    val timeRemainingText: StateFlow<String>
    val progress: StateFlow<Float>
    val selectedAnswer: StateFlow<Int?>
    val currentScore: StateFlow<Int>

    val opponentScore: StateFlow<Int>

    val isGameOver: StateFlow<Boolean>

    val isLoading: StateFlow<Boolean>

    val customEndMessage: StateFlow<String?>

    val quizName: StateFlow<String>


    fun setAnswer(answer: Int)

    fun requestNextQuestion()

    companion object {
        const val ARG_QUIZ_NAME = "QuizName"
    }
}

class VersusViewModelImpl(
    private val timerUtils: TimerUtils,
    private val versusModeUseCase: VersusModeUseCase,
    private val questionsUseCase: QuestionsUseCase,
    private val args: ArgPersistence<String?>,
) : BaseViewModel(), VersusViewModel {

    private var questionsList = emptyList<Question>()

    private val _question = MutableStateFlow<Question?>(null)
    override val question: StateFlow<Question?> = _question.asStateFlow()

    private val _versusModes = MutableStateFlow<List<VersusMode>>(emptyList())
    override val versusModes: StateFlow<List<VersusMode>> = _versusModes.asStateFlow()

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

    private val _opponentScore = MutableStateFlow(0)
    override val opponentScore: StateFlow<Int>
        get() = _opponentScore

    private val _isGameOver = MutableStateFlow(false)
    override val isGameOver: StateFlow<Boolean>
        get() = _isGameOver

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean>
        get() = _isLoading


    private val _customEndMessage = MutableStateFlow<String?>(null)
    override val customEndMessage: StateFlow<String?>
        get() = _customEndMessage

    private val _elapsedTime = MutableStateFlow(0)

    private val _quizName = MutableStateFlow("")
    override val quizName: StateFlow<String>
        get() = _quizName

    private var answered = false

    private var versusMode: VersusMode? = null


    private fun setCustomMessage(name: String, win: Boolean, draw: Boolean) {
        _customEndMessage.value =
            if (!win) {
                var text = "Unlucky!"
                text += "\n You lost against $name!"
                text
            } else if (draw) {
                var text = "Unlucky!"
                text += "\n You drew against $name!"
                text
            } else {
                var text = "Congratulations!"
                text += "\n You beat $name"
                text
            }

    }


    init {
        _isLoading.value = true
        viewModelScope.launch {
            args.get(ARG_QUIZ_NAME)?.let {
                versusModeUseCase.getVersusMode(it)?.let { quiz ->
                    versusMode = quiz
                    requestQuestions()
                    _question.value = questionsList.firstOrNull()
                    _questionsAmount.value = 10
                    _quizName.value = it
                    _isLoading.value = false
                    _opponentScore.value = 0
                    _currentScore.value = 0
                    startTimer(Quiz.versusQuiz.timeLimit)
                }
            }
            _versusModes.value = versusModeUseCase.getVersusMode()
        }
    }

    override fun setAnswer(answer: Int) {
        if (!answered) {
            answered = true
            if (answer == question.value?.answer) {
                // correct answer
                updateUserScore()
                question.value?.question_id?.let {
                    updateTimesAnswered(it)
                }
            }
            updateOppenentScore()
            _selectedAnswer.value = answer

            if (_currentQuestionNumber.value == _questionsAmount.value) {
                setCustomMessage(
                    name = versusMode?.name ?: "",
                    win = _currentScore.value > _opponentScore.value,
                    draw = _currentScore.value == _opponentScore.value
                )
                _isGameOver.value = true
            }
        }
    }

    private fun updateOppenentScore() {
        _elapsedTime.value.let {
            val remaining = Quiz.versusQuiz.timeLimit.minus(it)
            val opponentCorrect = versusMode?.guessCorrect()

            if (opponentCorrect == true) {
                if (remaining >= 15) {
                    _opponentScore.value += 150
                } else if (remaining > 10) {
                    _opponentScore.value += 100
                } else if (remaining > 5) {
                    _opponentScore.value += 50
                }
            }
        }
    }

    /**
     * score for getting it right in the first five seconds +150
     * score for getting it right between 5 to 10 seconds + 100
     * score for getting it right between 11 and 15 seconds + 50
     */
    private fun updateUserScore() {
        _elapsedTime.value.let {
            val remaining = Quiz.versusQuiz.timeLimit.minus(it)
            val opponentCorrect = versusMode?.guessCorrect()

            if (remaining >= 15) {
                _currentScore.value += 150
                if (opponentCorrect == true) {
                    _opponentScore.value += 150
                }
            } else if (remaining > 10) {
                _currentScore.value += 100
                if (opponentCorrect == true) {
                    _opponentScore.value += 100
                }
            } else if (remaining > 5) {
                _currentScore.value += 50
                if (opponentCorrect == true) {
                    _opponentScore.value += 50
                }
            }
        }
    }

    private fun updateTimesAnswered(questionId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            questionsUseCase.updateTimesAnswered(questionId)
        }
    }

    override fun requestNextQuestion() {
        _question.value = questionsList[currentQuestionNumber.value - 1]
        resetState()
    }

    private fun resetState() {
        answered = false
        _selectedAnswer.value = null
        _currentQuestionNumber.value += 1
        _progress.value = 0f
        _elapsedTime.value = 0
        viewModelScope.launch {
            startTimer(Quiz.versusQuiz.timeLimit)
        }
    }

    private suspend fun requestQuestions() {
        questionsList = versusModeUseCase.getQuestions()
    }

    private suspend fun startTimer(
        totalTime: Int,
    ) {
        for (elapsed in 0..totalTime) {
            _elapsedTime.value = elapsed
            if (_selectedAnswer.value == null) {
                _progress.value = timerUtils.calculateLinearProgress(elapsed, totalTime)
                _timeRemainingText.value =
                    timerUtils.getTimeRemainingText(elapsed, Quiz.versusQuiz.timeLimit)
                delay(1000)
                continue
            }
            return // question has been answered in allotted time
        }
        // time is over
        setAnswer(-1)
    }

}
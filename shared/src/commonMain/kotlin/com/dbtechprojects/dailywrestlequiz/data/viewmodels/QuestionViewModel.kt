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
    val selectedAnswer: MutableStateFlow<Int?>
    val currentScore: MutableStateFlow<Int>

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
    override val selectedAnswer: MutableStateFlow<Int?>
        get() =  _selectedAnswer

    private val _currentScore = MutableStateFlow(0)
    override val currentScore: MutableStateFlow<Int>
        get() = _currentScore

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
            _selectedAnswer.value = answer
            if (answer == state.value?.answer) {
                _currentScore.value += 1
            }
        }
    }

    override fun requestNextQuestion() {
        requestQuestion()
        resetState()
    }

    private fun resetState(){
        answered = false
        selectedAnswer.value = null
        _currentQuestionNumber.value += 1
        _progress.value = 0f
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
            if (_selectedAnswer.value == null){
                _progress.value = elapsed.toFloat() / totalTime.coerceAtLeast(1)
                _timeRemainingText.value = timerUtils.getTimeRemainingText(elapsed, quiz?.timeLimit ?: 0)
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
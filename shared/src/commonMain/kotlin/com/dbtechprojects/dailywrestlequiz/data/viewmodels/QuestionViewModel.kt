package com.dbtechprojects.dailywrestlequiz.data.viewmodels

import com.dbtechprojects.dailywrestlequiz.data.model.Question
import com.dbtechprojects.dailywrestlequiz.data.model.Quiz
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuestionUseCaseStub
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuestionsUseCase
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuestionsUseCaseImpl
import com.dbtechprojects.dailywrestlequiz.data.usecase.TimerUtils
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

    fun setAnswer(answer: Int)
}

class QuestionViewModelImpl(
    private val questionsUseCase: QuestionsUseCase,
    private val quiz: Quiz,
    private val timerUtils: TimerUtils
) : BaseViewModel(), QuestionViewModel {


    private val _state = MutableStateFlow<Question?>(null)
    override val state: StateFlow<Question?> = _state.asStateFlow()

    private val _questionsAmount = MutableStateFlow(quiz.questions)
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

    private var answered = false

    init {
        requestQuestion()
        viewModelScope.launch {
            startTimer(quiz.timeLimit)
        }
    }

    override fun setAnswer(answer: Int) {
        if (!answered) {
            answered = true
            _selectedAnswer.value = answer
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
                _timeRemainingText.value = timerUtils.getTimeRemainingText(elapsed, quiz.timeLimit)
                delay(1000)
                continue
            }
          break
        }
    }

    companion object {
        fun stub(): QuestionViewModel {
            return QuestionViewModelImpl(
                questionsUseCase = QuestionUseCaseStub(),
                quiz = Quiz.getQuiz().first(),
                timerUtils = TimerUtils()
            )
        }
    }
}
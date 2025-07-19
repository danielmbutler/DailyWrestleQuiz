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

class QuestionViewModel(
    private val questionsUseCase: QuestionsUseCase,
    private val quiz: Quiz,
    private val timerUtils: TimerUtils
): BaseViewModel() {


    private val _state = MutableStateFlow<List<Question>?>(null)
    val state: StateFlow<List<Question>?> = _state.asStateFlow()

    private val _timeRemainingText = MutableStateFlow("")
    val timeRemainingText: StateFlow<String> = _timeRemainingText.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    init {
        requestQuestion()
        viewModelScope.launch {
            startTimer(quiz.timeLimit)
        }
    }

    private fun requestQuestion(){
        viewModelScope.launch {
            _state.value = listOf(questionsUseCase.getRandomQuestion())
        }
    }

    suspend fun startTimer(
        totalTime: Int,
    ) {
        for (elapsed in 0..totalTime) {
            _progress.value = elapsed.toFloat() / totalTime.coerceAtLeast(1)
            _timeRemainingText.value = timerUtils.getTimeRemainingText(elapsed, quiz.timeLimit)
            delay(1000)
        }
    }

    companion object{
        fun stub() : QuestionViewModel {
            return QuestionViewModel(
                questionsUseCase = QuestionUseCaseStub(),
                quiz = Quiz.getQuiz().first(),
                timerUtils = TimerUtils()
            )
        }
    }
}
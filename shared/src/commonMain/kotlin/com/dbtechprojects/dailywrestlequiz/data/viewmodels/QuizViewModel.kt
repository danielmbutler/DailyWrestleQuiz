package com.dbtechprojects.dailywrestlequiz.data.viewmodels

import com.dbtechprojects.dailywrestlequiz.data.model.Quiz
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuizUseCase
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuizUseCaseStub
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


interface QuizViewModel {
    val quizzes: StateFlow<List<Quiz>>
    val isLoading: StateFlow<Boolean>
}

class QuizViewModelImpl(
    quizUseCase: QuizUseCase
) : BaseViewModel(), QuizViewModel {

    private val _quizzes = MutableStateFlow<List<Quiz>>(emptyList())

    override val quizzes: StateFlow<List<Quiz>>
        get() = _quizzes

    private val _isLoading = MutableStateFlow<Boolean>(false)
    override val isLoading: StateFlow<Boolean>
        get() = _isLoading


    init {
        _isLoading.value = true
        viewModelScope.launch {
            delay(1000)
            _quizzes.value = quizUseCase.getQuizzes()
            _isLoading.value = false
        }
    }
}

class QuizViewModelStub : QuizViewModel {
    private val _isLoading = MutableStateFlow<Boolean>(false)
    private val _quizzes = MutableStateFlow<List<Quiz>>(emptyList())

    override val quizzes: StateFlow<List<Quiz>>
        get() = _quizzes

    override val isLoading: StateFlow<Boolean>
        get() = _isLoading


    init {
        _isLoading.value = true
        _quizzes.value = QuizUseCaseStub().getQuizzes()
        _isLoading.value = false
    }
}

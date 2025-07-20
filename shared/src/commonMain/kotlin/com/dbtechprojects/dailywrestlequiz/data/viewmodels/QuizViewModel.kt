package com.dbtechprojects.dailywrestlequiz.data.viewmodels

import com.dbtechprojects.dailywrestlequiz.data.model.Quiz
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuizUseCase
import com.dbtechprojects.dailywrestlequiz.data.usecase.QuizUseCaseStub
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


interface QuizViewModel {
    val quizzes : StateFlow<List<Quiz>>
}

class QuizViewModeImpl(
    quizUseCase: QuizUseCase
) : QuizViewModel {

    private val _quizzes = MutableStateFlow<List<Quiz>>(emptyList())

    override val quizzes: StateFlow<List<Quiz>>
        get() = _quizzes


    init {
        _quizzes.value = quizUseCase.getQuizzes()
    }



    companion object {
        fun stub() = QuizViewModeImpl(QuizUseCaseStub())
    }

}
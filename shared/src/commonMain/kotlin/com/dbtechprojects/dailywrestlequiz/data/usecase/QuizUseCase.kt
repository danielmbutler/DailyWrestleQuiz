package com.dbtechprojects.dailywrestlequiz.data.usecase

import com.dbtechprojects.dailywrestlequiz.data.model.Quiz

interface QuizUseCase{
    fun getQuizzes() : List<Quiz>

    fun getQuiz(id: Int) : Quiz?
}

class QuizUseCaseImpl : QuizUseCase {

    override fun getQuizzes(): List<Quiz> {
        return Quiz.getQuiz()
    }

    override fun getQuiz(id: Int): Quiz? {
        return Quiz.getQuiz().find { it.id == id }
    }
}

class QuizUseCaseStub: QuizUseCase by QuizUseCaseImpl()
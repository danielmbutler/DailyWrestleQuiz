package com.dbtechprojects.dailywrestlequiz.data.usecase

import com.dbtechprojects.dailywrestlequiz.data.model.Quiz

interface QuizUseCase{
    fun getQuizzes() : List<Quiz>
}

class QuizUseCaseImpl : QuizUseCase {

    override fun getQuizzes(): List<Quiz> {
        return Quiz.getQuiz()
    }
}
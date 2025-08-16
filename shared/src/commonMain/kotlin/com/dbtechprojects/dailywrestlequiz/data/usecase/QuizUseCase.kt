package com.dbtechprojects.dailywrestlequiz.data.usecase

import com.dbtechprojects.dailywrestlequiz.data.data.persistence.database.daos.ScoreDao
import com.dbtechprojects.dailywrestlequiz.data.model.Quiz

interface QuizUseCase{
   suspend fun getQuizzes() : List<Quiz>

    fun getQuiz(id: Int) : Quiz?
}

class QuizUseCaseImpl(private val scoreDao: ScoreDao) : QuizUseCase {

    override suspend fun getQuizzes(): List<Quiz> {
        return Quiz.getQuiz().map {
            it.copy(
                highestScore = scoreDao.getScore(it.id)?.score ?: 0,
            )
        }
    }

    override fun getQuiz(id: Int): Quiz? {
        return Quiz.getQuiz().find { it.id == id }
    }
}

class QuizUseCaseStub: QuizUseCase {
    override suspend fun getQuizzes(): List<Quiz> {
        return Quiz.getQuiz()
    }

    override fun getQuiz(id: Int): Quiz? {
       return null
    }
}
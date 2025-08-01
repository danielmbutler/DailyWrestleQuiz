package com.dbtechprojects.dailywrestlequiz.data.usecase

import com.dbtechprojects.dailywrestlequiz.data.data.persistence.database.daos.QuestionDao
import com.dbtechprojects.dailywrestlequiz.data.model.Question
import com.dbtechprojects.dailywrestlequiz.data.model.Quiz

interface QuestionsUseCase {
    fun getQuestions(quiz: Quiz) : List<Question>
    fun getRandomQuestion() : Question

    suspend fun updateTimesAnswered(questionId: Int)
}

class QuestionsUseCaseImpl(private val questionDao: QuestionDao): QuestionsUseCase {
    override fun getQuestions(quiz: Quiz): List<Question> {
        return Question.getQuestions()
    }

    override fun getRandomQuestion(): Question {
        return Question.getQuestions().random()
    }

    override suspend fun updateTimesAnswered(questionId: Int) {
        questionDao.updateTimesAnswered(questionId)
    }
}

class QuestionUseCaseStub : QuestionsUseCase{
    override fun getQuestions(quiz: Quiz): List<Question> {
        return Question.getQuestions()
    }

    override fun getRandomQuestion(): Question {
        return Question.getQuestions().first()
    }

    override suspend fun updateTimesAnswered(questionId: Int) {
        // naaa
    }
}
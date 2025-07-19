package com.dbtechprojects.dailywrestlequiz.data.usecase

import com.dbtechprojects.dailywrestlequiz.data.model.Question
import com.dbtechprojects.dailywrestlequiz.data.model.Quiz

interface QuestionsUseCase {
    fun getQuestions(quiz: Quiz) : List<Question>
    fun getRandomQuestion() : Question
}

class QuestionsUseCaseImpl: QuestionsUseCase {
    override fun getQuestions(quiz: Quiz): List<Question> {
        return Question.getQuestions()
    }

    override fun getRandomQuestion(): Question {
        return Question.getQuestions().random()
    }
}

class QuestionUseCaseStub : QuestionsUseCase{
    override fun getQuestions(quiz: Quiz): List<Question> {
        return Question.getQuestions()
    }

    override fun getRandomQuestion(): Question {
        return Question.getQuestions().first()
    }
}
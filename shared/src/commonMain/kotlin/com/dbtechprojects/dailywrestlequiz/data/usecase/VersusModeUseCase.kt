package com.dbtechprojects.dailywrestlequiz.data.usecase

import com.dbtechprojects.dailywrestlequiz.data.model.Question
import com.dbtechprojects.dailywrestlequiz.data.model.Quiz
import com.dbtechprojects.dailywrestlequiz.data.model.VersusMode

interface VersusModeUseCase{
    fun getVersusMode(): List<VersusMode>

    fun getVersusMode(name: String): VersusMode?

    suspend fun getQuestions(): List<Question>
}

class VersusModeUseCaseImpl(private val questionsUseCase: QuestionsUseCase) : VersusModeUseCase {
    override fun getVersusMode(): List<VersusMode> {
        return VersusMode.getVersusModes().sortedBy { it.winPercentage }
    }

    override fun getVersusMode(name: String): VersusMode? {
        return VersusMode.getVersusModes().firstOrNull { it.name == name }
    }

    override suspend fun getQuestions(): List<Question> {
        return questionsUseCase.getQuestions(
            quiz = Quiz.versusQuiz,
            questionCount = 10
        )
    }
}
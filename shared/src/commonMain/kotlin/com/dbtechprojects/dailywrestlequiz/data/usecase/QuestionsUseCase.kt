package com.dbtechprojects.dailywrestlequiz.data.usecase

import androidx.room.RoomRawQuery
import com.dbtechprojects.dailywrestlequiz.data.data.persistence.database.daos.QuestionDao
import com.dbtechprojects.dailywrestlequiz.data.model.Question
import com.dbtechprojects.dailywrestlequiz.data.model.Quiz

interface QuestionsUseCase {
    suspend fun getQuestions(quiz: Quiz, questionCount: Int?): List<Question>
    suspend fun getRandomQuestion(): Question

    suspend fun updateTimesAnswered(questionId: Int)

    suspend fun saveScore(quizId: Int, score: Int)
}

class QuestionsUseCaseImpl(private val questionDao: QuestionDao) : QuestionsUseCase {

    override suspend fun getQuestions(quiz: Quiz, questionCount: Int?): List<Question> {
        val query = StringBuilder("SELECT * FROM Question WHERE 1=1 ")

        val limit = if (questionCount == 0) quiz.questions else questionCount

        quiz.apply {
            if (decade == "MODERN") {
                query.append("AND (decade = '2010s' OR decade = '2020s') ")
            } else if (decade.isNotEmpty()) {
                query.append("AND decade = '$decade' ")
            }

            if (company.isNotEmpty()) {
                query.append("AND company = '$company' ")
            }

            if (ppv.isNotEmpty()) {
                query.append("AND ppv = '$ppv' ")
            }

            if (special != 0) {
                query.append("AND special = $special ")
            }
        }

        query.append("ORDER BY timesAnswered ASC, RANDOM() LIMIT $limit")

        return try {
            questionDao.getQuestionsRaw(RoomRawQuery(query.toString()))
        }catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }

    }


    override suspend fun getRandomQuestion(): Question {
        return questionDao.getRandomQuestion() ?: questionDao.getRandomQuestionFromLeastAnswered()
    }

    override suspend fun updateTimesAnswered(questionId: Int) {
        questionDao.updateTimesAnswered(questionId)
    }

    override suspend fun saveScore(quizId: Int, score: Int) {
        // if score higher than previous score save
        if (quizId == Quiz.DAILY_TRIVIA){
            // update streak
            return
        }
        // handle normal quiz scores
    }
}

class QuestionUseCaseStub : QuestionsUseCase {
    override suspend fun getQuestions(quiz: Quiz, questionCount: Int?): List<Question> {
        return Question.getQuestions()
    }

    override suspend fun getRandomQuestion(): Question {
        return Question.getQuestions().first()
    }

    override suspend fun updateTimesAnswered(questionId: Int) {
        // naaa
    }

    override suspend fun saveScore(quizId: Int, score: Int) {

    }
}
package com.dbtechprojects.dailywrestlequiz.data.usecase

import androidx.room.RoomRawQuery
import com.dbtechprojects.dailywrestlequiz.data.data.persistence.database.daos.QuestionDao
import com.dbtechprojects.dailywrestlequiz.data.data.persistence.database.daos.ScoreDao
import com.dbtechprojects.dailywrestlequiz.data.data.persistence.database.daos.SettingsDao
import com.dbtechprojects.dailywrestlequiz.data.model.Question
import com.dbtechprojects.dailywrestlequiz.data.model.Quiz
import com.dbtechprojects.dailywrestlequiz.data.model.Score
import kotlinx.coroutines.flow.firstOrNull

interface QuestionsUseCase {
    suspend fun getQuestions(quiz: Quiz, questionCount: Int?): List<Question>
    suspend fun getRandomQuestion(): Question

    suspend fun updateTimesAnswered(questionId: Int)

    suspend fun saveScore(quizId: Int, score: Int)

    suspend fun getStreak(): Int
}

class QuestionsUseCaseImpl(
    private val questionDao: QuestionDao,
    private val scoreDao: ScoreDao,
    private val settingsDao: SettingsDao
) : QuestionsUseCase {

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
        } catch (e: Exception) {
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
        if (quizId == Quiz.DAILY_TRIVIA) {
            // update streak
            updateDailyTriviaStreak(score)
            return
        }
        // handle normal quiz scores
        // get previous score
        // if higher update
        // else do nothing
        scoreDao.getScore(quizId)?.let {
            if (score > it.score) {
                scoreDao.updateScore(it.copy(score = score))
                return
            }
        }

        // no score
        scoreDao.insertScore(
            Score(
                quizId = quizId.toString(),
                score = score, username = null
            )
        )

    }

    override suspend fun getStreak(): Int {
        return settingsDao.getSettingsFlow().firstOrNull()?.streak ?: 0
    }

    private suspend fun updateDailyTriviaStreak(score: Int) {
        if (score > 1) {
            settingsDao.getSettingsFlow().firstOrNull {
                if (it?.streak == 0) {
                    settingsDao.setStreakStartDate()
                }
                settingsDao.increaseStreak()
                true
            }
        } else {
            settingsDao.endStreak()
            true
        }
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

    override suspend fun getStreak(): Int {
        return 0
    }
}
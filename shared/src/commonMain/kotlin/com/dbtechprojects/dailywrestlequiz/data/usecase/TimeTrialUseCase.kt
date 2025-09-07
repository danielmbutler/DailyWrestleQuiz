package com.dbtechprojects.dailywrestlequiz.data.usecase

import com.dbtechprojects.dailywrestlequiz.data.data.persistence.database.daos.ScoreDao
import com.dbtechprojects.dailywrestlequiz.data.model.Score
import com.dbtechprojects.dailywrestlequiz.data.model.TimeTrial

interface TimeTrialUseCase {
    suspend fun getTimeTrials(): List<TimeTrial>

    fun getTimeTrial(id: Int): TimeTrial?
    fun getDetails(timeTrialId: Int): Map<String, List<String>>

    suspend fun saveTime(score: Int, timeTrialId: Int)
}

class TimeTrialUseCaseImpl(
    private val timerUtils: TimerUtils,
    private val scoreDao: ScoreDao
) : TimeTrialUseCase {

    private val timeTrials = listOf(
        TimeTrial(
            id = 1,
            name = "WWE Champions",
            highScore = "",
            color = "#e3b85b"
        ),
        TimeTrial(
            id = 2,
            name = "WCW Champions",
            highScore = "",
            color = "#1E3A8A"
        ),
        TimeTrial(
            id = 3,
            name = "ECW Champions",
            highScore = "",
            color = "#4B0082"
        ),
        TimeTrial(
            id = 4,
            name = "Royal Rumble Winners",
            highScore = "",
            color = "#B45309"
        ),
//        TimeTrial(
//            id = 5,
//            name = "Test",
//            highScore = "",
//            color = "#B45309"
//        )
    )

    override suspend fun getTimeTrials(): List<TimeTrial> {
        return timeTrials.map {
            val score = scoreDao.getTimeTrialScore(it.id)?.score ?: 0
            if(score > 0) {
                it.copy(highScore = timerUtils.getMinutesAndSecondsFromSeconds(score))
            } else it
        }
    }

    override fun getTimeTrial(id: Int): TimeTrial? {
        return timeTrials.find { it.id == id }
    }

    override fun getDetails(timeTrialId: Int): Map<String, List<String>> {
        return when (timeTrialId) {
            1 -> TimeTrial.wweChampions
            2 -> TimeTrial.wcwChampions
            3 -> TimeTrial.ecwChampions
            4 -> TimeTrial.royalRumbleWinners
            else -> emptyMap()
        }
    }

    override suspend fun saveTime(score: Int, timeTrialId:Int) {
        // get previous score
        // if lower update
        // else do nothing
        scoreDao.getTimeTrialScore(timeTrialId)?.let {
            if (score < it.score) {
                scoreDao.updateScore(it.copy(score = score))
                return
            }
        }

        // no score
        scoreDao.insertScore(
            Score(
                quizId = timeTrialId.toString(),
                score = score, username = null
            )
        )
    }


}

class TimeTrialUseCaseStub : TimeTrialUseCase {
    override suspend fun getTimeTrials(): List<TimeTrial> {
        return listOf()
    }

    override fun getTimeTrial(id: Int): TimeTrial? {
        return null
    }

    override fun getDetails(timeTrialId: Int): Map<String, List<String>> {
        return emptyMap()
    }

    override suspend fun saveTime(score: Int, timeTrialId: Int) {

    }

}
package com.dbtechprojects.dailywrestlequiz.data.usecase

import com.dbtechprojects.dailywrestlequiz.data.model.Question
import com.dbtechprojects.dailywrestlequiz.data.model.Quiz
import com.dbtechprojects.dailywrestlequiz.data.model.TimeTrial

interface TimeTrialUseCase {
    fun getTimeTrials(): List<TimeTrial>
    fun getDetails(id: Int): Map<String, List<String>>

    fun setScore(score: Int)
}

class TimeTrialUseCaseImpl : TimeTrialUseCase {

    override fun getTimeTrials(): List<TimeTrial> {
        return listOf(
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
        )
    }

    override fun getDetails(id: Int): Map<String, List<String>> {
        return when(id){
            1 -> TimeTrial.wweChampions
            2 -> TimeTrial.wcwChampions
            3 -> TimeTrial.ecwChampions
            4 -> TimeTrial.royalRumbleWinners
            else -> emptyMap()
        }
    }

    override fun setScore(score: Int) {

    }


}

class TimeTrialUseCaseStub : TimeTrialUseCase by TimeTrialUseCaseImpl()
package com.dbtechprojects.dailywrestlequiz.data.usecase

import com.dbtechprojects.dailywrestlequiz.data.model.TimeTrial

interface TimeTrialUseCase {
    fun getTimeTrials(): List<TimeTrial>

    fun getTimeTrial(id: Int): TimeTrial?
    fun getDetails(timeTrialId: Int): Map<String, List<String>>

    fun saveTime(score: Int)
}

class TimeTrialUseCaseImpl : TimeTrialUseCase {

    private val timeTrials =  listOf(
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

    override fun getTimeTrials(): List<TimeTrial> {
        return timeTrials
    }

    override fun getTimeTrial(id: Int): TimeTrial? {
        return  timeTrials.find { it.id == id }
    }

    override fun getDetails(timeTrialId: Int): Map<String, List<String>> {
        return when(timeTrialId){
            1 -> TimeTrial.wweChampions
            2 -> TimeTrial.wcwChampions
            3 -> TimeTrial.ecwChampions
            4 -> TimeTrial.royalRumbleWinners
            else -> emptyMap()
        }
    }

    override fun saveTime(score: Int) {

    }


}

class TimeTrialUseCaseStub : TimeTrialUseCase by TimeTrialUseCaseImpl()
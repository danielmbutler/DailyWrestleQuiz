package com.dbtechprojects.dailywrestlequiz.data.usecase

class TimerUtils {

    fun getTimeRemainingText(timeElapsed: Int, totalTime: Int): String {
        val timeRemaining = totalTime - timeElapsed
        if (timeRemaining < 10){
            return "0:0$timeRemaining"
        }
        return "0:$timeRemaining"
    }

}
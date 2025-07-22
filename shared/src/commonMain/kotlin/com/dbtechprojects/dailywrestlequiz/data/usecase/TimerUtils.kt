package com.dbtechprojects.dailywrestlequiz.data.usecase

class TimerUtils {

    fun getTimeRemainingText(timeElapsed: Int, totalTime: Int): String {
        val timeRemaining = totalTime - timeElapsed
        val minutes = timeRemaining / 60
        val seconds = timeRemaining % 60
        val secondsText = if (seconds < 10) "0$seconds" else "$seconds"
        return "$minutes:$secondsText"
    }

}
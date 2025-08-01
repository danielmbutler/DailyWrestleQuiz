package com.dbtechprojects.dailywrestlequiz.data.usecase

class TimerUtils {

    fun getTimeRemainingText(timeElapsed: Int, totalTime: Int): String {
        val timeRemaining = totalTime - timeElapsed
        val minutes = timeRemaining / 60
        val seconds = timeRemaining % 60
        val secondsText = if (seconds < 10) "0$seconds" else "$seconds"
        return "$minutes:$secondsText"
    }

    fun formatTimeFromSeconds(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60

        return when {
            minutes == 0 -> "$remainingSeconds ${if (remainingSeconds == 1) "second" else "seconds"}"
            remainingSeconds == 0 -> "$minutes ${if (minutes == 1) "minute" else "minutes"}"
            else -> {
                val minuteStr = if (minutes == 1) "minute" else "minutes"
                val secondStr = if (remainingSeconds == 1) "second" else "seconds"
                "$minutes $minuteStr and $remainingSeconds $secondStr"
            }
        }
    }

    fun calculateLinearProgress(timeElapsed: Int, totalTime: Int): Float {
        return timeElapsed.toFloat() / totalTime.coerceAtLeast(1)
    }

}
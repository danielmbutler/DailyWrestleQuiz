package com.dbtechprojects.dailywrestlequiz.data.model

import kotlin.random.Random

data class VersusMode(
    val name: String, val winPercentage: Float, val color: String, val completed: Boolean = false
) {

    companion object {
        const val FACT_FIEND = "Fact Fiend"
        const val QUIZ_MASTER = "Quiz Master"
        const val TRIVIA_TITAN = "Trivia Titan"

        fun getVersusModes(): List<VersusMode> = listOf(
            VersusMode(name = FACT_FIEND, winPercentage = 85f, color = "#8E24AA"),
            VersusMode(name = QUIZ_MASTER, winPercentage = 60f, color = "#FFD54F"),
            VersusMode(
                name = TRIVIA_TITAN, winPercentage = 40f, color = "#1976D2"
            )
        )
        fun VersusMode.guessCorrect(): Boolean {
            val roll = Random.nextInt(100)
            return roll < winPercentage
        }
    }
}


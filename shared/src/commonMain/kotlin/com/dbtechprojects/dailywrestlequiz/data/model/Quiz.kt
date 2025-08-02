package com.dbtechprojects.dailywrestlequiz.data.model

data class Quiz(
    var id: Int,
    val name: String,
    val questions: Int,
    val timeLimit: Int,
    val color: String,
    val company: String,
    val decade: String,
    val ppv: String,
    val special: Int,
    val order: Int,
    var wrestlers: String = "",
    var highestScore: Int = -1,
    var disabled: Boolean = false
) {

    fun adaptNameForPie() : String {
        return name.replace(" ", "\n")
    }
    companion object {

        const val DAILY_TRIVIA = 9999
//        const val TIME_TRIALS = 2
//        const val VERSUS = 3
//        const val WHEEL_OF_TRIVIA = 4

        fun getQuiz(): List<Quiz> {
            return listOf(
                Quiz(1, "ATTITUDE ERA", 20, 20, "#B22222", "WWE", "90s", "", 1, 1),       // Deep red (fire brick)
                Quiz(2, "90s", 20, 20, "#4B0082", "", "90s", "", 0, 2),                  // Deep purple (indigo)
                Quiz(3, "2000s", 20, 20, "#1E3A8A", "", "2000s", "", 0, 3),              // Deep blue (navy)
                Quiz(4, "AEW", 20, 20, "#B45309", "AEW", "", "", 0, 4),                  // Deep orange (burnt orange)
                Quiz(5, "ALL ERAS", 20, 20, "#166534", "", "", "", 0, 5)                 // Deep green (forest green)
            )
        }
    }
}
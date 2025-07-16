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
    companion object {
        fun getQuiz(): List<Quiz> {
            return listOf(
                Quiz(1, "ATTITUDE ERA", 20, 20, "#D72F2F", "WWE", "90s", "", 1, 1),
                Quiz(2, "90s", 20, 20, "#c0c0c0", "", "90s", "", 0, 2),
                Quiz(3, "2000s", 20, 20, "#006BC9", "", "2000s", "", 0, 3),
                Quiz(4, "AEW", 20, 20, "#B19534", "AEW", "", "", 0, 4),
                Quiz(5, "ALL ERAs", 20, 20, "#CE1BEB", "", "", "", 0, 5)
            )
        }
    }
}
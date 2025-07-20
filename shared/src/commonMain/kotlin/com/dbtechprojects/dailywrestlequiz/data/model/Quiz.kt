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
                Quiz(1, "ATTITUDE ERA", 20, 20, "#E99696", "WWE", "90s", "", 1, 1),       // softer red (light coral)
                Quiz(2, "90s", 20, 20, "#D3D3D3", "", "90s", "", 0, 2),                  // softer silver (light gray)
                Quiz(3, "2000s", 20, 20, "#7FAEDB", "", "2000s", "", 0, 3),              // softer blue (light steel blue)
                Quiz(4, "AEW", 20, 20, "#D4C27E", "AEW", "", "", 0, 4),                  // softer gold (pale goldenrod)
                Quiz(5, "ALL ERAS", 20, 20, "#CFA3DD", "", "", "", 0, 5)                 // softer purple (plum)
            )
        }
    }
}
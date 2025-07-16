package com.dbtechprojects.dailywrestlequiz.data.model


data class Question(
    val id: Int ,
    val question: String,
    val answers: String,
    val company: String,
    val decade: String,
    val ppv: String,
    val special: Int,
    val wrestlers: String?,
    val answer: Int,
    val type: Int,
    val timesAnswered: Int = 0
){
    companion object {
        fun getQuestions(): List<Question> {
            return listOf(
                Question(0, "What year was AEW formed?", "2019, 2020, 2018", "AEW", "2020s", "", 0, "", 0, 1, 0),
                Question(1, "What year was WCW formed?", "2019, 2020, 2018", "AEW", "2010s", "", 0, "", 0, 1, 0),
                Question(2, "What year was AEW formed?", "2019, 2020, 2018, 2017, 2016, 2015", "AEW", "90s", "Royal Rumble", 0, "", 0, 1, 0),
                Question(3, "What year was World Wrestling Trivia formed?", "2019, 2020, 2018", "AEW", "90s", "Royal Rumble", 24, "", 0, 1, 0)
            )
        }

        fun getAnswers(answers: String): List<String> {
            val split = answers.split(",").map { it.trim() }
            return split.mapIndexed { index, answer -> "${getAnswerLetter(index)}. $answer" }
        }

        fun getAnswerLetter(index: Int): String {
            return when (index) {
                0 -> "A"
                1 -> "B"
                2 -> "C"
                3 -> "D"
                4 -> "E"
                5 -> "F"
                else -> ""
            }
        }

        fun Question.isValidQuestion(): Boolean {
            val answers = getAnswers(this.answers)
            if (answers.isEmpty() || answers.size > 5 || this.question.isEmpty() || this.type == 2) return false
            return answers.none { it.isEmpty() }
        }
    }
}


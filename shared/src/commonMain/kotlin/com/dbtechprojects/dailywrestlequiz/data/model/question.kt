package com.dbtechprojects.dailywrestlequiz.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@Entity
@Serializable
data class Question(
    @PrimaryKey(autoGenerate = false)
    val question_id: Int,
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

        fun fromJson(json: String): List<Question> {
            return try {
                Json.decodeFromString(json)
            } catch (e: Exception) {
                e.printStackTrace()
                return emptyList()
            }
        }
        fun getQuestions(): List<Question> {
            return listOf(
                Question(3, "Which of the below superstars had the most eliminations in the 1999 Royal Rumble match ?",
                    "Kane, Steve Austin, The Rock, The Undertaker",
                    "AEW", "90s", "Royal Rumble", 24, "", 0, 1, 0)
            )
        }

        fun getAnswers(answers: String): List<String> {
            if (answers.isEmpty()) return emptyList()
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


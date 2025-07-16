package com.dbtechprojects.dailywrestlequiz.data.model

data class Score (
    val id: String,
    val quizId: String,
    val score: Int,
    val username: String?,
    val sent: Boolean = false,
)

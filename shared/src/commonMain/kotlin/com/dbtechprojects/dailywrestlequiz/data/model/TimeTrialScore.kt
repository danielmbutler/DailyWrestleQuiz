package com.dbtechprojects.dailywrestlequiz.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TimeTrialScore (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val quizId: String,
    val score: Int,
    val username: String?,
    val sent: Boolean = false,
)
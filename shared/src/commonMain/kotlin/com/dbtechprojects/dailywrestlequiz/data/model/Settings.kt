package com.dbtechprojects.dailywrestlequiz.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Settings(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val streak: Int,
    val streakStartDate: String,
    val currentStreakLastAnsweredDate: String
)

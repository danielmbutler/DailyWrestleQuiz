package com.dbtechprojects.dailywrestlequiz.android.ui

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

object NavRoutes {

     @Serializable data object Home: NavKey
     @Serializable data object Quiz: NavKey
     @Serializable data class Question(val quizId: Int, val amount: Int): NavKey
     @Serializable data object WheelOfTrivia: NavKey
     @Serializable data object TimeTrial: NavKey
     @Serializable data object Versus: NavKey

     @Serializable data class VersusGame(val quizName: String): NavKey
     @Serializable data class TimeTrialGame(val timeTrialId: Int): NavKey

}
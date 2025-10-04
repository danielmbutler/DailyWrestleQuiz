package com.dbtechprojects.dailywrestlequiz.android.ui

import android.content.Context
import android.content.Intent
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.dbtechprojects.dailywrestlequiz.android.R
import com.dbtechprojects.dailywrestlequiz.data.model.VersusMode

object UiUtils {
    fun hexToColor(hex: String): Color {
        val colorString = if (hex.startsWith("#")) hex else "#$hex"
        return Color(colorString.toColorInt())
    }

    fun sendShareEvent(text: String, context: Context){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
         context.startActivity(shareIntent)
    }

    fun getVersusImageRes(versusName: String): Int {
        return when (versusName) {
            VersusMode.FACT_FIEND -> {
                R.drawable.fact_fiend
            }

            VersusMode.QUIZ_MASTER -> {
                R.drawable.quiz_master
            }

            VersusMode.TRIVIA_TITAN -> {
                R.drawable.trivia_titan
            }

            else -> {
                R.drawable.quiz_master
            }
        }
    }
}
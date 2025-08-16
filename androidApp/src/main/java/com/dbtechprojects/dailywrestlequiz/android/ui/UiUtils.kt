package com.dbtechprojects.dailywrestlequiz.android.ui

import android.content.Context
import android.content.Intent
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

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
}
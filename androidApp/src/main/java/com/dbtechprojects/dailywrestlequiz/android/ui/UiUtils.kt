package com.dbtechprojects.dailywrestlequiz.android.ui

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

object UiUtils {
    fun hexToColor(hex: String): Color {
        val colorString = if (hex.startsWith("#")) hex else "#$hex"
        return Color(colorString.toColorInt())
    }
}
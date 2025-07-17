package com.dbtechprojects.dailywrestlequiz.android

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.dbtechprojects.dailywrestlequiz.android.ui.theme.AppTypography
import com.dbtechprojects.dailywrestlequiz.android.ui.theme.DarkColorScheme
import com.dbtechprojects.dailywrestlequiz.android.ui.theme.LightColorScheme

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}

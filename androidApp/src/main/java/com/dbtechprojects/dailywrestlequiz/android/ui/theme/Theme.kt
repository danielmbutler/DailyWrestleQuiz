package com.dbtechprojects.dailywrestlequiz.android.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

 val DarkColorScheme = darkColorScheme(
    primary = WrestleGold,
    onPrimary = GraphiteBlack,
    background = GraphiteBlack,
    onBackground = LightText,
    surface = DarkSurface,
    onSurface = LightText
)

 val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = DarkText,
    background = LightBackground,
    onBackground = DarkText,
    surface = LightSurface,
    onSurface = DarkText
)


val AppTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        color = LightText
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = LightText
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        color = MutedGray
    )
)

package com.dbtechprojects.dailywrestlequiz.android.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val DarkColorScheme = darkColorScheme(
    primary = WrestleGold,             // same gold for primary
    onPrimary = GraphiteBlack,         // text on primary: black
    background = DarkSurface,        // #1C1C1E
    onBackground = LightText,          // #F2F2F7 (light text)
    surface = GraphiteBlack,             // #2C2C2E
    onSurface = LightText,             // text on surface: light
    secondary = LightSecondaryText,    // secondary text lighter
    onSecondary = DarkSurface,
    outline = DividerGray,
    inverseSurface = DarkSurface,
    tertiary = LightSecondaryText
)

val LightColorScheme = lightColorScheme(
    primary = LightPrimary,            // #FFCC4D (WrestleGold)
    onPrimary = GraphiteBlack,         // Text on primary: dark for contrast
    background = LightBackground,      // #F9F9FB (light bg)
    onBackground = DarkText,           // #1C1C1E (dark text)
    surface = LightSurface,            // #FFFFFF (white surface)
    onSurface = DarkText,              // Text on surface: dark
    secondary = MutedGray,             // For secondary text/buttons
    onSecondary = LightSurface,        // Text on secondary color
    outline = LightDivider,
    inverseSurface =  QuestionBoxWhite,
    tertiary = ChipGroupBackgroundLight

    // add other colors as needed
)


val AppTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 42.sp,
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.SansSerif,
        fontSize = 24.sp,
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.SansSerif,
        fontSize = 18.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 24.sp,
        color = LightText
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
    )
)

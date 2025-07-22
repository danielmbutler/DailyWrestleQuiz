package com.dbtechprojects.dailywrestlequiz.android.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

val GraphiteBlack = Color(0xFF1a1a1a)
val DarkSurface = Color(0xFF2C2C2E)
val WrestleGold = Color(0xFFe3b85b)
val LightText = Color(0xFFF2F2F7)
val MutedGray = Color(0xFF8E8E93)
val DividerGray = Color(0xFF3A3A3C)

val LightBackground = Color(0xFFEEEEEE)
val LightSurface = Color(0xFFFFFFFF)
val LightPrimary = Color(0xFFFFCC4D)
val DarkText = Color(0xFF1C1C1E)
val LightSecondaryText = Color(0xFF6C6C70)
val LightDivider = Color(0xFFE5E5EA)

// Specific Game Colors
val CorrectGreen = Color(0xFF22C55E)
val IncorrectRed = Color(0xFFC97878)

val QuestionBoxWhite= Color(0xFFF8F9FA)

@Composable
fun Color.asState(): State<Color> = remember(this) { mutableStateOf(this) }

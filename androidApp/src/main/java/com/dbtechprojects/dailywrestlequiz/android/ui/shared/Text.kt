package com.dbtechprojects.dailywrestlequiz.android.ui.shared

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isUnspecified
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


@Composable
fun PrimaryBodyLarge(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Unspecified,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Text(
        text = text, modifier = modifier,
        style = MaterialTheme.typography.bodyLarge,
        color = color,
        textAlign = textAlign,
    )
}

@Composable
fun PrimaryBodyMedium(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Unspecified,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Text(
        text = text, modifier = modifier,
        style = MaterialTheme.typography.labelLarge,
        color = color,
        textAlign = textAlign,
    )
}

@Composable
fun PrimaryBodyLargeAnimateSize(
    text: String,
    shouldAnimate: Boolean,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Unspecified,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    val initialFont = MaterialTheme.typography.bodyLarge.fontSize.value
    var targetFont by remember { mutableFloatStateOf(initialFont) }

    val animatedFontSize by animateFloatAsState(
        targetValue = targetFont,
        animationSpec = tween(durationMillis = 150),
        label = "fontSizeAnimation"
    )

    // Trigger the animation whenever shouldAnimate becomes true
    LaunchedEffect(shouldAnimate) {
        if (shouldAnimate) {
            targetFont = initialFont * 1.25f
            delay(150) // grow duration
            targetFont = initialFont
        }
    }
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge,
        fontSize = animatedFontSize.sp,
        color = color,
        textAlign = textAlign
    )
}

@Composable
fun PrimaryBodySmall(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
fun AutoResizedTextHeight(
    text: String,
    style: androidx.compose.ui.text.TextStyle,
    color: Color,
    modifier: Modifier = Modifier,
) {
    var resizedTextStyle by remember {
        mutableStateOf(style)
    }
    var shouldDraw by remember {
        mutableStateOf(false)
    }

    val defaultFontSize = MaterialTheme.typography.bodyLarge.fontSize

    Text(
        text = text,
        color = color,
        style = resizedTextStyle,
        textAlign = TextAlign.Center,
        modifier = modifier.drawWithContent {
            if (shouldDraw) {
                drawContent()
            }
        },
        softWrap = true,
        onTextLayout = { result: TextLayoutResult ->
            if (result.didOverflowHeight) {
                if (style.fontSize.isUnspecified) {
                    resizedTextStyle = resizedTextStyle.copy(
                        fontSize = defaultFontSize
                    )
                }
                resizedTextStyle = resizedTextStyle.copy(
                    fontSize = resizedTextStyle.fontSize * 0.95
                )
            } else {
                shouldDraw = true
            }
        }
    )
}

@Composable
fun AutoResizedTextWidth(
    text: String,
    style: TextStyle,
    color: Color,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center
) {
    var resizedTextStyle by remember(text) {
        mutableStateOf(style)
    }
    var shouldDraw by remember(text) {
        mutableStateOf(false)
    }

    val defaultFontSize = MaterialTheme.typography.bodyLarge.fontSize

    Text(
        text = text,
        color = color,
        style = resizedTextStyle,
        textAlign = textAlign,
        modifier = modifier.drawWithContent {
            if (shouldDraw) {
                drawContent()
            }
        },
        softWrap = false,
        onTextLayout = { result: TextLayoutResult ->
            if (result.didOverflowWidth) {
                val newSize = if (resizedTextStyle.fontSize.isUnspecified) {
                    defaultFontSize
                } else {
                    resizedTextStyle.fontSize * 0.95
                }
                resizedTextStyle = resizedTextStyle.copy(fontSize = newSize)
            } else {
                shouldDraw = true
            }
        }
    )
}

@Composable
fun ScreenCenterTitle(
    text: String,
    subtitle: String? = null,
    bottomPadding: Int = 24
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                color = MaterialTheme.colorScheme.onBackground,
                text = text,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(top = 24.dp, bottom =
                    if (subtitle == null) bottomPadding.dp else (bottomPadding / 2).dp),
                textAlign = TextAlign.Center
            )
            subtitle?.let {
                AutoResizedTextWidth(
                    color = MaterialTheme.colorScheme.onBackground,
                    text = subtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = bottomPadding.dp),
                    textAlign = TextAlign.Center
                )
            }

        }

    }
}
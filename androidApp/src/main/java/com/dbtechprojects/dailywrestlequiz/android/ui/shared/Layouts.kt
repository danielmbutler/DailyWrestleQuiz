package com.dbtechprojects.dailywrestlequiz.android.ui.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun PrimaryBorderedBox(
    maxWidth: Float = 0.75f,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(maxWidth)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(
                color = MaterialTheme.colorScheme.surface,
            )
            .padding(12.dp)
    ) {
        content.invoke()
    }
}

@Composable
fun ImageRow(
    color: Color,
    label: String,
    drawable: Int,
    onClick: () -> Unit
) {
    ReusableRow(
        color,
        onClick
    ) {
        Image(
            painter = painterResource(id = drawable),
            contentDescription = label,
            colorFilter = ColorFilter.tint(
                MaterialTheme.colorScheme.onSurface
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun ReusableRow(
    color: Color,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(color)
            .clickable
            {
                onClick.invoke()
            }
            .padding(12.dp),

        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        content.invoke()
    }
}

@Composable
fun SurfaceSection(
    contentSpacedBy: Int = 0,
    isScrollable: Boolean = false,
    content: @Composable () -> Unit,
) {
    val coloumnModifer = Modifier
        .fillMaxSize()
        .clip(RoundedCornerShape(12.dp))
        .background(MaterialTheme.colorScheme.surface)
        .padding(12.dp)

    if (isScrollable) {
        coloumnModifer
            .verticalScroll(rememberScrollState())
            .imePadding()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp, start = 12.dp, end = 12.dp, bottom = 12.dp)
    )
    {
        Column(
            coloumnModifer,
            verticalArrangement = Arrangement.spacedBy(contentSpacedBy.dp),
        ) {
            content.invoke()
        }
    }

}
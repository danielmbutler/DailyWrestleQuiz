package com.dbtechprojects.dailywrestlequiz.android.ui.shared

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dbtechprojects.dailywrestlequiz.android.ui.theme.CorrectGreen
import com.dbtechprojects.dailywrestlequiz.android.ui.theme.QuestionBoxWhite

@Composable
fun PrimaryButton(
    modifier: Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit, text: String) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor =
               if(enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            ,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Text(
            style = MaterialTheme.typography.bodyLarge,
            text = text
        )
    }
}

@Composable
fun SecondaryButton(
    modifier: Modifier,
    onClick: () -> Unit, text: String) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor =
              MaterialTheme.colorScheme.secondary
            ,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Text(
            style = MaterialTheme.typography.bodyLarge,
            text = text
        )
    }
}

@Composable
fun GreenButton(
    modifier: Modifier,
    onClick: () -> Unit, text: String) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor =
                CorrectGreen
            ,
            contentColor = QuestionBoxWhite
        )
    ) {
        Text(
            style = MaterialTheme.typography.bodyLarge,
            text = text
        )
    }
}
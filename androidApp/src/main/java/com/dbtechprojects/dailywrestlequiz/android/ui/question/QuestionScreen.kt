package com.dbtechprojects.dailywrestlequiz.android.ui.question

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dbtechprojects.dailywrestlequiz.android.R
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.AutoResizedText
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.PrimaryBodyLarge
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.SurfaceSection
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuestionViewModel


@Composable
fun QuestionScreen(
    viewModel: QuestionViewModel,
    navHome: () -> Unit,
) {
    val questions by viewModel.state.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val remainingText by viewModel.timeRemainingText.collectAsState()

    val currentIndex = remember { mutableIntStateOf(0) }

    // Update when questions or currentIndex change
    val currentQuestion = remember(questions, currentIndex.intValue) {
        questions?.getOrNull(currentIndex.intValue)
    }


    SurfaceSection {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

        }
        QuestionScreenHeaderRow(
            currentIndex,
            questionsSize = questions?.size ?: 0
        )
        QuestionTimer(
            progress,
            remainingText
        )

        QuestionBox(
            question = currentQuestion?.question ?: ""
        )
    }

}

@Composable
fun QuestionScreenHeaderRow(
    currentIndex: MutableIntState,
    questionsSize: Int
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        PrimaryBodyLarge("${currentIndex.intValue + 1}/$questionsSize ")
        PrimaryBodyLarge(stringResource(R.string.app_name))
    }
}

@Composable
fun QuestionTimer(
    progress: Float,
    remainingText: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(6.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.background
        )
        Text(
            text = remainingText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground
        )

    }

}

@Composable
fun QuestionBox(
    question: String,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.25f)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        AutoResizedText(
            text = question,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}



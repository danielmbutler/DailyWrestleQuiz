package com.dbtechprojects.dailywrestlequiz.android.ui.timetrial

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dbtechprojects.dailywrestlequiz.android.R
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.PrimaryBodyLarge
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.PrimaryButton
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.QuestionTimer
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.ScreenCenterTitle
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.SurfaceSection
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.TimeTrialGameViewModel
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.TimeTrialGameViewModelImpl


@Composable
fun TimeTrialGameScreen(viewModel: TimeTrialGameViewModel) {

    val isGameOver by viewModel.gameOver.collectAsState()
    val win by viewModel.gameOver.collectAsState()
    val foundItems by viewModel.foundItems.collectAsState()
    val timeRemainingText by viewModel.timeRemainingText.collectAsState()
    val scoreText by viewModel.scoreText.collectAsState()
    val quizName by viewModel.quizName.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val textFieldState = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is TimeTrialGameViewModelImpl.UiEvent.InCorrectAttempt,
                is TimeTrialGameViewModelImpl.UiEvent.CorrectAttempt -> {
                    textFieldState.value = ""
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SurfaceSection(
            contentSpacedBy = 12
        ) {
            if (scoreText.isEmpty()) {
                ScreenCenterTitle(
                    stringResource(R.string.time_trials_title),
                    stringResource(R.string.name_all) + " " + quizName
                )
            } else {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(
                        tween(durationMillis = 500)
                    )
                ) {
                    ScreenCenterTitle(
                        stringResource(R.string.time_trials_title),
                        bottomPadding = 12
                    )
                    CorrectWrestlersRow(foundItems)
                }
            }
            if (isGameOver) {
                EndGameBox(win)
            } else {
                GameScreen(progress, timeRemainingText, scoreText, viewModel, textFieldState)
            }
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                PrimaryButton(
                    onClick = {},
                    text = stringResource(R.string.show_entries),
                    modifier = Modifier.padding(32.dp)
                )
            }
        }
    }

}

@Composable
fun GameScreen(
    progress: Float,
    timeRemainingText: String,
    scoreText: String,
    viewmodel: TimeTrialGameViewModel,
    textFieldState: MutableState<String>
) {
    QuestionTimer(progress, timeRemainingText)
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        PrimaryBodyLarge(
            scoreText + " " + stringResource(R.string.found),
            textAlign = TextAlign.Center
        )
    }
    TimeTrialTextField(viewmodel, textFieldState)
}

@Composable
fun EndGameBox(win: Boolean) {
    PrimaryBodyLarge(
        if (win) stringResource(R.string.congratulations)
        else stringResource(R.string.better_luck_next_time),
        textAlign = TextAlign.Center
    )
    PrimaryButton(
        onClick = {},
        text = stringResource(R.string.share_result),
        modifier = Modifier.padding(32.dp)
    )
}

@Composable
fun TimeTrialTextField(viewModel: TimeTrialGameViewModel, nameState: MutableState<String>) {

    TextField(
        value = nameState.value,
        onValueChange = {
            nameState.value = it
            viewModel.sendAttempt(nameState.value)
        },
        label = { Text(stringResource(R.string.wrestler_name)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        singleLine = true
    )

}

@Composable
fun CorrectWrestlersRow(foundItems: List<String>) {
    val listState = rememberLazyListState()

    var previousSize by remember { mutableStateOf(0) }

    LaunchedEffect(foundItems.size) {
        if (foundItems.size > previousSize) {
            listState.animateScrollToItem(0)
        }
        previousSize = foundItems.size
    }

    LazyRow(state = listState) {
        items(foundItems.size) { index ->
            if (foundItems[index] == "******") return@items
            NameChip(foundItems[index])
            if (index != foundItems.size - 1) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }

}

@Composable
fun NameChip(name: String) {
    Surface(
        modifier = Modifier.padding(6.dp),
        shadowElevation = 8.dp,
        tonalElevation = 8.dp,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.tertiary
    ) {
        Row {
            Text(
                text = name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
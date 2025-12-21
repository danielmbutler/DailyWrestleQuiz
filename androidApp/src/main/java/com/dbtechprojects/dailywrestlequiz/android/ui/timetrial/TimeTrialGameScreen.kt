package com.dbtechprojects.dailywrestlequiz.android.ui.timetrial

import androidx.appcompat.app.AlertDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.dbtechprojects.dailywrestlequiz.android.R
import com.dbtechprojects.dailywrestlequiz.android.ui.UiUtils
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.ImageRow
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.PrimaryBodyLarge
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.PrimaryBodySmall
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.PrimaryButton
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.QuestionTimer
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.QuestionTimerTypeTwo
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.ScreenCenterTitle
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.SecondaryButton
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.SurfaceSection
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.TimeTrialGameViewModel
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.TimeTrialGameViewModelImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.compose
import kotlinx.coroutines.launch


@Composable
fun TimeTrialGameScreen(viewModel: TimeTrialGameViewModel) {

    val isGameOver by viewModel.gameOver.collectAsState()
    val win by viewModel.win.collectAsState()
    val foundItems by viewModel.foundItems.collectAsState()
    val timeRemainingText by viewModel.timeRemainingText.collectAsState()
    val scoreText by viewModel.scoreText.collectAsState()
    val quizName by viewModel.quizName.collectAsState()
    val textFieldState = remember { mutableStateOf("") }
    val winText by viewModel.winTime.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    var previousFoundItems by remember { mutableStateOf(foundItems) }

    LaunchedEffect(foundItems) {
        // Find the item that changed from "******" to something else
        val newlyRevealedIndex = foundItems.indices.find { index ->
            previousFoundItems.getOrNull(index) == "******" && foundItems[index] != "******"
        }

        if (newlyRevealedIndex != null) {
            listState.animateScrollToItem(newlyRevealedIndex)
        }

        previousFoundItems = foundItems
    }

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp, start = 12.dp, end = 12.dp, bottom = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp)
            .imePadding()
    ) {

        TimerRow(timeRemainingText, scoreText)
        Spacer(modifier = Modifier.height(8.dp))
        // Scrollable content

        if (showDialog && !isGameOver) {
            WrestlersDialog(foundItems, scoreText) {
                showDialog = false
            }
        }
        Column(
            modifier = Modifier
                .weight(1f) // Takes remaining space
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            if (isGameOver) {

                ScreenCenterTitle(
                    stringResource(R.string.time_trials_title),
                    stringResource(R.string.name_all) + " " + quizName,
                    bottomPadding = 8
                )

                EndGameBox(
                    { viewModel.reset() },
                    winText = winText,
                    quizName = quizName,
                    win = win,
                    scoreText = scoreText
                )
            } else {

                // Expandable content
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    item {


                        ScreenCenterTitle(
                            stringResource(R.string.time_trials_title),
                            stringResource(R.string.name_all) + " " + quizName,
                            bottomPadding = 8
                        )


                    }
                    items(foundItems.size) { index ->
                        Text(
                            text = foundItems[index],
                            color = if (foundItems[index] == "******")
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            else
                                MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // Text field outside scrollable area, at bottom
        if (!isGameOver) {
            TimeTrialTextField(
                viewModel = viewModel,
                nameState = textFieldState,
                onClick = { showDialog = true},
            )
        }
    }
    }


@Composable
fun WrestlersDialog(
    foundItems: List<String>,
    scoreText: String,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ScreenCenterTitle(
                    stringResource(R.string.entries),
                    bottomPadding = 12
                )
                PrimaryBodyLarge("$scoreText found")
                LazyColumn(
                    Modifier
                        .fillMaxHeight(0.85f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .padding(12.dp)
                        .background(
                            MaterialTheme.colorScheme.background
                        )
                ) {
                    items(foundItems.size) { index ->
                        Row(
                            modifier = Modifier.padding(8.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center

                        ) {
                            PrimaryBodyLarge(
                                foundItems[index],
                                textAlign = TextAlign.Center
                            )
                        }

                    }
                }
                PrimaryButton(
                    text = "Close",
                    onClick = { onDismiss.invoke() },
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
@Composable
fun TimerRow(
    timeRemainingText: String,
    scoreText: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$scoreText Found",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = timeRemainingText,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Composable
fun EndGameBox(
    onTryAgain: () -> Unit,
    winText: String = "",
    win: Boolean,
    quizName: String,
    scoreText: String = ""
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val shareOpened = remember { mutableStateOf(false) }
    var text = if (win) stringResource(R.string.congratulations)
    else stringResource(R.string.better_luck_next_time)

    text += "\n You named $scoreText wrestlers"

    if (win) {
        text += "\n Your time was ${winText}"
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp), horizontalArrangement = Arrangement.Center
    ) {
        PrimaryBodyLarge(
            text,
            textAlign = TextAlign.Center
        )
    }
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (win) {
                PrimaryButton(
                    onClick = {
                        if (!shareOpened.value) {
                            shareOpened.value = true
                            UiUtils.sendShareEvent(
                                "I completed the $quizName challenge and named $scoreText wrestlers in $winText. On the Daily Wrestling Trivia App !",
                                context,
                            )
                            // Reset after 2 seconds
                            scope.launch {
                                if (shareOpened.value) {
                                    delay(2000)
                                    shareOpened.value = false
                                }
                            }
                        }
                    },
                    text = stringResource(R.string.share_result),
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
                )
            }
            SecondaryButton(
                onClick = {
                    onTryAgain.invoke()
                },
                text = stringResource(R.string.try_again),
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
            )
        }

    }
}

@Composable
fun TimeTrialTextField(
    onClick: () -> Unit,
    viewModel: TimeTrialGameViewModel, nameState: MutableState<String>
) {

    TextField(
        value = nameState.value,
        onValueChange = {
            nameState.value = it
            viewModel.sendAttempt(nameState.value)
        },
        colors = TextFieldDefaults.colors(
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            focusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        ),
        label = {
            Text(stringResource(R.string.wrestler_name))
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        trailingIcon = {
            Image(
                painter = painterResource(id = R.drawable.checklist),
                contentDescription = stringResource(R.string.checklist),
                colorFilter = ColorFilter.tint(
                    MaterialTheme.colorScheme.onSurface
                ),
//                modifier = Modifier.clickable(
//                    enabled = true,
//                    onClick = onClick,
//                    indication = LocalIndication.current,
//                    interactionSource =  remember { MutableInteractionSource() }
//                )
            )
        }
    )

}



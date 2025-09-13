package com.dbtechprojects.dailywrestlequiz.android.ui.versus

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dbtechprojects.dailywrestlequiz.android.R
import com.dbtechprojects.dailywrestlequiz.android.ui.UiUtils
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.FullScreenLoadingSpinner
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.GreenButton
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.PrimaryBodyLarge
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.PrimaryButton
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.SurfaceSection
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.VersusViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun VersusScreen(
    viewModel: VersusViewModel,
    navHome: () -> Unit
){
    val progress by viewModel.progress.collectAsState()
    val remainingText by viewModel.timeRemainingText.collectAsState()
    val currentQuestion by viewModel.question.collectAsState()
    val currentIndex by viewModel.currentQuestionNumber.collectAsState()
    val questionsSize by viewModel.questionsAmount.collectAsState()
    val selectedAnswer by viewModel.selectedAnswer.collectAsState()
    val currentScore by viewModel.currentScore.collectAsState()
    val opponentScore by viewModel.opponentScore.collectAsState()
    val isGameOver by viewModel.isGameOver.collectAsState()
    val quizName by viewModel.quizName.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val customEndMessage by viewModel.customEndMessage.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            FullScreenLoadingSpinner()
            return@Box
        }

        AnimatedVisibility(
            visible = isGameOver,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 3000,
                    delayMillis = 3500
                )
            )

        ) {
            SurfaceSection {
                EndScreen(
                    quizName = quizName,
                    score = currentScore,
                    onNavigateHome = navHome,
                    customEndMessage = customEndMessage,
                    opponentScore = opponentScore
                )
            }

        }

        AnimatedVisibility(
            visible = !isGameOver,
            exit = fadeOut(
                animationSpec = tween(
                    durationMillis = 3000,
                    delayMillis = 1000
                )
            )
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                SurfaceSection {
                    GameScreen(
                        viewModel,
                        progress,
                        remainingText,
                        currentQuestion,
                        currentIndex,
                        questionsSize,
                        selectedAnswer,
                        currentScore
                    )
                }
                CurrentScore(
                    currentScore = currentScore,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(24.dp)
                )
            }
        }
    }

}

@Composable
fun EndScreen(
    quizName: String,
    score: Int,
    onNavigateHome: () -> Unit,
    opponentScore: Int = 0,
    customEndMessage: String?,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val shareOpened = remember { mutableStateOf(false) }
    var text = ""

    customEndMessage?.let {
        text = customEndMessage
    }

    val shareText = "I scored $score points against $quizName On the Daily Wrestling Trivia App !"


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
    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            if (opponentScore < score) {
                Image(
                    painter = painterResource(id = R.drawable.belt),
                    contentDescription = "Trivia Championship",
                    modifier = Modifier
                        .size(320.dp)
                        .padding(
                            bottom = 24.dp
                        )
                )
            }

            PrimaryButton(
                onClick = {
                    if (!shareOpened.value) {
                        shareOpened.value = true
                        UiUtils.sendShareEvent(
                            shareText,
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

            GreenButton(
                onClick = {
                    onNavigateHome.invoke()
                },
                text = stringResource(R.string.home),
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
            )
        }

    }
}
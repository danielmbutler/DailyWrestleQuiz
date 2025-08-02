package com.dbtechprojects.dailywrestlequiz.android.ui.question

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dbtechprojects.dailywrestlequiz.android.R
import com.dbtechprojects.dailywrestlequiz.android.ui.UiUtils
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.AutoResizedTextHeight
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.AutoResizedTextWidth
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.FullScreenLoadingSpinner
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.GreenButton
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.PrimaryBodyLarge
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.PrimaryBodyLargeAnimateSize
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.PrimaryButton
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.QuestionTimer
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.ReusableRow
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.SurfaceSection
import com.dbtechprojects.dailywrestlequiz.android.ui.theme.CorrectGreen
import com.dbtechprojects.dailywrestlequiz.android.ui.theme.IncorrectRed
import com.dbtechprojects.dailywrestlequiz.data.model.Question
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuestionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun QuestionScreen(
    viewModel: QuestionViewModel,
    navHome: () -> Unit,
) {
    val progress by viewModel.progress.collectAsState()
    val remainingText by viewModel.timeRemainingText.collectAsState()
    val currentQuestion by viewModel.state.collectAsState()
    val currentIndex by viewModel.currentQuestionNumber.collectAsState()
    val questionsSize by viewModel.questionsAmount.collectAsState()
    val selectedAnswer by viewModel.selectedAnswer.collectAsState()
    val currentScore by viewModel.currentScore.collectAsState()
    val isGameOver by viewModel.isGameOver.collectAsState()
    val quizName by viewModel.quizName.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val customEndMessage by viewModel.customEndMessage.collectAsState()
    val streak by viewModel.streak.collectAsState()

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
                    streak = streak,
                    onNavigateHome = navHome,
                    customEndMessage = customEndMessage
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
fun GameScreen(
    viewModel: QuestionViewModel,
    progress: Float,
    remainingText: String,
    currentQuestion: Question?,
    currentIndex: Int,
    questionsSize: Int,
    selectedAnswer: Int?,
    currentScore: Int
) {
    QuestionScreenHeaderRow(
        currentIndex,
        questionsSize = questionsSize
    )
    QuestionTimer(
        progress,
        remainingText
    )
    QuestionBox(
        question = currentQuestion?.question ?: ""
    )

    AnswerSection(
        answers = Question.getAnswers(currentQuestion?.answers ?: ""),
        onClickListener = { answer ->
            viewModel.setAnswer(answer)
        },
        correctAnswer = currentQuestion?.answer ?: 0,
        selectedAnswer = selectedAnswer
    )

    if (selectedAnswer != null) {
        ResultStatement(
            isCorrect = selectedAnswer == currentQuestion?.answer,
            isOutOfTime = selectedAnswer == -1,
            currentScore = currentScore,
            isLast = currentIndex == questionsSize
        )
        if (currentIndex != questionsSize) {
            NextButton { viewModel.requestNextQuestion() }
        }
    }
}


@Composable
fun EndScreen(
    quizName: String, score: Int,
    onNavigateHome: () -> Unit,
    customEndMessage: String?,
    streak: Int
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val shareOpened = remember { mutableStateOf(false) }
    var text = stringResource(R.string.congratulations)

    text += "\n You completed the $quizName quiz"

    text += "\n Your score was ${score}!"

    customEndMessage?.let {
        text = customEndMessage
    }

    val shareText = if (customEndMessage != null) {
        "My Streak is now $streak On the Daily Wrestling Trivia App !"
    } else {
        "I completed the $quizName quiz and scored $score. On the Daily Wrestling Trivia App !"
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
    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.belt),
                contentDescription = "Trivia Championship",
                modifier = Modifier
                    .size(320.dp)
                    .padding(
                        bottom = 24.dp
                    )
            )

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

@Composable
fun QuestionScreenHeaderRow(
    currentIndex: Int,
    questionsSize: Int
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        PrimaryBodyLarge("${currentIndex}/$questionsSize ")
        PrimaryBodyLarge(stringResource(R.string.app_name))
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
        AutoResizedTextHeight(
            text = question,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun AnswerSection(
    answers: List<String>,
    onClickListener: (Int) -> Unit,
    selectedAnswer: Int?,
    correctAnswer: Int,
) {
    LazyColumn(contentPadding = PaddingValues(12.dp)) {
        itemsIndexed(answers) { index, answer ->
            val isCorrect = index == correctAnswer
            val isSelected = index == selectedAnswer
            val answered = selectedAnswer != null
            val backgroundColor by animateColorAsState(
                targetValue = when {
                    isCorrect -> CorrectGreen// Green
                    isSelected -> IncorrectRed// Red
                    else -> MaterialTheme.colorScheme.background
                },
                animationSpec = tween(durationMillis = 500)
            )

            if (answered && !isCorrect && !isSelected) {
                return@itemsIndexed
            }
            ReusableRow(
                onClick = { if (selectedAnswer == null) onClickListener(index) },
                color = if (selectedAnswer == null) MaterialTheme.colorScheme.background
                else backgroundColor
            ) {
                AutoResizedTextWidth(
                    text = answer,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            if (index != answers.lastIndex) {
                Spacer(modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun ResultStatement(
    isCorrect: Boolean,
    isOutOfTime: Boolean,
    currentScore: Int,
    isLast: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Column {
            PrimaryBodyLarge(
                if (isCorrect) {
                    stringResource(R.string.question_answer_correct)
                } else if (isOutOfTime) {
                    stringResource(R.string.question_answer_out_of_time)
                } else {
                    stringResource(R.string.question_answer_incorrect)
                }
            )
            if (!isLast) {
                Row {
                    PrimaryBodyLarge(
                        text = stringResource(R.string.current_score)
                    )
                    PrimaryBodyLargeAnimateSize(
                        shouldAnimate = isCorrect,
                        text = "$currentScore"
                    )
                }
            }
        }

    }
}


@Composable
fun NextButton(
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(durationMillis = 1500, delayMillis = 300))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(24.dp)
            ) {
                PrimaryBodyLarge(
                    text = stringResource(R.string.next),
                )
            }
        }
    }
}

@Composable
fun CurrentScore(
    currentScore: Int,
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom
    ) {
        PrimaryBodyLarge(stringResource(R.string.current_score) + "${currentScore}")
    }
}




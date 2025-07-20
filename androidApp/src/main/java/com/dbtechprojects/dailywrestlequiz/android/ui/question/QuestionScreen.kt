package com.dbtechprojects.dailywrestlequiz.android.ui.question

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dbtechprojects.dailywrestlequiz.android.R
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.AutoResizedTextHeight
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.AutoResizedTextWidth
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.PrimaryBodyLarge
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.ReusableRow
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.SurfaceSection
import com.dbtechprojects.dailywrestlequiz.android.ui.theme.CorrectGreen
import com.dbtechprojects.dailywrestlequiz.android.ui.theme.IncorrectRed
import com.dbtechprojects.dailywrestlequiz.data.model.Question
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuestionViewModel


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

    Box(modifier = Modifier.fillMaxSize()) {
        SurfaceSection {
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
                    isOutOfTime = selectedAnswer == -1
                )
                NextButton { viewModel.requestNextQuestion() }
            }
        }
        CurrentScore(
            currentScore = currentScore,
            questionsSize = questionsSize,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        )
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
                    selectedAnswer == null -> MaterialTheme.colorScheme.inverseSurface
                    isCorrect -> CorrectGreen// Green
                    isSelected -> IncorrectRed// Red
                    else -> MaterialTheme.colorScheme.inverseSurface
                },
                animationSpec = tween(durationMillis = 500)
            )
            if (answered && !isCorrect && !isSelected) {
                return@itemsIndexed
            }
            ReusableRow(
                onClick = { if (selectedAnswer == null) onClickListener(index) },
                color = backgroundColor
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
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        PrimaryBodyLarge(
            if (isCorrect) {
                stringResource(R.string.question_answer_correct)
            } else if (isOutOfTime) {
                stringResource(R.string.question_answer_out_of_time)
            } else {
                stringResource(R.string.question_answer_incorrect)
            }
        )
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
    questionsSize: Int,
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom
    ) {
        PrimaryBodyLarge(stringResource(R.string.current_score) + "${currentScore}/$questionsSize ")
    }
}




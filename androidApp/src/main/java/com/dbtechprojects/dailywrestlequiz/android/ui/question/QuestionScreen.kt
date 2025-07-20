package com.dbtechprojects.dailywrestlequiz.android.ui.question

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

    SurfaceSection(
        contentSpacedBy = 12
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
            val isCorrect = selectedAnswer == currentQuestion?.answer
            val outOfTime = selectedAnswer == -1
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                PrimaryBodyLarge(
                    if (isCorrect) {
                        stringResource(R.string.question_answer_correct)
                    } else if(outOfTime) {
                        stringResource(R.string.question_answer_out_of_time)
                    } else {
                        stringResource(R.string.question_answer_incorrect)
                    }
                )
            }
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
                    selectedAnswer == null -> MaterialTheme.colorScheme.background
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




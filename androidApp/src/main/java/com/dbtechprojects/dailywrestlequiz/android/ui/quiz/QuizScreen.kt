package com.dbtechprojects.dailywrestlequiz.android.ui.quiz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dbtechprojects.dailywrestlequiz.android.R
import com.dbtechprojects.dailywrestlequiz.android.ui.UiUtils
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.FullScreenLoadingSpinner
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.PrimaryBodyLarge
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.PrimaryBorderedBox
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.ReusableRow
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.ScreenCenterTitle
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.SurfaceSection
import com.dbtechprojects.dailywrestlequiz.data.model.Quiz
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuizViewModel


@Composable
fun QuizScreen(
    quizViewModel: QuizViewModel,
    navigateToQuestion: (Quiz) -> Unit
) {
    val quizzes by quizViewModel.quizzes.collectAsState()
    val isLoading by quizViewModel.isLoading.collectAsState()

    SurfaceSection {
      ScreenCenterTitle(stringResource(R.string.wrestling_trivia))
        if (isLoading) {
            FullScreenLoadingSpinner()
            return@SurfaceSection
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuizSection(quizzes, onNavigateToQuestion = navigateToQuestion)
        }

    }
}



@Composable
fun QuizSection(
    quizzes: List<Quiz>,
    onNavigateToQuestion: (Quiz) -> Unit
) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(
            tween(durationMillis = 500)
        )
    ) {
        PrimaryBorderedBox(
            maxWidth = 1f,
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(quizzes) { index, it ->
                    ReusableRow(
                        color = UiUtils.hexToColor(it.color),
                        onClick = {
                            onNavigateToQuestion(it)
                        }
                    ) {
                        PrimaryBodyLarge(
                            it.name,
                            modifier= Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            textAlign = TextAlign.Center,
                            color = androidx.compose.ui.graphics.Color.White
                        )
                    }
                    if (index != quizzes.lastIndex) {
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}
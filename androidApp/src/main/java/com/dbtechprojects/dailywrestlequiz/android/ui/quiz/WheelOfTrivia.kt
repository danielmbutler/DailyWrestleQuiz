package com.dbtechprojects.dailywrestlequiz.android.ui.quiz

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.commandiron.spin_wheel_compose.SpinWheel
import com.commandiron.spin_wheel_compose.SpinWheelColors
import com.commandiron.spin_wheel_compose.SpinWheelDefaults
import com.commandiron.spin_wheel_compose.SpinWheelDimensions
import com.commandiron.spin_wheel_compose.state.SpinWheelState
import com.commandiron.spin_wheel_compose.state.rememberSpinWheelState
import com.dbtechprojects.dailywrestlequiz.android.R
import com.dbtechprojects.dailywrestlequiz.android.ui.UiUtils
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.AutoResizedTextWidth
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.FullScreenLoadingSpinner
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.PrimaryButton
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.ScreenCenterTitle
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.SurfaceSection
import com.dbtechprojects.dailywrestlequiz.android.ui.theme.asState
import com.dbtechprojects.dailywrestlequiz.data.model.Quiz
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuizViewModel
import kotlinx.coroutines.launch


@Composable
fun WheelOfTriviaScreen(
    quizViewModel: QuizViewModel,
    navigateToQuestion: (Quiz) -> Unit
) {
    val quizzes by quizViewModel.quizzes.collectAsState()
    val isLoading by quizViewModel.isLoading.collectAsState()
    var isSpinning by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    SurfaceSection {
        ScreenCenterTitle(stringResource(R.string.wheel_of_trivia))
        if (isLoading) {
            FullScreenLoadingSpinner()
            return@SurfaceSection
        }
        val state = rememberSpinWheelState(
            pieCount = quizzes.size,
            durationMillis = 7000
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            WheelSection(state, quizzes)
            PrimaryButton(
                modifier = Modifier.padding(32.dp),
                text =
                    if (!isSpinning) {
                        stringResource(R.string.spin)
                    } else {
                        stringResource(R.string.spinning)
                    },
                enabled = !isSpinning,
                onClick = {
                    if (!isSpinning) {
                        isSpinning = true
                        scope.launch {
                            state.spin {
                                val quiz = quizzes[it]
                                navigateToQuestion(quiz)
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun WheelSection(
    spinState: SpinWheelState,
    quizzes: List<Quiz>
) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(
            tween(durationMillis = 500)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Wheel(
                spinState,
                quizzes
            )
        }
    }
}

@Composable
fun Wheel(state: SpinWheelState, quizzes: List<Quiz>) {

    val wheelSize = remember { mutableStateOf(320.dp) }

    val colors = remember {
        mutableStateOf(
            quizzes.map { UiUtils.hexToColor(it.color) }
        )
    }

    val dimensions = object : SpinWheelDimensions {
        @Composable
        override fun frameWidth(): State<Dp> {
            return SpinWheelDefaults.spinWheelDimensions().frameWidth()
        }

        @Composable
        override fun selectorWidth(): State<Dp> {
            return SpinWheelDefaults.spinWheelDimensions().selectorWidth()
        }

        @Composable
        override fun spinWheelSize(): State<Dp> {
            return wheelSize
        }

    }

    val spinWheelColors = object : SpinWheelColors {
        @Composable
        override fun dividerColor(): State<Color> {
            return SpinWheelDefaults.spinWheelColors().dividerColor()
        }

        @Composable
        override fun frameColor(): State<Color> {
            return MaterialTheme.colorScheme.onBackground.asState()
        }

        @Composable
        override fun pieColors(): State<List<Color>> {
            return colors
        }

        @Composable
        override fun selectorColor(): State<Color> {
            return SpinWheelDefaults.spinWheelColors().selectorColor()
        }

    }

    SpinWheel(
        state = state,
        colors = spinWheelColors,
        dimensions = dimensions,
    ) { pieIndex ->
        AutoResizedTextWidth(
            text = quizzes[pieIndex].adaptNameForPie(),
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
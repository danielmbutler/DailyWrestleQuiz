package com.dbtechprojects.dailywrestlequiz.android.ui.previews

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dbtechprojects.dailywrestlequiz.android.MyApplicationTheme
import com.dbtechprojects.dailywrestlequiz.android.ui.home.HomeScreen
import com.dbtechprojects.dailywrestlequiz.android.ui.question.QuestionScreen
import com.dbtechprojects.dailywrestlequiz.android.ui.quiz.QuizScreen
import com.dbtechprojects.dailywrestlequiz.android.ui.quiz.WheelOfTriviaScreen
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuestionViewModelImpl
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuizViewModelStub


@Preview(
    name = "Home - Pixel 5", // Example of adding other standard device previews
    group = "Standard Devices",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = androidx.compose.ui.tooling.preview.Devices.PIXEL_5
)
@Composable
fun HomeScreenPreview() {
    MyApplicationTheme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScreen(
                onNavigateToDaily = {}, {}, {},
                {}
            )
        }
    }
}

@Preview(
    name = "Home - Pixel 5", // Example of adding other standard device previews
    group = "Standard Devices",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = androidx.compose.ui.tooling.preview.Devices.PIXEL_5
)
@Composable
fun QuizScreenPreview() {
    MyApplicationTheme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            QuizScreen(
                QuizViewModelStub(), {}
            )
        }
    }
}

@Preview(
    name = "Home - Pixel 5", // Example of adding other standard device previews
    group = "Standard Devices",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = androidx.compose.ui.tooling.preview.Devices.PIXEL_5
)
@Composable
fun QuestionScreenPreview() {
    MyApplicationTheme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            QuestionScreen(
                QuestionViewModelImpl.stub(),
                {}
            )
        }
    }
}

@Preview(
    name = "Home - Pixel 5", // Example of adding other standard device previews
    group = "Standard Devices",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = androidx.compose.ui.tooling.preview.Devices.PIXEL_5
)
@Composable
fun HomeScreenPreviewDark() {
    MyApplicationTheme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScreen(
                onNavigateToDaily = {}, {}, {},
                {}
            )
        }
    }
}

@Preview(
    name = "Home - Pixel 5", // Example of adding other standard device previews
    group = "Standard Devices",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = androidx.compose.ui.tooling.preview.Devices.PIXEL_5
)
@Composable
fun QuizScreenPreviewDark() {
    MyApplicationTheme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            QuizScreen(
                QuizViewModelStub(), {}
            )
        }
    }
}

@Preview(
    name = "Home - Pixel 5", // Example of adding other standard device previews
    group = "Standard Devices",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = androidx.compose.ui.tooling.preview.Devices.PIXEL_5
)
@Composable
fun QuestionScreenPreviewDark() {
    MyApplicationTheme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            QuestionScreen(
                QuestionViewModelImpl.stub(),
                {}
            )
        }
    }
}

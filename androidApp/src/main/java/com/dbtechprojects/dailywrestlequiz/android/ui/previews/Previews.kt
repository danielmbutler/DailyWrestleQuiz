package com.dbtechprojects.dailywrestlequiz.android.ui.previews

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.dbtechprojects.dailywrestlequiz.android.MyApplicationTheme
import com.dbtechprojects.dailywrestlequiz.android.ui.home.HomeScreen
import com.dbtechprojects.dailywrestlequiz.android.ui.question.QuestionScreen
import com.dbtechprojects.dailywrestlequiz.android.ui.quiz.QuizScreen
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.HomeViewModelStub
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuestionViewModelImpl
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuizViewModelStub


@Preview(
    name = "Home - Custom Low DPI (mdpi)",
    group = "DPI Scaling", // Optional: Group previews in the UI
    device = "spec:width=360dp,height=640dp,dpi=160", // mdpi
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Home - Custom High DPI (xxxhdpi)",
    group = "DPI Scaling",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=411dp,height=891dp,dpi=640" // xxxhdpi
)
@Preview(
    name = "Home - Pixel 5", // Example of adding other standard device previews
    group = "Standard Devices",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_5
)
@Preview(
    name = "Home - High DPI - Large Font", // Example with font scaling
    group = "Font Scaling",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=411dp,height=891dp,dpi=640",
    fontScale = 1.15f
)
@Composable
fun HomeScreenPreviewFs1() {
    MyApplicationTheme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScreen(
                onNavigateToDaily = {},
                onNavigateToTrivia = {},
                onNavigateToVersus = {},
                onNavigateToTimeTrial = {},
                viewModel = HomeViewModelStub()
            )
        }
    }
}

@Preview(
    name = "Home - Custom Low DPI (mdpi)",
    group = "DPI Scaling", // Optional: Group previews in the UI
    device = "spec:width=360dp,height=640dp,dpi=160", // mdpi
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Home - Custom High DPI (xxxhdpi)",
    group = "DPI Scaling",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=411dp,height=891dp,dpi=640" // xxxhdpi
)
@Preview(
    name = "Home - Pixel 5", // Example of adding other standard device previews
    group = "Standard Devices",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_5
)
@Preview(
    name = "Home - High DPI - Large Font", // Example with font scaling
    group = "Font Scaling",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=411dp,height=891dp,dpi=640",
    fontScale = 1.15f
)
@Composable
fun QuestionScreenPreviewFs1() {
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
    name = "Home - Custom Low DPI (mdpi)",
    group = "DPI Scaling", // Optional: Group previews in the UI
    device = "spec:width=360dp,height=640dp,dpi=160", // mdpi
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Home - Custom High DPI (xxxhdpi)",
    group = "DPI Scaling",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=411dp,height=891dp,dpi=640" // xxxhdpi
)
@Preview(
    name = "Home - Pixel 5", // Example of adding other standard device previews
    group = "Standard Devices",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_5
)
@Preview(
    name = "Home - High DPI - Large Font", // Example with font scaling
    group = "Font Scaling",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=411dp,height=891dp,dpi=640",
    fontScale = 1.15f
)
@Composable
fun QuizScreenPreviewFs1() {
    MyApplicationTheme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            QuizScreen(
                QuizViewModelStub()
            ) {}
        }
    }
}
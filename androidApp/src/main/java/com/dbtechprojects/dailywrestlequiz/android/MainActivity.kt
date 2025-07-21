package com.dbtechprojects.dailywrestlequiz.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dbtechprojects.dailywrestlequiz.android.ui.NavRoutes
import com.dbtechprojects.dailywrestlequiz.android.ui.NavViewModel
import com.dbtechprojects.dailywrestlequiz.android.ui.home.HomeScreen
import com.dbtechprojects.dailywrestlequiz.android.ui.question.QuestionScreen
import com.dbtechprojects.dailywrestlequiz.android.ui.quiz.QuizScreen
import com.dbtechprojects.dailywrestlequiz.android.ui.quiz.WheelOfTriviaScreen
import com.dbtechprojects.dailywrestlequiz.android.viewmodel.QuestionViewModelWrapper
import com.dbtechprojects.dailywrestlequiz.android.viewmodel.getQuestionViewModel
import com.dbtechprojects.dailywrestlequiz.android.viewmodel.getQuizViewModel
import com.dbtechprojects.dailywrestlequiz.data.model.Quiz
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuestionViewModel
import org.koin.core.parameter.parametersOf
import org.koin.mp.KoinPlatform.getKoin

class MainActivity : ComponentActivity() {
    private val navViewModel: NavViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost(navViewModel = navViewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    navViewModel: NavViewModel
) {
    NavHost(navController = navController, startDestination = NavRoutes.HOME) {

        composable(NavRoutes.HOME) {
            HomeScreen(
                onNavigateToDaily = {
                    navController.navigate(NavRoutes.WHEEL_OF_TRIVIA)
                },
                onNavigateToTrivia = {
                    navController.navigate(NavRoutes.QUIZ)
                },
                onNavigateToTimeTrial = {
                    // TODO: Add navigation route
                },
                onNavigateToVersus = {
                    // TODO: Add navigation route
                }
            )
        }

        composable(NavRoutes.WHEEL_OF_TRIVIA) {
            WheelOfTriviaScreen(getQuizViewModel(), navigateToQuestion = {
                navViewModel.setSelectedQuiz(it)
                navController.navigate(NavRoutes.QUESTION)
            })
        }

        composable(NavRoutes.QUIZ) {
            QuizScreen(getQuizViewModel(),navigateToQuestion = {
                navViewModel.setSelectedQuiz(it)
                navController.navigate(NavRoutes.QUESTION)
            })
        }

        composable(NavRoutes.QUESTION) {
            val quiz = remember { navViewModel.getSelectedQuiz() }
            quiz?.let { it1 ->
                QuestionScreen(getQuestionViewModel(it1)) {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            }
        }
    }
}

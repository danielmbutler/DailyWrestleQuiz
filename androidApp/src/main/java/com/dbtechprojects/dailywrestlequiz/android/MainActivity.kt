package com.dbtechprojects.dailywrestlequiz.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dbtechprojects.dailywrestlequiz.android.ui.NavRoutes
import com.dbtechprojects.dailywrestlequiz.android.ui.home.HomeScreen
import com.dbtechprojects.dailywrestlequiz.android.ui.question.QuestionScreen
import com.dbtechprojects.dailywrestlequiz.android.ui.quiz.QuizScreen
import com.dbtechprojects.dailywrestlequiz.android.ui.quiz.WheelOfTriviaScreen
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.BackButton
import com.dbtechprojects.dailywrestlequiz.android.ui.timetrial.TimeTrialGameScreen
import com.dbtechprojects.dailywrestlequiz.android.ui.timetrial.TimeTrialListScreen
import com.dbtechprojects.dailywrestlequiz.android.ui.versus.VersusListScreen
import com.dbtechprojects.dailywrestlequiz.android.ui.versus.VersusScreen
import com.dbtechprojects.dailywrestlequiz.android.viewmodel.ArgPersistenceImplementation
import com.dbtechprojects.dailywrestlequiz.android.viewmodel.HomeViewModelFactory
import com.dbtechprojects.dailywrestlequiz.android.viewmodel.getHomeViewModel
import com.dbtechprojects.dailywrestlequiz.android.viewmodel.getQuestionViewModel
import com.dbtechprojects.dailywrestlequiz.android.viewmodel.getQuizViewModel
import com.dbtechprojects.dailywrestlequiz.android.viewmodel.getTimeTrialGameViewModel
import com.dbtechprojects.dailywrestlequiz.android.viewmodel.getTimeTrialListViewModel
import com.dbtechprojects.dailywrestlequiz.android.viewmodel.getVersusViewModel
import com.dbtechprojects.dailywrestlequiz.data.di.ArgPersistence
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuestionViewModel
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.TimeTrialGameViewModel
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.VersusViewModel


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost()
                }
            }
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
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
                    navController.navigate(NavRoutes.TIME_TRIAL)
                },
                onNavigateToVersus = {
                    navController.navigate(NavRoutes.VERSUS)
                },
                viewModel = getHomeViewModel()
            )
        }

        composable(NavRoutes.WHEEL_OF_TRIVIA) {
            WheelOfTriviaScreen(getQuizViewModel(), navigateToQuestion = {
                navController.navigate(
                    "${NavRoutes.QUESTION}/$it?" +
                            "${QuestionViewModel.ARG_QUESTION_COUNT}=1"
                )
            })
            BackButton(navController)
        }

        composable(NavRoutes.QUIZ) {
            QuizScreen(getQuizViewModel(), navigateToQuestion = {
                navController.navigate("" +
                        "${NavRoutes.QUESTION}/$it?" +
                        "${QuestionViewModel.ARG_QUESTION_COUNT}=0")
            })
            BackButton(navController)
        }
        composable(NavRoutes.TIME_TRIAL) {
            TimeTrialListScreen(
                getTimeTrialListViewModel(), navigateToTimeTrial =
                    {
                        navController.navigate("${NavRoutes.TIME_TRIAL_GAME}/$it")
                    }
            )
            BackButton(navController)
        }

        composable(
            route = "${NavRoutes.QUESTION}/{${QuestionViewModel.ARG_QUIZ_ID}}?${QuestionViewModel.ARG_QUESTION_COUNT}={${QuestionViewModel.ARG_QUESTION_COUNT}}",
            arguments = listOf(
                navArgument(QuestionViewModel.ARG_QUIZ_ID) {
                    type = NavType.IntType
                },
                navArgument(QuestionViewModel.ARG_QUESTION_COUNT) {
                    type = NavType.IntType
                }
            )
        ) { entry ->

           val args = ArgPersistenceImplementation<Int?>(savedStateHandle = entry.savedStateHandle)

            QuestionScreen(getQuestionViewModel(args)) {
                navController.navigate(NavRoutes.HOME) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        }

        composable(
            route = "${NavRoutes.TIME_TRIAL_GAME}/{${TimeTrialGameViewModel.ARG_TIME_TRIAL_ID}}",
            arguments = listOf(
                navArgument(TimeTrialGameViewModel.ARG_TIME_TRIAL_ID) {
                    type = NavType.IntType
                }
            )
        ) { entry ->

            val args = ArgPersistenceImplementation<Int>(savedStateHandle = entry.savedStateHandle )

            TimeTrialGameScreen(getTimeTrialGameViewModel(args))
        }

        composable(NavRoutes.VERSUS) {

            VersusListScreen(
                getVersusViewModel(
                    object :ArgPersistence<String?>{
                        override fun get(argName: String): String? {
                            return null
                        }
                    }
                ),
                navToVersusGame = {
                    navController.navigate("${NavRoutes.VERSUS_GAME}/$it")
                }
            )
            BackButton(navController)
        }

        composable(
            route = "${NavRoutes.VERSUS_GAME}/{${VersusViewModel.ARG_QUIZ_NAME}}",
            arguments = listOf(
                navArgument(VersusViewModel.ARG_QUIZ_NAME) {
                    type = NavType.StringType
                }
            )
        ) { entry ->

            val args = ArgPersistenceImplementation<String?>(savedStateHandle = entry.savedStateHandle )

            VersusScreen(getVersusViewModel(args),  {
                navController.navigate(NavRoutes.HOME) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            })
        }
    }
}

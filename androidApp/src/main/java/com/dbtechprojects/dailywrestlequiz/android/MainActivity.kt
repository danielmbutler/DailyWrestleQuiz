package com.dbtechprojects.dailywrestlequiz.android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.dbtechprojects.dailywrestlequiz.android.ui.NavRoutes
import com.dbtechprojects.dailywrestlequiz.android.ui.home.HomeScreen
import com.dbtechprojects.dailywrestlequiz.android.ui.question.QuestionScreen
import com.dbtechprojects.dailywrestlequiz.android.ui.quiz.QuizScreen
import com.dbtechprojects.dailywrestlequiz.android.ui.quiz.WheelOfTriviaScreen
import com.dbtechprojects.dailywrestlequiz.android.ui.timetrial.TimeTrialGameScreen
import com.dbtechprojects.dailywrestlequiz.android.ui.timetrial.TimeTrialListScreen
import com.dbtechprojects.dailywrestlequiz.android.ui.versus.VersusListScreen
import com.dbtechprojects.dailywrestlequiz.android.ui.versus.VersusScreen
import com.dbtechprojects.dailywrestlequiz.android.viewmodel.HomeViewModelFactory
import com.dbtechprojects.dailywrestlequiz.android.viewmodel.QuestionViewModelFactory
import com.dbtechprojects.dailywrestlequiz.android.viewmodel.getHomeViewModel
import com.dbtechprojects.dailywrestlequiz.android.viewmodel.getQuestionViewModel
import com.dbtechprojects.dailywrestlequiz.android.viewmodel.getQuizViewModel
import com.dbtechprojects.dailywrestlequiz.android.viewmodel.getTimeTrialGameViewModel
import com.dbtechprojects.dailywrestlequiz.android.viewmodel.getTimeTrialListViewModel
import com.dbtechprojects.dailywrestlequiz.android.viewmodel.getVersusViewModel
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuestionViewModel
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.QuestionViewModelArgs
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.TimeTrialGameViewModel
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.VersusViewModel
import java.util.Map.entry
import java.util.UUID


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

) {
    val backStack = rememberNavBackStack(NavRoutes.Home)
    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            // Add the default decorators for managing scenes and saving state
            rememberSaveableStateHolderNavEntryDecorator(),
            // Then add the view model store decorator
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<NavRoutes.Home> {
                HomeScreen(
                    onNavigateToDaily = {
                        backStack.add(NavRoutes.WheelOfTrivia)
                    },
                    onNavigateToTrivia = {
                        backStack.add(NavRoutes.Quiz)
                    },
                    onNavigateToTimeTrial = {
                        backStack.add(NavRoutes.TimeTrial)
                    },
                    onNavigateToVersus = {
                        backStack.add(NavRoutes.Versus)
                    },
                    viewModel = getHomeViewModel()
                )
            }
            entry<NavRoutes.WheelOfTrivia> {
                WheelOfTriviaScreen(getQuizViewModel(), navigateToQuestion = { id ->
                    backStack.add(NavRoutes.Question(id, 1))
                })
            }
            entry<NavRoutes.Quiz> {
                QuizScreen(getQuizViewModel(), navigateToQuestion = { id ->
                    backStack.add(NavRoutes.Question(id, 0))
                })
            }
            entry<NavRoutes.TimeTrial> {
                TimeTrialListScreen(getTimeTrialListViewModel(), navigateToTimeTrial = {
                    backStack.add(NavRoutes.TimeTrialGame(it))
                })
            }
            entry<NavRoutes.Question> {
                val questionViewModel: QuestionViewModel = viewModel(
                    key = "QuestionViewModel",
                    factory =
                        QuestionViewModelFactory(QuestionViewModelArgs(it.amount, it.quizId))
                )
                QuestionScreen(
                    questionViewModel
                ) {
                    while (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    }
                }
            }
            entry<NavRoutes.TimeTrialGame> {
                TimeTrialGameScreen(getTimeTrialGameViewModel(it.timeTrialId))
            }
            entry<NavRoutes.Versus> {
                VersusListScreen(getVersusViewModel(null), navToVersusGame = {
                    backStack.add(NavRoutes.VersusGame(it))
                })
            }
            entry<NavRoutes.VersusGame> {
                it
                VersusScreen(getVersusViewModel(it.quizName), {
                    while (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    }
                })
            }
        }
    )
}


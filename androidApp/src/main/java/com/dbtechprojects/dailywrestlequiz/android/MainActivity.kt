package com.dbtechprojects.dailywrestlequiz.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dbtechprojects.dailywrestlequiz.android.ui.home.HomeScreen
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.HomeViewModel
import org.koin.mp.KoinPlatform.getKoin

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
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            val viewModel = getKoin().get<HomeViewModel>()
            HomeScreen(viewModel = viewModel) {
                navController.navigate("detail")
            }
        }

        composable("detail") {

        }
    }
}

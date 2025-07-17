package com.dbtechprojects.dailywrestlequiz.android.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.HomeViewModel
import org.koin.core.component.get

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToDetail: () -> Unit
) {
    val question by viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            color = MaterialTheme.colorScheme.onBackground,
            text = question?.question ?: "No question")

        Button(onClick = {
            viewModel.requestQuestion()
        }) {
            Text(
                text = "Click here")
        }
    }
}
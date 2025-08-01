package com.dbtechprojects.dailywrestlequiz.android.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dbtechprojects.dailywrestlequiz.android.R
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.ImageRow
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.PrimaryBodySmall
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.PrimaryBodyLarge
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.PrimaryBorderedBox
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.PrimaryButton
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToDaily: () -> Unit,
    onNavigateToTrivia: () -> Unit,
    onNavigateToTimeTrial: () -> Unit,
    onNavigateToVersus: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AppTitle()
        HomeCallToAction {
            onNavigateToDaily.invoke()
        }
        StreakSection()
        Options(
            onNavigateToTrivia,
            onNavigateToTimeTrial,
            onNavigateToVersus
        )
    }
}

@Composable
fun AppTitle() {
    Text(
        color = MaterialTheme.colorScheme.onBackground,
        text = stringResource(R.string.home_title),
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.padding(top = 24.dp, bottom = 24.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun HomeCallToAction(onClick: () -> Unit) {
    PrimaryBorderedBox {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PrimaryBodyLarge(stringResource(R.string.home_todays_match))
            PrimaryBodySmall(stringResource(R.string.home_match_description))
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(0.75f),
                onClick = { onClick.invoke() }, text = stringResource(R.string.home_start_button)
            )
        }
    }

}

@Composable
fun StreakSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.fire),
                contentDescription = stringResource(R.string.current_streak),
            )
            Text(
                text = stringResource(R.string.home_streak_text, 5),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }

}

@Composable
fun Options(
    onNavigateToTrivia: () -> Unit,
    onNavigateToTimeTrial: () -> Unit,
    onNavigateToVersus: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(0.75f),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ImageRow(
            drawable = R.drawable.gamepad,
            color = MaterialTheme.colorScheme.surface,
            label = stringResource(R.string.trivia_mode),
            onClick = {onNavigateToTrivia.invoke()}
        )
        ImageRow(
            drawable = R.drawable.timer,
            color = MaterialTheme.colorScheme.surface,
            label = stringResource(R.string.time_trials_mode),
            onClick = {onNavigateToTimeTrial.invoke()}
        )
        ImageRow(
            drawable = R.drawable.group,
            color = MaterialTheme.colorScheme.surface,
            label = stringResource(R.string.versus_mode),
            onClick = {onNavigateToVersus.invoke()}
        )
    }

}
package com.dbtechprojects.dailywrestlequiz.android.ui.timetrial

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dbtechprojects.dailywrestlequiz.android.R
import com.dbtechprojects.dailywrestlequiz.android.ui.UiUtils
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.PrimaryBodyLarge
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.PrimaryBodySmall
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.PrimaryBorderedBox
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.ReusableRow
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.ScreenCenterTitle
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.SurfaceSection
import com.dbtechprojects.dailywrestlequiz.android.ui.theme.WrestleGold
import com.dbtechprojects.dailywrestlequiz.data.model.TimeTrial
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.TimeTrialListViewModel


@Composable
fun TimeTrialListScreen(
    timeTrialListViewModel: TimeTrialListViewModel,
    navigateToTimeTrial: (id: Int) -> Unit
) {
    val trials by timeTrialListViewModel.timeTrials.collectAsState()

    SurfaceSection {
        ScreenCenterTitle(
            stringResource(R.string.time_trials_title),
            stringResource(R.string.how_fast_can_you_answer)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TimeTrialsSection(trials, navigate = navigateToTimeTrial)
        }

    }
}


@Composable
fun TimeTrialsSection(
    list: List<TimeTrial>,
    navigate: (id: Int) -> Unit
) {
    val itemHeight = 80.dp
    val spacing = 8.dp
    val maxItems = 4
    val maxHeight = (itemHeight * maxItems) + (spacing * (maxItems - 1))

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
                modifier = Modifier
                    .height(maxHeight).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(list) { index, it ->
                    ReusableRow(
                        color = UiUtils.hexToColor(it.color),
                        onClick = {
                            navigate(it.id)
                        }
                    ) {
                        PrimaryBodyLarge(
                            it.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                        if (it.highScore.isNotEmpty()) {
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                PrimaryBodySmall(stringResource(R.string.fastest_Time) + ": " + it.highScore)
                            }
                        }
                    }
                    if (index != list.lastIndex) {
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}
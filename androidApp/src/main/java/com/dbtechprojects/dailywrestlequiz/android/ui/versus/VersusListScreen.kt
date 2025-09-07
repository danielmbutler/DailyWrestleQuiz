package com.dbtechprojects.dailywrestlequiz.android.ui.versus

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dbtechprojects.dailywrestlequiz.android.R
import com.dbtechprojects.dailywrestlequiz.android.ui.UiUtils
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.BackButton
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.PrimaryBodyLarge
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.PrimaryBodySmall
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.PrimaryBorderedBox
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.ReusableRow
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.ScreenCenterTitle
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.SurfaceSection
import com.dbtechprojects.dailywrestlequiz.android.ui.theme.WrestleGold
import com.dbtechprojects.dailywrestlequiz.data.model.TimeTrial
import com.dbtechprojects.dailywrestlequiz.data.model.VersusMode
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.TimeTrialListViewModel
import com.dbtechprojects.dailywrestlequiz.data.viewmodels.VersusViewModel


@Composable
fun VersusListScreen(
    versusViewModel: VersusViewModel
) {
    val versusModes by versusViewModel.versusModes.collectAsState()

    SurfaceSection {
        ScreenCenterTitle(
            stringResource(R.string.versus_mode),
            stringResource(R.string.can_you_beat_our_champions)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            VersusSection(versusModes)
        }

    }
}

fun getImageRes(versusMode: VersusMode): Int {
    return when (versusMode.name) {
        VersusMode.FACT_FIEND -> {
            R.drawable.fact_fiend
        }

        VersusMode.QUIZ_MASTER -> {
            R.drawable.quiz_master
        }

        VersusMode.TRIVIA_TITAN -> {
            R.drawable.trivia_titan
        }

        else -> {
            R.drawable.quiz_master
        }
    }
}


@Composable
fun VersusSection(
    list: List<VersusMode>,
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
                    .height(maxHeight)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(list) { index, it ->
                    ReusableRow(
                        color = UiUtils.hexToColor(it.color),
                        onClick = {}
                    ) {

                        Image(
                            painter = painterResource(id = getImageRes(it)),
                            contentDescription = it.name,
                            modifier = Modifier
                                .size(120.dp)
                                .padding(12.dp)
                        )

                        PrimaryBodyLarge(
                            it.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
//                        if (it.highScore.isNotEmpty()) {
//                            Row(
//                                horizontalArrangement = Arrangement.End,
//                                modifier = Modifier.fillMaxWidth()
//                            ) {
//                                PrimaryBodySmall(stringResource(R.string.fastest_Time) + ": " + it.highScore)
//                            }
//                        }
                    }
                    if (index != list.lastIndex) {
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}
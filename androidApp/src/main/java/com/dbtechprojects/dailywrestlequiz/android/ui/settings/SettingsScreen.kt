package com.dbtechprojects.dailywrestlequiz.android.ui.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dbtechprojects.dailywrestlequiz.android.R
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.ScreenCenterTitle

@Composable
fun SettingsScreen() {
    var showPrivacy by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        // Title
        ScreenCenterTitle(
            stringResource(R.string.settings),
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { showPrivacy = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Text(text = "Privacy Policy", style = MaterialTheme.typography.bodyLarge)
        }

        Button(
            onClick = { /* no-op for now */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Clear Data", style = MaterialTheme.typography.bodyLarge)
        }
    }

    if (showPrivacy) {
        Dialog(onDismissRequest = { showPrivacy = false }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
            Surface(modifier = Modifier.fillMaxSize(), shape = MaterialTheme.shapes.medium) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Content (now scrollable for long privacy text)
                    val scrollState = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 12.dp)
                            .verticalScroll(scrollState),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Privacy Policy", style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.height(12.dp))
                        // Long dummy text to demonstrate scrolling. Replace with real content as needed.
                        Text(text = (1..50).joinToString(separator = "\n\n") { "This is some dummy privacy policy text. Replace with actual content. Paragraph #$it." })
                    }

                    // Close button top-right — use the ic_close vector drawable. Placed after content so it is on top and clickable.
                    IconButton(onClick = { showPrivacy = false }, modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)) {
                        Icon(painter = painterResource(id = R.drawable.ic_close), contentDescription = stringResource(R.string.close))
                    }
                }
            }
        }
    }
}

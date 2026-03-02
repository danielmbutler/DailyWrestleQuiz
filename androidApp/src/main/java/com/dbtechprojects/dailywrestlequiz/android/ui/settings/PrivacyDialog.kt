package com.dbtechprojects.dailywrestlequiz.android.ui.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dbtechprojects.dailywrestlequiz.android.R


@Composable
fun PrivacyDialog(
    onDismiss: () -> Unit,
    onClose: () -> Unit
) {

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
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
                IconButton(onClick =onClose, modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)) {
                    Icon(painter = painterResource(id = R.drawable.ic_close), contentDescription = stringResource(R.string.close))
                }
            }
        }
    }
}
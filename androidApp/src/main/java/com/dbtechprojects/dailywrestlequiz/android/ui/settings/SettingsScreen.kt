package com.dbtechprojects.dailywrestlequiz.android.ui.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dbtechprojects.dailywrestlequiz.android.R
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.RoundedSmallDialogTwoOption
import com.dbtechprojects.dailywrestlequiz.android.ui.shared.ScreenCenterTitle

@Composable
fun SettingsScreen(
    onClearDataPress: () -> Unit
) {
    var showPrivacy by remember { mutableStateOf(false) }
    var showClearDataDialog by remember { mutableStateOf(false) }
    var dataCleared by remember { mutableStateOf(false) }

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
            onClick = { showClearDataDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Clear Data", style = MaterialTheme.typography.bodyLarge)
        }
    }
    if (dataCleared){
        Toast.makeText(
            LocalContext.current,
            "All data cleared",
            Toast.LENGTH_SHORT
        ).show()
    }

    if (showPrivacy) {
        PrivacyDialog(
            onDismiss = { showPrivacy = false },
            onClose = { showPrivacy = false }
        )
    }
    if (showClearDataDialog) {
        RoundedSmallDialogTwoOption(
            onConfirm = {
                onClearDataPress.invoke()
                showClearDataDialog = false
                dataCleared = true
            },
            closeDialog = { showClearDataDialog = false },
            title = stringResource(R.string.are_you_sure_you_want_to_clear_all_data)
        )
    }
}

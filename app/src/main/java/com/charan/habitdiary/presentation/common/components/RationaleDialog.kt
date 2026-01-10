package com.charan.habitdiary.presentation.common.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.charan.habitdiary.R

@Composable
fun RationaleDialog(
    title: String,
    message: String,
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(title)
        },
        text = {
            Text(message)
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmRequest
            ) {
                Text(stringResource(R.string.open_settings))
            }

        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
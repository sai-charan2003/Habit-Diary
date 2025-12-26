package com.charan.habitdiary.presentation.common.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.charan.habitdiary.R

@Composable

fun DeleteWarningDialog(
    onConfirm : () -> Unit,
    onDismiss : () -> Unit,
    title : String = stringResource(R.string.delete),
    message : String = stringResource(R.string.delete_confirmation_description)
) {
    AlertDialog(
        title = {
            Text(title)
        },
        text = {
            Text(message)
        },
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(R.string.cancel))
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(stringResource(R.string.delete))
            }
        }
    )
}
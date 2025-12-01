package com.charan.habitdiary.presentation.common.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable

fun DeleteWarningDialog(
    onConfirm : () -> Unit,
    onDismiss : () -> Unit,
    title : String = "Delete",
    message : String = "Are you sure you want to delete this item?"
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
                Text("Cancel")
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("Delete")
            }
        }
    )
}
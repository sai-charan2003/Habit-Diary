package com.charan.habitdiary.presentation.common.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable

@Composable
fun SelectDateDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (Long?) -> Unit,
    dateMillis : Long,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = dateMillis
    )
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(
                    datePickerState.selectedDateMillis
                )
            }) {
                Text("OK")
            }
        }

    ) {
        DatePicker(datePickerState)
    }
}
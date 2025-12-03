package com.charan.habitdiary.presentation.common.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDateDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (Long?) -> Unit,
    dateMillis : LocalDate,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDate = dateMillis.toJavaLocalDate()
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
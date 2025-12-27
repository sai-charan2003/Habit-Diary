package com.charan.habitdiary.presentation.common.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.charan.habitdiary.R

@Composable
fun ActionButtonRow(
    saveButtonText : String = stringResource(R.string.save),
    deleteButtonText : String = stringResource(R.string.delete),
    onSave : () -> Unit,
    onDelete : () -> Unit,
    showDeleteButton : Boolean = false,
    isSaveEnabled : Boolean = true
) {
    Row(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 5.dp)
    ) {
        Button(
            onClick = onSave,
            modifier = Modifier.weight(1f),
            enabled = isSaveEnabled
        ) {
            Text(saveButtonText)
        }

        if(showDeleteButton) {
            Spacer(Modifier.width(10.dp))

            OutlinedButton(
                onClick = onDelete,
                modifier = Modifier
                    .weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )

            ) {
                Text(deleteButtonText)
            }
        }
    }
}

@Preview
@Composable
fun ActionButtonRowPreview() {
    ActionButtonRow(
        onSave = { },
        onDelete = { }
    )
}
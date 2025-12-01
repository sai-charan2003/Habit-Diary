package com.charan.habitdiary.presentation.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SettingsRowItem(
    title : String,
    trailingContent : @Composable () -> Unit,

) {
    Row (
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(title)
        trailingContent()
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsRowItemPreview() {
    SettingsRowItem(
        title = "Sample Setting",
        trailingContent = {
            Text("Enabled")
        }
    )

}
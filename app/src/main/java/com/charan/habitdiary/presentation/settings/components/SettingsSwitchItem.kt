package com.charan.habitdiary.presentation.settings.components

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import com.charan.habitdiary.presentation.common.components.CustomListItem
import com.charan.habitdiary.ui.theme.IndexItem

@Composable
fun SettingsSwitchItem(
    title : String,
    index : IndexItem,
    isChecked : Boolean,
    onCheckedChange : (Boolean) -> Unit
) {
    CustomListItem(
        indexItem = index,
        content = {
            SettingsRowItem(
                title = title,
                trailingContent = {
                    Switch(
                        checked = isChecked,
                        onCheckedChange = onCheckedChange
                    )

                }
            )
        },
        onClick = {
            onCheckedChange(!isChecked)
        }
    )

}
package com.charan.habitdiary.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.material3.DropdownMenuGroup
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenuPopup
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.fastForEachIndexed

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun<T> CustomDropDown(
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    isExpanded : Boolean = false,
    onDismiss : () -> Unit = { },
    containerColor : Color = MaterialTheme.colorScheme.surfaceContainerHigh
) {
        DropdownMenuPopup(
            expanded = isExpanded,
            onDismissRequest = {
                onDismiss()
            },

        ) {
                DropdownMenuGroup(
                    shapes = MenuDefaults.groupShape(0, 1),
                    containerColor = containerColor

                    ) {
                    items.fastForEachIndexed { index, item ->
                        DropdownMenuItem(
                            text = {
                                Text(text = stringResource(item as Int))
                            },
                            shapes = MenuDefaults.itemShape(index, items.size),
                            onCheckedChange = {
                                onItemSelected(item)
                            },
                            checked = item == selectedItem,
                            colors = MenuDefaults.selectableItemColors().copy(
                                containerColor = containerColor
                            )
                        )
                    }
                }

        }


}
package com.charan.habitdiary.presentation.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FabMenu(
    onAddHabitClick: () -> Unit,
    onAddDailyLogClick: () -> Unit,
    isExpanded: Boolean = false,
    onFabClick: () -> Unit
) {
    val items = listOf(
        FabMenuItem(
            label = "Add Habit",
            icon = Icons.Rounded.Add,
            onClick = onAddHabitClick
        ),
        FabMenuItem(
            label = "Add Daily Log",
            icon = Icons.Rounded.Add,
            onClick = onAddDailyLogClick
        )
    )

    FloatingActionButtonMenu(
        expanded = isExpanded,
        button = {
            ToggleFloatingActionButton(
                checked = isExpanded,
                onCheckedChange = { onFabClick() }
            ) {
                Icon(
                    imageVector = if (isExpanded) Icons.Rounded.Close else Icons.Rounded.Add,
                    contentDescription = "Menu toggle",
                    tint = if (isExpanded)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    ) {
        items.forEach { item ->
            FloatingActionButtonMenuItem(
                onClick = item.onClick,
                text = { Text(item.label) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                }
            )
        }
    }
}

data class FabMenuItem(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

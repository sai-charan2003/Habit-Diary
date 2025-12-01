package com.charan.habitdiary.presentation.add_habit.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.charan.habitdiary.utils.DateUtil

@Suppress("NonSkippableComposable")
@Composable
fun ScheduleHabitComponent(
    selectedTime: String,
    onTimeClick: () -> Unit,
    selectedDays: List<Int>,
    onDayToggle: (Int) -> Unit,
    daysInitials: List<String> = DateUtil.getDayInitials()
) {
    SectionContainer(title = "When will you do this?") {

        LabelActionListItem(
            label = "Time",
            icon = { Icon(Icons.Rounded.Schedule, contentDescription = null) },
            tailingContent = {
                TextButton(onClick = onTimeClick) {
                    Text(selectedTime)
                }
            }
        )

        Spacer(Modifier.height(20.dp))

        Text("Repeat", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(8.dp))

        SelectDaysItem(
            daysInitials = daysInitials,
            selectedDays = selectedDays,
            onDayToggle = onDayToggle
        )
    }
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SelectDaysItem(
    daysInitials: List<String>,
    selectedDays: List<Int>,
    onDayToggle: (Int) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Rounded.Repeat,
            contentDescription = "Repeat Icon"
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Days")
    }

    Spacer(modifier = Modifier.height(8.dp))

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement
            .spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween, Alignment.CenterHorizontally)

    ) {
        daysInitials.forEachIndexed { index, label ->
            ToggleButton(
                checked = selectedDays.contains(index),
                onCheckedChange = { onDayToggle(index) },
                shapes = when (index) {
                    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                    daysInitials.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                }
            ) {
                Text(label)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleFieldPreview() {
    ScheduleHabitComponent(
        selectedTime = "08:00 AM",
        onTimeClick = {},
        selectedDays = listOf(1, 3, 5),
        onDayToggle = {}
    )
}

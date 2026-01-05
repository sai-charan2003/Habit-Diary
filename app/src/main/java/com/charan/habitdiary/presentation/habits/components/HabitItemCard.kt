package com.charan.habitdiary.presentation.habits.components


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HabitItemCard(
    title: String,
    description: String,
    time: String = "",
    reminder: String = "",
    isCompleted: Boolean,
    onCompletedChange: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 12.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (description.isNotBlank()) {
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                RoundCheckbox(
                    checked = isCompleted,
                    onCheckedChange = onCompletedChange
                )
            }

            if (time.isNotBlank() || reminder.isNotBlank()) {
                HorizontalDivider(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                )
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {

                    if (time.isNotBlank()) {
                        HabitInfoItem(
                            icon = Icons.Rounded.Schedule,
                            text = time
                        )
                    }

                    if (reminder.isNotBlank()) {
                        HabitInfoItem(
                            icon = Icons.Rounded.Notifications,
                            text = reminder
                        )
                    }


                }
            }
        }
    }
}

@Composable
private fun HabitInfoItem(
    icon: ImageVector,
    text: String
) {
    Row(
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .size(18.dp)
                .padding(end = 4.dp)
        )

        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun RoundCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    strokeColor : Color = MaterialTheme.colorScheme.outline,
    fillColor: Color = MaterialTheme.colorScheme.primary
) {


    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .clickable { onCheckedChange(!checked) }
            .padding(4.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {

            drawCircle(
                color = if (checked) fillColor.copy(alpha = 0.35f) else strokeColor,
                style = if (!checked) {
                    Stroke(width = 2.dp.toPx())
                } else {
                    Fill
                }
            )

            if (checked) {
                drawCircle(
                    color = fillColor,
                    radius = size.minDimension * 0.32f
                )
            }
        }
    }
}

@Preview
@Composable
fun HabitItemCardPreview() {
    HabitItemCard(
        title = "Morning Jog",
        description = "Jog for 30 minutes",
        time = "6:00 AM",
        reminder = "5:45 AM",
        isCompleted = false,
        onCompletedChange = {},
        onClick = {}
    )
}



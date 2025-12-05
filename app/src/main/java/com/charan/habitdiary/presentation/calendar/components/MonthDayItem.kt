package com.charan.habitdiary.presentation.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.DayOfWeek

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MonthDayItem(
    date: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    hasContent: Boolean = false,
    isToday: Boolean,
    isCurrentMonth : Boolean = true
) {

    val circleModifier = Modifier
        .padding(3.dp)
        .size(40.dp)
        .clip(CircleShape)
        .clickable(onClick = onClick)

    val modifier =
        when {
            isSelected -> {
                circleModifier
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f))
            }
            isToday -> {
                circleModifier
                    .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    shape = CircleShape
                )
            }
            else -> {
                circleModifier
            }
        }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = date,
                style = if(isCurrentMonth) {
                    MaterialTheme.typography.titleSmallEmphasized
                } else {
                    MaterialTheme.typography.titleSmallEmphasized.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    )
                },
            )

            if (hasContent) {
                Spacer(modifier = Modifier.height(3.dp))
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.primary
                        )
                )
            }
        }
    }
}





@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MonthHeaderView(
    dayOfWeek : List<DayOfWeek>
) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
        for (dayOfWeek in dayOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                style = MaterialTheme.typography.labelSmallEmphasized,
                text = dayOfWeek.name.take(3),
            )
        }
    }

}

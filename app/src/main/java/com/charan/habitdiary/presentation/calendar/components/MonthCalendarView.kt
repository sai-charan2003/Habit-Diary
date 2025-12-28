package com.charan.habitdiary.presentation.calendar.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.yearMonth

@Composable
fun MonthCalendarView(
    state : CalendarState,
    currentDate : LocalDate,
    selectedDate : LocalDate,
    onClick :(LocalDate) -> Unit,
    visibleMonth : Month,
    datesWithLogs : List<LocalDate>
) {
    LaunchedEffect(Unit) {
        state.animateScrollToMonth(selectedDate.yearMonth)
    }
    HorizontalCalendar(
        state = state,
        dayContent = {
            CalendarDayItem(
                date = it.date.day.toString(),
                isSelected = selectedDate == it.date,
                isToday = currentDate == it.date,
                onClick = {
                    onClick(it.date)
                },
                isCurrentMonth = it.date.month == visibleMonth,
                hasContent = datesWithLogs.contains(it.date)
            )
        },
        monthHeader ={
            CalendarHeaderItem(it.weekDays.first().map { it.date.dayOfWeek })
        }
    )
}

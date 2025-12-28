package com.charan.habitdiary.presentation.calendar.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

@Composable
fun CustomWeekCalendar(
    calendarState : WeekCalendarState,
    selectedDate : LocalDate,
    onClick : (LocalDate) -> Unit,
    currentDate : LocalDate,
    visibleMonth : Month,
    datesWithLogs : List<LocalDate>
) {
    LaunchedEffect(Unit) {
        calendarState.animateScrollToWeek(selectedDate)
    }
    WeekCalendar(
        state = calendarState,
        weekHeader = {
            CalendarHeaderItem(it.days.map { it.date.dayOfWeek })
        },
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
        }
    )
}
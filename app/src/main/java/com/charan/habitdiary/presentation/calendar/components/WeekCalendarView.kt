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
    visibleMonth : Month
) {
    LaunchedEffect(Unit) {
        calendarState.animateScrollToWeek(selectedDate)
    }
    WeekCalendar(
        state = calendarState,
        weekHeader = {
            MonthHeaderView(it.days.map { it.date.dayOfWeek })
        },
        dayContent = {
            MonthDayItem(
                date = it.date.day.toString(),
                isSelected = selectedDate == it.date,
                isToday = currentDate == it.date,
                onClick = {
                    onClick(it.date)
                },
                isCurrentMonth = it.date.month == visibleMonth
            )
        }
    )
}
package com.charan.habitdiary.presentation.calendar.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import kotlinx.datetime.LocalDate

@Composable
fun CustomWeekCalendar(
    calendarState : WeekCalendarState,
    selectedDate : LocalDate,
    onClick : (LocalDate) -> Unit,
    currentDate : LocalDate
) {
    LaunchedEffect(Unit) {
        calendarState.animateScrollToWeek(selectedDate)
    }
    WeekCalendar(
        state = calendarState,
        dayContent = {
            WeekDayItem(
                date = it.date.day.toString(),
                day = it.date.dayOfWeek.name.take(3),
                isSelected = selectedDate == it.date,
                isToday = currentDate == it.date,
                onClick = {
                    onClick(it.date)
                }
            )
        }
    )
}
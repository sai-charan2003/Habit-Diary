package com.charan.habitdiary.presentation.calendar

sealed class LogCalendarEffect {

    data object ScrollToCurrentDate : LogCalendarEffect()

    data object ScrollToSelectedDate : LogCalendarEffect()

    data class OnNavigateToAddDailyLogScreen(val id : Int) : LogCalendarEffect()
}
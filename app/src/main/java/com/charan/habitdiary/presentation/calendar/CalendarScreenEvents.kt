package com.charan.habitdiary.presentation.calendar

import kotlinx.datetime.LocalDate

sealed class CalendarScreenEvents {

    data class OnDateSelected(val date : LocalDate) : CalendarScreenEvents()

    data class OnCalendarViewTypeChange(val viewType : CalendarViewType) : CalendarScreenEvents()

    data object OnScrollToCurrentDate : CalendarScreenEvents()

    data class OnNavigateToAddDailyLogScreen(val id : Int) : CalendarScreenEvents()

}
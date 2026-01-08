package com.charan.habitdiary.presentation.diary

import kotlinx.datetime.LocalDate

sealed class DiaryScreenEvents {

    data class OnDateSelected(val date : LocalDate) : DiaryScreenEvents()

    data class OnDiaryViewTypeChange(val viewType : CalendarViewType) : DiaryScreenEvents()

    data object OnScrollToCurrentDate : DiaryScreenEvents()

    data class OnNavigateToAddDailyLogScreen(val id : Long?) : DiaryScreenEvents()

    data class OnVisibleDateRangeChange(val startDate: LocalDate, val endDate: LocalDate) : DiaryScreenEvents()

}
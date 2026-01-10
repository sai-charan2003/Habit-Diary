package com.charan.habitdiary.presentation.diary

sealed class DiaryScreenEffect {

    data object ScrollToCurrentDate : DiaryScreenEffect()

    data object ScrollToSelectedDate : DiaryScreenEffect()

    data class OnNavigateToAddDailyLogScreen(val id : Long?) : DiaryScreenEffect()
}
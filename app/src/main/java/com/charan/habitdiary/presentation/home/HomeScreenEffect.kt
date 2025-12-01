package com.charan.habitdiary.presentation.home

sealed class HomeScreenEffect {
    data class OnNavigateToAddHabitScreen(val id : Int?) : HomeScreenEffect()

    data class OnNavigateToAddDailyLogScreen(val id : Int?) : HomeScreenEffect()
}
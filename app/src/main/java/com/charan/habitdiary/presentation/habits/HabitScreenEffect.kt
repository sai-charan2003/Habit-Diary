package com.charan.habitdiary.presentation.habits

sealed class HabitScreenEffect {
    data class OnNavigateToAddHabitScreen(val id : Int?) : HabitScreenEffect()

    data class OnNavigateToAddDailyLogScreen(val id : Int?) : HabitScreenEffect()
}
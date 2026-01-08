package com.charan.habitdiary.presentation.habits

sealed class HabitScreenEffect {
    data class OnNavigateToAddHabitScreen(val id : Long?) : HabitScreenEffect()

    data class OnNavigateToAddDailyLogScreen(val id : Long?) : HabitScreenEffect()

    data class OnNavigateToHabitStatsScreen(val habitId : Long) : HabitScreenEffect()
}
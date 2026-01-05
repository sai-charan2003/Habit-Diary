package com.charan.habitdiary.presentation.habit_stats

sealed class HabitStatEffect {
    data object OnNavigateBack : HabitStatEffect()

    data object AnimateToNextMonth : HabitStatEffect()

    data object AnimateToPreviousMonth : HabitStatEffect()

    data class OnNavigateToAddLogScreen(val logId : Int) : HabitStatEffect()

    data class OnNavigateToEditHabitScreen(val habitId : Int) : HabitStatEffect()


}
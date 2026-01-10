package com.charan.habitdiary.presentation.habit_stats

sealed class HabitStatEffect {
    data object OnNavigateBack : HabitStatEffect()

    data object AnimateToNextMonth : HabitStatEffect()

    data object AnimateToPreviousMonth : HabitStatEffect()

    data class OnNavigateToAddLogScreen(val logId : Long) : HabitStatEffect()

    data class OnNavigateToEditHabitScreen(val habitId : Long) : HabitStatEffect()


}
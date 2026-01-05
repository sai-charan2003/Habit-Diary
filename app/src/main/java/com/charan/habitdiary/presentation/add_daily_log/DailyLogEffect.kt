package com.charan.habitdiary.presentation.add_daily_log


sealed class DailyLogEffect {

    data object OnNavigateBack : DailyLogEffect()

    data object OnOpenMediaPicker : DailyLogEffect()

    data object OnTakePhoto : DailyLogEffect()

    data object OnRequestCameraPermission : DailyLogEffect()

    data object OnTakeVideo : DailyLogEffect()

    data class OnNavigateToHabitScreen(val habitId : Int) : DailyLogEffect()
}
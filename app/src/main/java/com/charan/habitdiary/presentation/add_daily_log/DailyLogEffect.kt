package com.charan.habitdiary.presentation.add_daily_log


sealed class DailyLogEffect {

    data object OnNavigateBack : DailyLogEffect()

    data object OnOpenImagePicker : DailyLogEffect()

    data object OnTakePhoto : DailyLogEffect()

    data object OnRequestCameraPermission : DailyLogEffect()
}
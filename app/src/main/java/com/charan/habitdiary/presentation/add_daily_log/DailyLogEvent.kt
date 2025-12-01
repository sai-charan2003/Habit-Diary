package com.charan.habitdiary.presentation.add_daily_log

import android.net.Uri

sealed class DailyLogEvent {

    data class InitializeLog(val habitId : Int?) : DailyLogEvent()
    data class OnNotesTextChange(val text : String) : DailyLogEvent()
    data class OnImagePathChange(val path : String) : DailyLogEvent()
    data object OnSaveDailyLogClick : DailyLogEvent()
    data object OnBackClick : DailyLogEvent()

    data class OnToggleImagePickOptionsSheet(val isVisible : Boolean) : DailyLogEvent()

    data object OnPickFromGalleryClick : DailyLogEvent()

    data object OnTakePhotoClick : DailyLogEvent()

    data class OnImagePick(val uri : Uri) : DailyLogEvent()

    data object OnOpenSettingsForPermissions : DailyLogEvent()

    data class ToggleShowRationaleForCameraPermission(val showRationale : Boolean) : DailyLogEvent()

    data class OnToggleDateSelectorDialog(val isVisible : Boolean) : DailyLogEvent()

    data class OnDateSelected(val timeMillis : Long) : DailyLogEvent()

    data class OnToggleTimeSelectorDialog(val isVisible : Boolean) : DailyLogEvent()

    data class OnTimeSelected(val timeMillis : Long) : DailyLogEvent()

    data class OnToggleDeleteDialog(val showDeleteDialog : Boolean) : DailyLogEvent()

    data object OnDeleteDailyLog : DailyLogEvent()

}
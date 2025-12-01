package com.charan.habitdiary.presentation.add_daily_log

data class DailyLogState(
    val showImagePickOptionsSheet : Boolean = false,
    val tempImagePath : String = "",
    val showRationaleForCameraPermission : Boolean = false,
    val showDateSelectDialog : Boolean = false,
    val showTimeSelectDialog : Boolean = false,
    val isEdit : Boolean = false,
    val showDeleteDialog : Boolean = false,
    val is24HourFormat : Boolean = false,
    val dailyLogItemDetails : DailyLogItemDetails = DailyLogItemDetails()
)

data class DailyLogItemDetails(
    val id: Int? = null,
    val notesText: String = "",
    val imagePath: String = "",
    val formattedDateString: String = "",
    val formattedTimeString: String = "",
    val timeMillis: Long = 0L,
    val dateMillis: Long = 0L,
    val habitId: Int? = null,
    val habitName: String? = null,
    val habitDescription: String? = null
)



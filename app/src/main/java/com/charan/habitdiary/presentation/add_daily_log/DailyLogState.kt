package com.charan.habitdiary.presentation.add_daily_log

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

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
    val time: LocalTime = LocalTime(0,0),
    val date: LocalDate = LocalDate(1970,1,1),
    val habitId: Int? = null,
    val habitName: String? = null,
    val habitDescription: String? = null
)



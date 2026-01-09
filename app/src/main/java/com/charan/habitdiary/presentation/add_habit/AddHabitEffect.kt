package com.charan.habitdiary.presentation.add_habit

sealed interface AddHabitEffect {

    data class OnNavigateBack(val isHabitDeleted : Boolean = false) : AddHabitEffect

    data object RequestNotificationPermission : AddHabitEffect

}
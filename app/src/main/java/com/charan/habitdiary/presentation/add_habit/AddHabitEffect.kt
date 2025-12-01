package com.charan.habitdiary.presentation.add_habit

sealed interface AddHabitEffect {

    data object OnNavigateBack : AddHabitEffect

    data object RequestNotificationPermission : AddHabitEffect

}
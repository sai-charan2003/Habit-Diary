package com.charan.habitdiary.presentation.habit_stats

import kotlinx.datetime.LocalDate

sealed class HabitStatEvent {

    data class OnDateSelected(val date : LocalDate) : HabitStatEvent()

    data object OnNextMonthClick: HabitStatEvent()

    data object OnPreviousMonthClick : HabitStatEvent()

    data class OnCompleteTaskClick(val date : LocalDate) : HabitStatEvent()

    data object OnAddLog : HabitStatEvent()

    data object OnNavigateBackClick : HabitStatEvent()

    data object OnEditHabitClick : HabitStatEvent()



}
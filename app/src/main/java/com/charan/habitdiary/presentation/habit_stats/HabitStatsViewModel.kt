package com.charan.habitdiary.presentation.habit_stats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charan.habitdiary.data.local.entity.HabitEntity
import com.charan.habitdiary.data.mapper.toDailyLogEntity
import com.charan.habitdiary.data.repository.HabitLocalRepository
import com.charan.habitdiary.utils.DateUtil
import com.charan.habitdiary.utils.getBestHabitStreak
import com.charan.habitdiary.utils.getHabitStreak
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atTime
import javax.inject.Inject
@HiltViewModel(assistedFactory = HabitStatsViewModel.Factory::class)
class HabitStatsViewModel @AssistedInject constructor(
    @Assisted val habitId : Int,
    private val habitLocalRepository: HabitLocalRepository

) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(habitId : Int): HabitStatsViewModel
    }

    private val _state = MutableStateFlow(HabitStatState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HabitStatEffect>()
    val effect  = _effect.asSharedFlow()
    init {
        fetchHabitStats()
    }

    fun onEvent(event: HabitStatEvent) {

        when(event){
            is HabitStatEvent.OnDateSelected -> {
                handleSelectedDate(event.date)
            }
            HabitStatEvent.OnNextMonthClick -> {
                sendEffect(HabitStatEffect.AnimateToNextMonth)
            }
            HabitStatEvent.OnPreviousMonthClick -> {
                sendEffect(HabitStatEffect.AnimateToPreviousMonth)
            }

            is HabitStatEvent.OnCompleteTaskClick -> {
                handleCompleteTask(event.date)
            }

            HabitStatEvent.OnAddLog -> {
                handleAddLog()
            }

            HabitStatEvent.OnNavigateBackClick -> {
                sendEffect(HabitStatEffect.OnNavigateBack)
            }

            HabitStatEvent.OnEditHabitClick -> {
                sendEffect(HabitStatEffect.OnNavigateToEditHabitScreen(_state.value.habitId))
            }
        }

    }

    private fun handleAddLog() = viewModelScope.launch(Dispatchers.IO) {
        val logId = habitLocalRepository.getLoggedHabitFromIdForRange(
            habitId = _state.value.habitId,
            startOfDay = _state.value.selectedDate.atTime(LocalTime(0,0)),
            endOfDay = _state.value.selectedDate.atTime(LocalTime(23,59))
        )
        logId?.let {
            sendEffect(HabitStatEffect.OnNavigateToAddLogScreen(it.id))
        }
    }

    private fun handleCompleteTask(date : LocalDate) = viewModelScope.launch(Dispatchers.IO) {
        val habitLogExists = _state.value.datesWithHabitDone.contains(date)
        if (!habitLogExists){
            val habit = habitLocalRepository.getHabitWithId(_state.value.habitId)
            val createdTime = date.atTime(DateUtil.getCurrentTime())
            habitLocalRepository.upsetDailyLog(
                habit.toDailyLogEntity(date = createdTime)
            )
        } else{
            val existingLog = habitLocalRepository.getLoggedHabitFromIdForRange(
                habitId = _state.value.habitId,
                startOfDay = date.atTime(LocalTime(0,0)),
                endOfDay = date.atTime(LocalTime(23,59))
            )
            existingLog?.let {
                habitLocalRepository.deleteDailyLog(it.id)
            }
        }
    }

    private fun handleSelectedDate(date : LocalDate){
        _state.update {
            it.copy(
                selectedDate = date
            )
        }

    }

    private fun fetchHabitStats() = viewModelScope.launch(Dispatchers.IO) {
        val habit = habitLocalRepository.getHabitWithId(id = habitId)
        _state.update { state->
            state.copy(
                habitId = habitId,
                habitName = habit.habitName,
                habitFrequency = habit.habitFrequency,
                habitDescription = habit.habitDescription,
                habitTime = habit.habitTime
            )
        }
        observeLogs(habit)
    }

    private fun observeLogs(habit : HabitEntity) = viewModelScope.launch (Dispatchers.IO){
        habitLocalRepository.getAllLogsWithHabitId(habitId).collectLatest {
            _state.update { state->
                state.copy(
                    currentStreak = it.getHabitStreak(habit.habitFrequency),
                    bestStreak = it.getBestHabitStreak(habit.habitFrequency),
                    datesWithHabitDone = it.map { log-> log.createdAt.date }.toSet(),
                )
            }

        }
    }

    private fun sendEffect(effect : HabitStatEffect) = viewModelScope.launch {
        _effect.emit(effect)
    }

}
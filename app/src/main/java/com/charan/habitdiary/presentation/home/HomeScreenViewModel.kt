package com.charan.habitdiary.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charan.habitdiary.data.repository.DataStoreRepository
import com.charan.habitdiary.data.repository.HabitLocalRepository
import com.charan.habitdiary.presentation.home.HomeScreenEffect.*
import com.charan.habitdiary.presentation.mapper.toDailyLogEntity
import com.charan.habitdiary.presentation.mapper.toDailyLogUIStateList
import com.charan.habitdiary.presentation.mapper.toHabitUIState
import com.charan.habitdiary.utils.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val habitLocalRepository: HabitLocalRepository,
    private val dataStoreRepo : DataStoreRepository

): ViewModel() {
    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HomeScreenEffect>()
    val effect  = _effect.asSharedFlow()
    init {
        getHabits()
        getDailyLogs()
        _state.update {
            it.copy(
                todayDate = DateUtil.getTodayDayAndDate()
            )
        }

    }

    fun onEvent(event : HomeScreenEvent){
        when(event){
            is HomeScreenEvent.OnFabExpandToggle -> {
                _state.value = state.value.copy(
                    isFabExpanded = !state.value.isFabExpanded
                )
            }

            HomeScreenEvent.OnAddDailyLogClick -> {
                sendEffect(OnNavigateToAddDailyLogScreen(null))

            }

            HomeScreenEvent.OnAddHabitClick ->{
                sendEffect(OnNavigateToAddHabitScreen(null))

            }

            is HomeScreenEvent.OnHabitCheckToggle -> {
                onAddHabitClick(event.habit,event.isChecked)

            }

            is HomeScreenEvent.OnDailyLogEdit -> {
                sendEffect(OnNavigateToAddDailyLogScreen(event.id))
            }

            is HomeScreenEvent.OnEditHabit -> {
                sendEffect(OnNavigateToAddHabitScreen(event.id))
            }
        }
    }

    private fun getHabits() = viewModelScope.launch(Dispatchers.IO){
        habitLocalRepository.getTodayHabits().collectLatest { habits->
            _state.update {
                it.copy(
                    habits = habits.toHabitUIState()
                )
            }

        }
    }

    private fun getDailyLogs() = viewModelScope.launch(Dispatchers.IO) {
        combine(
            habitLocalRepository.getDailyLogsInRange(),
            dataStoreRepo.getIs24HourFormat
        ) { logs, is24Hours ->
            logs.toDailyLogUIStateList(is24Hours)
        }.collectLatest { dailyLogs ->
            _state.update { it.copy(dailyLogs = dailyLogs) }
        }
    }


    private fun sendEffect(effect : HomeScreenEffect) = viewModelScope.launch {
        _effect.emit(effect)
    }

    private fun onAddHabitClick(habitUI : HabitItemUIState,isChecked : Boolean) = viewModelScope.launch(Dispatchers.IO) {
        if (isChecked) {
            habitLocalRepository.upsetDailyLog(habitUI.toDailyLogEntity(DateUtil.getCurrentDateTime()))
        } else {
            habitLocalRepository.deleteDailyLog(habitUI.logId ?: return@launch)
        }
    }


}

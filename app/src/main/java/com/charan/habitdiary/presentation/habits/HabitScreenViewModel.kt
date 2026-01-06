package com.charan.habitdiary.presentation.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charan.habitdiary.data.local.entity.HabitEntity
import com.charan.habitdiary.data.local.model.HabitWithDone
import com.charan.habitdiary.data.model.enums.HabitSortType
import com.charan.habitdiary.data.repository.DataStoreRepository
import com.charan.habitdiary.data.repository.HabitLocalRepository
import com.charan.habitdiary.presentation.habits.HabitScreenEffect.*
import com.charan.habitdiary.presentation.mapper.toDailyLogEntity
import com.charan.habitdiary.presentation.mapper.toDailyLogUIStateList
import com.charan.habitdiary.presentation.mapper.toHabitUIState
import com.charan.habitdiary.utils.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitScreenViewModel @Inject constructor(
    private val habitLocalRepository: HabitLocalRepository,
    private val dataStoreRepo : DataStoreRepository

): ViewModel() {
    private val _state = MutableStateFlow(HabitScreenState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HabitScreenEffect>()
    val effect  = _effect.asSharedFlow()
    init {
        getHabits()
        getDailyLogs()
        _state.update {
            it.copy(
                todayDate = DateUtil.getTodayDayAndDate()
            )
        }
        observeIs24HourFormat()
        observeHabitSortType()

    }

    /**
     * Handles a UI event from the habit screen by updating state, emitting one-time effects, or delegating the corresponding action.
     *
     * @param event The event to handle (e.g., toggle FAB, navigate to add/edit screens, toggle a habit check, open habit stats, change sort type, or toggle the sort dropdown).
     */
    fun onEvent(event : HabitScreenEvent){
        when(event){
            is HabitScreenEvent.OnFabExpandToggle -> {
                _state.value = state.value.copy(
                    isFabExpanded = !state.value.isFabExpanded
                )
            }

            HabitScreenEvent.OnAddDailyLogClick -> {
                sendEffect(OnNavigateToAddDailyLogScreen(null))

            }

            HabitScreenEvent.OnAddHabitClick ->{
                sendEffect(OnNavigateToAddHabitScreen(null))

            }

            is HabitScreenEvent.OnHabitCheckToggle -> {
                onAddHabitClick(event.habit,event.isChecked)

            }

            is HabitScreenEvent.OnDailyLogEdit -> {
                sendEffect(OnNavigateToAddDailyLogScreen(event.id))
            }

            is HabitScreenEvent.OnHabitStatsScreen -> {
                sendEffect(OnNavigateToHabitStatsScreen(event.id))
            }

            is HabitScreenEvent.OnSortTypeChange -> {
                handleSortTypeChange(event.sortType)
            }

            HabitScreenEvent.OnSortDropDownToggle -> {
                toggleSortDropDown()
            }
        }
    }

    /**
     * Toggles the sort dropdown expanded state in the ViewModel's UI state.
     */
    private fun toggleSortDropDown() {
        _state.update {
            it.copy(
                isSortDropDownExpanded  = !it.isSortDropDownExpanded
            )
        }
    }

    /**
     * Persists the selected habit sort type and toggles the sort dropdown visibility.
     *
     * @param sortType The habit sort type to store.
     */
    private fun handleSortTypeChange(sortType : HabitSortType) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepo.setHabitSortType(sortType)
        toggleSortDropDown()

    }
    /**
     * Provide a flow of habits matching the specified sort type.
     *
     * @param sortType The selection determining which habits to observe:
     *  - `ALL_HABITS`: active habits.
     *  - `TODAY_HABITS`: habits for today.
     * @return A Flow that emits lists of `HabitWithDone` corresponding to the chosen sort type.
     */
    fun observeHabits(sortType: HabitSortType): Flow<List<HabitWithDone>> {
        return when (sortType) {
            HabitSortType.ALL_HABITS -> {
                habitLocalRepository.getActiveHabits()
            }
            HabitSortType.TODAY_HABITS -> {
                habitLocalRepository.getTodayHabits()
            }
        }
    }


    /**
     * Observes the current habit list (driven by the selected habit sort type) together with the 24-hour format setting and updates the UI state's `habits` accordingly.
     *
     * Collects habit data, maps it to `HabitUIState` using the active 24-hour format, and writes the resulting value into `state.habits`.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getHabits() = viewModelScope.launch(Dispatchers.IO) {
        combine(
            _state.map { it.habitSortType }.flatMapLatest {
                observeHabits(it)
            },
            _state.map { it.is24HourFormat }.distinctUntilChanged()
        ) { habits, is24Hours ->
            habits.toHabitUIState(is24Hours)
        }.collectLatest { habitsUIState ->
            _state.update {
                it.copy(habits = habitsUIState)
            }
        }
    }



    /**
     * Begins a coroutine that observes daily logs together with the 24-hour format setting, converts them to UI models, and updates the state's `dailyLogs` list.
     *
     * @return The started coroutine's Job.
     */
    private fun getDailyLogs() = viewModelScope.launch(Dispatchers.IO) {
        combine(
            habitLocalRepository.getDailyLogsInRange(),
            _state.map { it.is24HourFormat }.distinctUntilChanged()
        ) { logs, is24Hours ->
            logs.toDailyLogUIStateList(is24Hours)
        }.collectLatest { dailyLogs ->
            _state.update { it.copy(dailyLogs = dailyLogs) }
        }
    }


    private fun sendEffect(effect : HabitScreenEffect) = viewModelScope.launch {
        _effect.emit(effect)
    }

    /**
     * Adds or removes a daily log for the given habit based on the checked state.
     *
     * When `isChecked` is true, upserts a daily log for the habit with the current date-time.
     * When `isChecked` is false, deletes the associated daily log if `habitUI.logId` is present.
     *
     * @param habitUI UI state of the habit to add or remove the daily log for.
     * @param isChecked `true` to create or update the daily log, `false` to delete it.
     */
    private fun onAddHabitClick(habitUI : HabitItemUIState,isChecked : Boolean) = viewModelScope.launch(Dispatchers.IO) {
        if (isChecked) {
            habitLocalRepository.upsetDailyLog(habitUI.toDailyLogEntity(DateUtil.getCurrentDateTime()))
        } else {
            habitLocalRepository.deleteDailyLog(habitUI.logId ?: return@launch)
        }
    }

    /**
     * Observes the persisted habit sort type and updates the view model state with any changes.
     *
     * Collects values from dataStoreRepo.habitSortType and writes the latest sort type into
     * the state's `habitSortType` property so the UI can react to sort preference changes.
     *
     * @return The [Job] representing the launched observer coroutine.
     */
    private fun observeHabitSortType() = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepo.habitSortType.collectLatest { sortType ->
            _state.update {
                it.copy(
                    habitSortType = sortType
                )
            }
        }
    }
    /**
     * Observes the persisted 24-hour time format setting and updates the UI state's `is24HourFormat`.
     *
     * @return The Job for the launched coroutine.
     */
    private fun observeIs24HourFormat() = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepo.getIs24HourFormat.collectLatest { is24HourFormat ->
            _state.update {
                it.copy(
                    is24HourFormat = is24HourFormat
                )
            }
        }
    }


}
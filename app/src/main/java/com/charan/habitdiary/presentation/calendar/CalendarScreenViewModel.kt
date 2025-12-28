package com.charan.habitdiary.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charan.habitdiary.data.repository.DataStoreRepository
import com.charan.habitdiary.data.repository.HabitLocalRepository
import com.charan.habitdiary.presentation.calendar.LogCalendarEffect.*
import com.charan.habitdiary.presentation.mapper.toDailyLogUIStateList
import com.charan.habitdiary.utils.DateUtil.getEndOfDay
import com.charan.habitdiary.utils.DateUtil.getStartOfDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import kotlinx.datetime.LocalDate
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@HiltViewModel
class CalendarScreenViewModel @Inject constructor(
    private val habitLocalRepository: HabitLocalRepository,
    private val dataStoreRepo: DataStoreRepository
) : ViewModel() {
    private val _state = MutableStateFlow(LogCalendarState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LogCalendarEffect>()
    val effect = _effect.asSharedFlow()
    init {
        fetchDailyLogsForDate()
        getLoggedDatesInRange()
    }


    fun onEvent(event: CalendarScreenEvents) {
        when (event) {
            is CalendarScreenEvents.OnDateSelected -> {
                selectDateChange(event.date)
            }

            is CalendarScreenEvents.OnCalendarViewTypeChange -> {
                calendarViewChange(event.viewType)
                sendEffect(LogCalendarEffect.ScrollToSelectedDate)
            }

            CalendarScreenEvents.OnScrollToCurrentDate -> {
                scrollToCurrentDate()
            }

            is CalendarScreenEvents.OnNavigateToAddDailyLogScreen -> {
                sendEffect(OnNavigateToAddDailyLogScreen(event.id))
            }

            is CalendarScreenEvents.OnVisibleDateRangeChange -> {
                handleDateRangeChange(event.startDate, event.endDate)


            }
        }
    }

    private fun handleDateRangeChange(startDate: LocalDate, endDate: LocalDate) {
        _state.update {
            it.copy(
                visibleStartOfDate = startDate,
                visibleEndOfDate = endDate
            )
        }
    }


    fun selectDateChange(date : LocalDate){
        _state.update {
            it.copy(
                selectedDate = date
            )
        }
    }

    private fun scrollToCurrentDate(){
        _state.update {
            it.copy(
                selectedDate = it.currentDate
            )
        }
        sendEffect(LogCalendarEffect.ScrollToCurrentDate)
    }

    fun calendarViewChange(viewType : CalendarViewType) = viewModelScope.launch {
        _state.update {
            it.copy(
                selectedCalendarView = viewType
            )
        }

    }

    private fun sendEffect(effect : LogCalendarEffect) = viewModelScope.launch{
            _effect.emit(effect)
    }

    @OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)
    private fun fetchDailyLogsForDate() = viewModelScope.launch(Dispatchers.IO) {

        _state
            .map { it.selectedDate }
            .distinctUntilChanged()
            .flatMapLatest { date ->
                _state.update { it.copy(dailyLogItem = emptyList()) }
                val start = date.getStartOfDay()
                val end = date.getEndOfDay()
                val logsFlow = habitLocalRepository.getDailyLogsInRange(start, end)
                combine(
                    logsFlow,
                    dataStoreRepo.getIs24HourFormat
                ) { logs, is24Hours ->
                    logs.toDailyLogUIStateList(is24Hours)
                }
            }
            .collectLatest { uiList ->
                _state.update { it.copy(dailyLogItem = uiList) }
            }
    }

    private fun getLoggedDatesInRange() {
        viewModelScope.launch(Dispatchers.IO) {
            _state
                .map { it.visibleStartOfDate to it.visibleEndOfDate }
                .distinctUntilChanged()
                .flatMapLatest { range ->
                    habitLocalRepository.getLoggedDatesInRange(range.first.getStartOfDay(), range.second.getEndOfDay())
                }
                .collectLatest { dates ->
                    _state.update { it.copy(datesWithLogs = dates.toSet()) }
                }
        }
    }


}
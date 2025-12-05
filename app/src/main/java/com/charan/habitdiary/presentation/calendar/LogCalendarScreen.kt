package com.charan.habitdiary.presentation.calendar

import DayLogEntryItem
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CalendarViewWeek
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charan.habitdiary.presentation.calendar.components.CustomWeekCalendar
import com.charan.habitdiary.presentation.calendar.components.MonthCalendarView
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import kotlinx.coroutines.flow.collectLatest
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun LogCalendarScreen(
    onNavigateToDailyLogScreen : (id : Int) -> Unit
){
    val viewModel = hiltViewModel<CalendarScreenViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val weekCalendarState = rememberWeekCalendarState(
        startDate = state.startOfDate,
        endDate = state.endOfDate,
        firstDayOfWeek = firstDayOfWeekFromLocale(),
    )
    val monthCalendarState = rememberCalendarState(
        startMonth = state.startOfMonth,
        endMonth = state.endOfMonth,
        firstVisibleMonth = state.currentMonth,
        firstDayOfWeek = firstDayOfWeekFromLocale(),
    )
    val currentMonthTitle by remember(
        state.selectedCalendarView,
        weekCalendarState,
        monthCalendarState
    ) {
        derivedStateOf {
            when (state.selectedCalendarView) {
                CalendarViewType.WEEK -> {
                    weekCalendarState.lastVisibleWeek.days.last().date.month
                }
                CalendarViewType.MONTH -> {
                    monthCalendarState.lastVisibleMonth.yearMonth.month
                }
            }
        }
    }


    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when(effect){
                LogCalendarEffect.ScrollToCurrentDate -> {
                    when(state.selectedCalendarView){
                        CalendarViewType.WEEK -> {

                            weekCalendarState.animateScrollToWeek(state.currentDate)
                        }
                        CalendarViewType.MONTH -> {
                            monthCalendarState.animateScrollToMonth(state.currentMonth)
                        }
                    }
                }
                is LogCalendarEffect.OnNavigateToAddDailyLogScreen -> {
                    onNavigateToDailyLogScreen(effect.id)
                }
                else -> {}
            }
        }
    }
    Scaffold(
        topBar = {
            MediumFlexibleTopAppBar(
                title = {
                    Text(currentMonthTitle.name)
                },
                actions = {
                    ResetCalendarButton {
                        viewModel.onEvent(CalendarScreenEvents.OnScrollToCurrentDate)
                    }
                    CalendarViewToggleButton(
                        selectedView = state.selectedCalendarView,
                        onToggle = {
                            viewModel.onEvent(CalendarScreenEvents.OnCalendarViewTypeChange(
                                if(state.selectedCalendarView == CalendarViewType.WEEK)
                                    CalendarViewType.MONTH
                                else
                                    CalendarViewType.WEEK
                            )
                            )
                        }
                    )

                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            AnimatedVisibility(
                visible = state.selectedCalendarView == CalendarViewType.WEEK,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                CustomWeekCalendar(
                    calendarState = weekCalendarState,
                    onClick = { date ->
                        viewModel.onEvent(CalendarScreenEvents.OnDateSelected(date))
                    },
                    currentDate = state.currentDate,
                    selectedDate = state.selectedDate,
                    visibleMonth = weekCalendarState.lastVisibleWeek.days.last().date.month
                )
            }
            AnimatedVisibility(
                visible = state.selectedCalendarView == CalendarViewType.MONTH,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                MonthCalendarView(
                    state = monthCalendarState,
                    currentDate = state.currentDate,
                    selectedDate = state.selectedDate,
                    onClick = { date ->
                        viewModel.onEvent(CalendarScreenEvents.OnDateSelected(date))
                    },
                    visibleMonth = monthCalendarState.lastVisibleMonth.yearMonth.month
                )
            }


            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                items(state.dailyLogItem.size) { index ->
                    val log = state.dailyLogItem[index]
                    DayLogEntryItem(
                        note = log.logNote,
                        time = log.createdAt,
                        imagePath = log.imagePath,
                        onClick = {
                            viewModel.onEvent(
                                CalendarScreenEvents.OnNavigateToAddDailyLogScreen(
                                    log.id
                                )
                            )

                        },
                        habitName = log.habitName ?: ""
                    )
                }
            }
        }


        }

    }



@Composable
private fun CalendarViewToggleButton(
    selectedView: CalendarViewType,
    onToggle: () -> Unit
) {
    IconButton(onClick = onToggle) {
        Icon(
            imageVector = when (selectedView) {
                CalendarViewType.WEEK -> Icons.Rounded.CalendarViewWeek
                CalendarViewType.MONTH -> Icons.Rounded.CalendarMonth
            },
            contentDescription = "Change Calendar View"
        )
    }
}

@Composable
private fun ResetCalendarButton(
    onResetClick : () ->Unit
){
    IconButton(onClick = onResetClick) {
        Icon(
            imageVector = Icons.Rounded.Event,
            contentDescription = "Reset Calendar to Current Date"
        )
    }
}

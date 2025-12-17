package com.charan.habitdiary.presentation.home

import DayLogEntryItem
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charan.habitdiary.presentation.common.components.SectionHeading
import com.charan.habitdiary.presentation.home.components.EmptyStateItem
import com.charan.habitdiary.presentation.home.components.FabMenu
import com.charan.habitdiary.presentation.home.components.HabitItemCard
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreen(
    onAddHabitClick : (id : Int?) -> Unit,
    onAddDailyLog : (id : Int?) -> Unit

) {
    val viewModel = hiltViewModel<HomeScreenViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when(effect){

                is HomeScreenEffect.OnNavigateToAddHabitScreen -> {
                    onAddHabitClick(effect.id)
                }

                is HomeScreenEffect.OnNavigateToAddDailyLogScreen -> {
                    onAddDailyLog(effect.id)
                }
            }
        }
    }
    Scaffold(
        topBar = {
            MediumFlexibleTopAppBar(
                title = {
                    Text("Today")

                },
                subtitle = {
                    Text(state.todayDate)
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FabMenu(
                onAddHabitClick = {
                    viewModel.onEvent(HomeScreenEvent.OnAddHabitClick)
                },
                onFabClick = {
                    viewModel.onEvent(HomeScreenEvent.OnFabExpandToggle)

                },
                onAddDailyLogClick = {
                    viewModel.onEvent(HomeScreenEvent.OnAddDailyLogClick)
                },
                isExpanded = state.isFabExpanded

            )
        }
    ) { innerPadding->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            item {
                SectionHeading(
                    title = "Today's Habits",
                    modifier = Modifier.padding(vertical = 15.dp)
                )
                if (state.habits.isEmpty()) {
                    EmptyStateItem(
                        "No habits for today"
                    )
                }
            }
            items(state.habits.size){
                val habit = state.habits[it]
                HabitItemCard(
                 title = habit.habitName,
                    description = habit.habitDescription,
                    onCompletedChange = { checked->
                        viewModel.onEvent(
                            HomeScreenEvent.OnHabitCheckToggle(
                                habit = habit,
                                isChecked = checked
                            )
                        )
                    },
                    isCompleted = habit.isDone,
                    onClick = {
                        viewModel.onEvent(
                            HomeScreenEvent.OnEditHabit(
                                habit.id
                            )
                        )
                    },
                    time = habit.habitTime,
                    reminder = habit.habitReminderTime ?: ""

                )
            }
            item {
                SectionHeading(
                    title = "Daily Journals",
                    modifier = Modifier.padding(vertical = 15.dp)
                )

                if (state.dailyLogs.isEmpty()) {
                    EmptyStateItem(
                        "No daily logs yet"
                    )
                }
            }

            items(state.dailyLogs.size) {
                val dailyLog = state.dailyLogs[it]
                DayLogEntryItem(
                    note = dailyLog.logNote,
                    time = dailyLog.createdAt,
                    mediaPath = dailyLog.mediaPaths,
                    onClick = {
                        viewModel.onEvent(
                            HomeScreenEvent.OnDailyLogEdit(
                                dailyLog.id
                            )
                        )
                    },
                    habitName = dailyLog.habitName ?: ""
                )
            }


        }

    }
}

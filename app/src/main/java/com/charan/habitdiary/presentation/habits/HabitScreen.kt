package com.charan.habitdiary.presentation.habits

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charan.habitdiary.R
import com.charan.habitdiary.presentation.common.components.SectionHeading
import com.charan.habitdiary.presentation.habits.components.EmptyStateItem
import com.charan.habitdiary.presentation.habits.components.HabitItemCard
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalTextApi::class
)
@Composable
fun HabitScreen(
    onHabitDetails : (id : Int?) -> Unit,
    onAddDailyLog : (id : Int?) -> Unit,
    onHabitStats : (id : Int)-> Unit

    ) {
    val viewModel = hiltViewModel<HabitScreenViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when(effect){

                is HabitScreenEffect.OnNavigateToAddHabitScreen -> {
                    onHabitDetails(effect.id)
                }

                is HabitScreenEffect.OnNavigateToAddDailyLogScreen -> {
                    onAddDailyLog(effect.id)
                }

                is HabitScreenEffect.OnNavigateToHabitStatsScreen -> {
                    onHabitStats(effect.habitId)
                }
            }
        }
    }
    Scaffold(
        topBar = {
            MediumFlexibleTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.today),
                    )

                },
                subtitle = {
                    Text(state.todayDate)
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(HabitScreenEvent.OnAddHabitClick)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.add_habit)
                )
            }
        }
    ) { innerPadding->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            item {
                SectionHeading(
                    title = stringResource(R.string.today_habits),
                    modifier = Modifier.padding(vertical = 15.dp)
                )
            }

            if (state.habits.isEmpty()) {
                item {
                    EmptyStateItem(
                        text = stringResource(R.string.no_habits_for_today),
                        modifier = Modifier.fillParentMaxSize()
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
                            HabitScreenEvent.OnHabitCheckToggle(
                                habit = habit,
                                isChecked = checked
                            )
                        )
                    },
                    isCompleted = habit.isDone,
                    onClick = {
                        viewModel.onEvent(
                            HabitScreenEvent.OnHabitStatsScreen(
                                habit.id
                            )
                        )
                    },
                    time = habit.habitTime,
                    reminder = habit.habitReminderTime ?: ""

                    )
                }

        }

    }
}

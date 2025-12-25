package com.charan.habitdiary.presentation.settings

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charan.habitdiary.R
import com.charan.habitdiary.presentation.common.components.CustomListItem
import com.charan.habitdiary.presentation.settings.components.SectionHeader
import com.charan.habitdiary.presentation.settings.components.SettingsRowItem
import com.charan.habitdiary.presentation.settings.components.SettingsSwitchItem
import com.charan.habitdiary.presentation.settings.components.ThemeOptionButtonGroup
import com.charan.habitdiary.ui.theme.IndexItem
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsScreen(
    navigateToAboutLibraries : () -> Unit
) {
    val viewModel = hiltViewModel<SettingsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when(effect){
                SettingsScreenEffect.NavigateToLibrariesScreen -> {
                    navigateToAboutLibraries()
                }
                else ->{}
            }
        }
    }
    Scaffold(
        topBar = {
            MediumFlexibleTopAppBar(
                title = {
                    Text(stringResource(R.string.settings))
                },
                scrollBehavior = scrollBehavior
            )

        }
    ) {innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal =  16.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            item {
                SectionHeader(
                    title = stringResource(R.string.appearance),
                )
                CustomListItem(
                    indexItem = IndexItem.FIRST,
                    content = {
                        Text(stringResource(R.string.theme))
                        ThemeOptionButtonGroup(
                            selectedTheme = state.selectedThemeOption
                        ) {
                            viewModel.onEvent(SettingsScreenEvent.OnThemeChange(it))
                        }

                    }
                )
                SettingsSwitchItem(
                    title = stringResource(R.string.use_dynamic_color),
                    index = IndexItem.LAST,
                    isChecked = state.isDynamicColorsEnabled,
                    onCheckedChange = {
                        viewModel.onEvent(SettingsScreenEvent.OnDynamicColorsChange(it))
                    }
                )

            }

            item {
                SectionHeader(
                    stringResource(R.string.general)
                )

                SettingsSwitchItem(
                    title = stringResource(R.string.hour_format_24),
                    index = IndexItem.FIRST_AND_LAST,
                    isChecked = state.is24HourFormat,
                    onCheckedChange = {
                        viewModel.onEvent(SettingsScreenEvent.OnTimeFormatChange(it))
                    }
                )
            }

            item {
                SectionHeader(
                    stringResource(R.string.about)
                )
                CustomListItem(
                    indexItem = IndexItem.FIRST,
                    content = {
                        Text(stringResource(R.string.open_source_libraries))
                    },
                    onClick = {
                        viewModel.onEvent(SettingsScreenEvent.OnAboutLibrariesClick)
                    }
                )

                CustomListItem(
                    indexItem = IndexItem.LAST,
                    content = {
                        SettingsRowItem(
                            title = stringResource(R.string.app_version),
                            trailingContent = {
                                Text(state.appVersion)
                            }
                        )
                    }
                )
            }

        }
    }

}
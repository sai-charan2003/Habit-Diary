package com.charan.habitdiary.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charan.habitdiary.BuildConfig
import com.charan.habitdiary.data.model.enums.ThemeOption
import com.charan.habitdiary.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore : DataStoreRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SettingsScreenEffect>()
    val effect = _effect.asSharedFlow()

    init {
        observeTheme()
        observeDynamicColorsState()
        observeTimeFormat()
        getAppVersion()
    }


    fun onEvent(event: SettingsScreenEvent) {
        when(event){
            is SettingsScreenEvent.OnThemeChange -> {
                changeTheme(event.theme)
            }
            is SettingsScreenEvent.OnTimeFormatChange -> {
                changeTimeFormat(event.is24HourFormat)

            }

            is SettingsScreenEvent.OnDynamicColorsChange -> {
                setDynamicColorsState(event.isEnabled)
            }

            SettingsScreenEvent.OnAboutLibrariesClick -> {
                sendEvent(SettingsScreenEffect.NavigateToLibrariesScreen)
            }
            SettingsScreenEvent.OnBack -> {
                sendEvent(SettingsScreenEffect.OnBack)
            }
        }
    }

    private fun changeTimeFormat(is24HourFormat : Boolean) = viewModelScope.launch{
        dataStore.setIs24HourFormat(is24HourFormat)
    }

    private fun observeTimeFormat() = viewModelScope.launch {
        dataStore.getIs24HourFormat.collect{ is24HourFormat ->
            _state.update {
                it.copy(
                    is24HourFormat = is24HourFormat
                )
            }
        }
    }

    private fun changeTheme(theme : ThemeOption) = viewModelScope.launch{
        dataStore.setTheme(
            theme
        )
    }

    private fun observeTheme() = viewModelScope.launch {
        dataStore.getTheme.collect{ themeOption ->
            _state.update {
                it.copy(
                    selectedThemeOption = themeOption
                )
            }
        }
    }

    private fun observeDynamicColorsState() = viewModelScope.launch {
        dataStore.getDynamicColorsState.collect{ isEnabled ->
            _state.update {
                it.copy(
                    isDynamicColorsEnabled = isEnabled
                )
            }
        }

    }

    private fun setDynamicColorsState(isEnabled : Boolean) = viewModelScope.launch {
        dataStore.setDynamicColorsState(isEnabled)
    }

    private fun sendEvent(event : SettingsScreenEffect) = viewModelScope.launch{
            _effect.emit(event)

    }

    private fun getAppVersion() {
        val appVersion = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
        _state.update {
            it.copy(
                appVersion = appVersion
            )
        }
    }

}

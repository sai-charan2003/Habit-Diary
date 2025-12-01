package com.charan.habitdiary.presentation.settings

import com.charan.habitdiary.data.model.enums.ThemeOption

sealed class SettingsScreenEvent {
    data class OnThemeChange(val theme : ThemeOption) : SettingsScreenEvent()
    data class OnTimeFormatChange(val is24HourFormat : Boolean) : SettingsScreenEvent()

    data class OnDynamicColorsChange(val isEnabled : Boolean) : SettingsScreenEvent()

    data object OnAboutLibrariesClick : SettingsScreenEvent()

    data object OnBack : SettingsScreenEvent()
}
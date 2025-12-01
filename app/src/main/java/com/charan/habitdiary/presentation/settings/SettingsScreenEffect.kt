package com.charan.habitdiary.presentation.settings

sealed interface SettingsScreenEffect {
    data object NavigateToLibrariesScreen : SettingsScreenEffect

    data object OnBack : SettingsScreenEffect
}
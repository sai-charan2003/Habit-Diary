package com.charan.habitdiary.data.model.enums

enum class ThemeOption {
    SYSTEM_DEFAULT,
    LIGHT,
    DARK;

    override fun toString(): String {
        return when (this) {
            SYSTEM_DEFAULT -> "System"
            LIGHT -> "Light"
            DARK -> "Dark"
        }
    }

    fun getLocaleString() : Int{
        return when(this){
            SYSTEM_DEFAULT -> com.charan.habitdiary.R.string.system
            LIGHT -> com.charan.habitdiary.R.string.light
            DARK -> com.charan.habitdiary.R.string.dark
        }
    }

    fun fromString(value: String): ThemeOption {
        return when (value) {
            "Light" -> LIGHT
            "Dark" -> DARK
            else -> SYSTEM_DEFAULT
        }
    }
}
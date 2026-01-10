package com.charan.habitdiary.data.local

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
object Converters {

    @TypeConverter
    fun localTimeToLong(value : LocalTime) : Int {
        return value.toSecondOfDay()
    }

    @TypeConverter
    fun longToLocalTime(value : Int) : LocalTime {
        return LocalTime.fromSecondOfDay(value)
    }

    @TypeConverter
    fun localDateTimeToLong(value : LocalDateTime) : Long {
        return value.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    @TypeConverter
    fun longToLocalDateTime(value : Long) : LocalDateTime {
        return Instant.fromEpochMilliseconds(value).toLocalDateTime(TimeZone.currentSystemDefault())
    }

    @TypeConverter
    fun dayOfWeekListToString(value: List<DayOfWeek>): String {
        return value.joinToString(separator = ",")
    }

    @TypeConverter
    fun stringToDayOfWeekList(value: String): List<DayOfWeek> {
        return if (value.isEmpty()) {
            emptyList()
        } else {
            value.split(",").map { DayOfWeek.valueOf(it) }
        }
    }

    @TypeConverter
    fun dayOfWeekToString(value: DayOfWeek): String {
        return value.name
    }
    @TypeConverter
    fun stringToDayOfWeek(value: String): DayOfWeek {
        return DayOfWeek.valueOf(value)
    }

    @TypeConverter
    fun localDateTimeToLocalDate(value: LocalDateTime): LocalDate {
        return value.date
    }
}
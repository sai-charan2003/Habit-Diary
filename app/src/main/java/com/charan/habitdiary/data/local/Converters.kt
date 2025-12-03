package com.charan.habitdiary.data.local

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.charan.habitdiary.utils.DateUtil.toTimeMillis
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
        return Instant.fromEpochSeconds(value).toLocalDateTime(TimeZone.currentSystemDefault())
    }
}
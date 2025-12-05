package com.charan.habitdiary.utils


import android.util.Log
import com.kizitonwose.calendar.core.minusMonths
import com.kizitonwose.calendar.core.now
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
@OptIn(ExperimentalTime::class)
object DateUtil {

    fun getDaysOfWeek() : List<DayOfWeek> {
        return listOf(
            DayOfWeek.SUNDAY,
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY
        )
    }

    @OptIn(ExperimentalTime::class)
    fun getTodayDayAndDate(): String {
        val now = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())

        val dayOfWeek = now.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
        val month = now.month.name.lowercase().substring(0, 3).replaceFirstChar { it.uppercase() }

        return "$dayOfWeek, $month ${now.day}"
    }

    fun defaultHabitFrequency() : List<DayOfWeek> {
        return listOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
        )
    }

    @OptIn(ExperimentalTime::class)
    fun todayStartOfDay(): LocalDateTime {
        return Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
            .atStartOfDayIn(TimeZone.currentSystemDefault())
            .toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun todayEndOfDay(): LocalDateTime {
        val today = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        val endOfDay = today.atTime(23, 59, 59, 999_999_999)
        return endOfDay.toInstant(TimeZone.currentSystemDefault())
            .toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun getCurrentDayOfWeek(): DayOfWeek {
        val todayIso = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .dayOfWeek
        Log.d("TAG", "getCurrentDayOfWeek: $todayIso")
        return todayIso
    }


    fun currentDayIndex(): Int {
        val todayIso = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .dayOfWeek
            .isoDayNumber
        return if (todayIso == 7) 0 else todayIso
    }

    fun mergeDateTime(date: LocalDate, time: LocalTime): LocalDateTime {
        val merged = LocalDateTime(
            date.year,
            date.month,
            date.day,
            time.hour,
            time.minute
        )

        return merged.toInstant(TimeZone.currentSystemDefault())
            .toLocalDateTime(TimeZone.currentSystemDefault())
    }


    fun LocalTime.toFormattedString(is24HrFormat : Boolean) : String{
        val hour = if(is24HrFormat){
            this.hour
        } else {
            if(this.hour % 12 ==0) 12 else this.hour % 12
        }
        val minute = this.minute.toString().padStart(2,'0')
        val amPm = if(is24HrFormat) "" else if(this.hour <12) " AM" else " PM"
        return "$hour:$minute$amPm"
    }

    fun LocalDate.toFormattedString() : String{
        val month = this.month.name.lowercase().substring(0, 3).replaceFirstChar { it.uppercase() }
        return "$month ${this.day}, ${this.year}"
    }

    fun Long.toLocalDate() : LocalDate {
        return Instant.fromEpochMilliseconds(this)
            .toLocalDateTime(TimeZone.UTC)
            .date
    }

    @OptIn(ExperimentalTime::class)
    fun getCurrentMonth() : YearMonth {
        return YearMonth.now()
    }

    fun getStartOfMonth() : YearMonth {
        return getCurrentMonth().minusMonths(100)
    }


    @OptIn(ExperimentalTime::class)
    fun getCurrentDate() : LocalDate {
        return LocalDate.now()
    }

    fun getCurrentTime() : LocalTime {
        return Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .time
    }


    fun DayOfWeek.getDisplayName() : String{
        return this.name.lowercase().replaceFirstChar { it.uppercase() }
    }

    fun LocalDate.toEndOfDayMillis() : Long {
        val endOfDay = this.atTime(23, 59, 59, 999_999_999)
        return endOfDay.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    fun getCurrentDateTime() : LocalDateTime {
        return Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun LocalDateTime.toFormattedString(is24HrFormat : Boolean) : String{
        val datePart = this.date.toFormattedString()
        val timePart = this.time.toFormattedString(is24HrFormat)
        return "$datePart, $timePart"
    }

    fun LocalDate.getStartOfDay() : LocalDateTime {
        val startOfDay = this.atStartOfDayIn(TimeZone.currentSystemDefault())
        return startOfDay.toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun LocalDate.getEndOfDay() : LocalDateTime {
        val endOfDay = this.atTime(23, 59, 59, 999_999_999)
        return endOfDay.toInstant(TimeZone.currentSystemDefault())
            .toLocalDateTime(TimeZone.currentSystemDefault())
    }
}

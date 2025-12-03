package com.charan.habitdiary.utils

import androidx.compose.material3.TimePickerState
import com.charan.habitdiary.presentation.add_habit.Time
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

    fun getDayInitials(): List<String> {
        return listOf("S", "M", "T", "W", "T", "F", "S")
    }

    @OptIn(ExperimentalTime::class)
    fun getTodayDayAndDate(): String {
        val now = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())

        val dayOfWeek = now.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
        val month = now.month.name.lowercase().substring(0, 3).replaceFirstChar { it.uppercase() }

        return "$dayOfWeek, $month ${now.day}"
    }

    @OptIn(ExperimentalTime::class)
    fun todayStartOfDay(): Long {
        val today = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date

        val start = today.atStartOfDayIn(TimeZone.currentSystemDefault())
        return start.toEpochMilliseconds()
    }

    fun todayEndOfDay(): Long {
        val timeZone = TimeZone.currentSystemDefault()
        val today = Clock.System.now().toLocalDateTime(timeZone).date
        val endOfDay = today.atTime(23, 59, 59, 999_999_999)
        return endOfDay.toInstant(timeZone).toEpochMilliseconds()
    }


    fun currentDayIndex(): Int {
        val todayIso = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .dayOfWeek
            .isoDayNumber
        return if (todayIso == 7) 0 else todayIso
    }



    fun convertTimeMillisToTimeString(timeMillis: Long , is24HrFormat: Boolean): String {
        val time = Instant.fromEpochMilliseconds(timeMillis)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .time

        val hour = if(is24HrFormat){
            time.hour
        } else {
            if(time.hour % 12 ==0) 12 else time.hour % 12
        }
        val minute = time.minute.toString().padStart(2,'0')
        val amPm = if(is24HrFormat) "" else if(time.hour <12) " AM" else " PM"
        return "$hour:$minute$amPm"
    }

    @OptIn(ExperimentalTime::class)
    fun getDateStringFromMillis(timeMillis: Long): String {
        return Instant.fromEpochMilliseconds(timeMillis)
            .toLocalDateTime(TimeZone.UTC)
            .date
            .toString()
    }
    @OptIn(ExperimentalTime::class)
    fun mergeDateTimeToMillis(dateMillis: Long, timeMillis: Long): Long {
        val date = Instant.fromEpochMilliseconds(dateMillis)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date

        val time = Instant.fromEpochMilliseconds(timeMillis)
            .toLocalDateTime(TimeZone.currentSystemDefault())

        val merged = LocalDateTime(
            date.year,
            date.month,
            date.day,
            time.hour,
            time.minute
        )

        return merged.toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()
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

    @OptIn(ExperimentalTime::class)
    fun TimePickerState.getTimeMillis(): Long {
        val today = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).date

        val dt = LocalDateTime(
            today.year,
            today.month,
            today.day,
            this.hour,
            this.minute
        )

        return dt.toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()
    }

    fun Long.toHourMinute(): Pair<Int, Int> {
        val total = this.toInt()
        val hour = total / 60
        val minute = total % 60
        return hour to minute
    }

    fun Time.toHourMinutesLong(): Long {
        return (this.hour * 60 + this.minutes).toLong()
    }

    fun Time.toTimeString(is24HrFormat : Boolean) : String{
        val hour = if(is24HrFormat){
            this.hour
        } else {
            if(this.hour % 12 ==0) 12 else this.hour % 12
        }
        val minute = this.minutes.toString().padStart(2,'0')
        val amPm = if(is24HrFormat) "" else if(this.hour <12) " AM" else " PM"
        return "$hour:$minute$amPm"
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

    @OptIn(ExperimentalTime::class)
    fun Long.toLocalTime() : LocalTime {
        return Instant.fromEpochMilliseconds(this)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .time
    }

    fun Long.toLocalDate() : LocalDate {
        return Instant.fromEpochMilliseconds(this)
            .toLocalDateTime(TimeZone.UTC)
            .date
    }

    @OptIn(ExperimentalTime::class)
    fun LocalTime.toTimeMillis() : Long {
        val today = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).date

        val dt = LocalDateTime(
            today.year,
            today.month,
            today.day,
            this.hour,
            this.minute
        )

        return dt.toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()
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
}

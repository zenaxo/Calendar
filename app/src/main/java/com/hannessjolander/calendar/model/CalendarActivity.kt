package com.hannessjolander.calendar.model

import android.annotation.SuppressLint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.UUID

/**
 * How often an activity repeats
 */
enum class RepeatAt {
    NEVER,
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY
}

/**
 * @return A string representation for the RepeatAt enum
 */
fun RepeatAt.displayName(): String = when (this) {
    RepeatAt.NEVER -> "Aldrig"
    RepeatAt.DAILY -> "Dagligen"
    RepeatAt.WEEKLY -> "Varje vecka"
    RepeatAt.MONTHLY -> "Varje månad"
    RepeatAt.YEARLY -> "Årligen"
}

/**
 * A calendar activity
 *
 * @param name The name of the activity
 * @param date When the activity is taking place
 * @param repeatAt How often the activity repeats, defaults to never
 * @param startHour The hour the activity starts, eg. 1, 2...,24
 * @param startMinute The minute the activity starts, eg. 32, 48, 60
 * @param endHour The hour the activity ends, eg. 1, 2...,24
 * @param endMinute The minute the activity ends eg. 32, 48, 60
 * @param id A global unique identifier for the activity
 */
class CalendarActivity(
    var name: String,
    var date: LocalDate,
    startHour: Int,
    startMinute: Int,
    endHour: Int,
    endMinute: Int,
    var repeatAt: RepeatAt = RepeatAt.NEVER,
    var id: UUID = UUID.randomUUID(),
) {
    var startTime: LocalDateTime = LocalDateTime.of(date, LocalTime.of(startHour, startMinute))
    var endTime: LocalDateTime = LocalDateTime.of(date, LocalTime.of(endHour, endMinute))

    init {
        require(endTime.isAfter(startTime)) { "End time must be after start time" }
    }

    /**
     * The duration of the activity in minutes
     * @return the duration of the activity in minutes
     */
    fun getDurationInMinutes(): Long {
        return ChronoUnit.MINUTES.between(startTime, endTime)
    }

    /**
     * Determines if the given time is between the start and end time of the activity
     * @param currentDateTime The time to determine if the activity is ongoing on
     * @return true if the activity is on going
     */
    fun isOngoing(currentDateTime: LocalDateTime = LocalDateTime.now()): Boolean {
        return currentDateTime in startTime..endTime
    }

    /**
     * Determines if the activity has past a given time
     * @param currentDateTime the time to compare with
     * @return true if the activity has past the currentDateTime
     */
    fun hasPast(currentDateTime: LocalDateTime = LocalDateTime.now()): Boolean {
        return !currentDateTime.isBefore(this.endTime)
    }

    /**
     * Determines if the activity overlaps with another activity in terms of the start and end time
     *
     * @param other the other activity
     * @return true if they overlaps
     */
    fun overlapsWith(other: CalendarActivity): Boolean {
        return !(this.endTime.isBefore(other.startTime) || this.startTime.isAfter(other.endTime))
    }

    /**
     * Determines if an activity is present on a given date
     * @param dateToCheck the given date
     * @return true if the date to check is the same as the date of the activity OR if it repeats on
     * the dateToCheck
     */
    fun isPresentOn(dateToCheck: LocalDate): Boolean {
        if (dateToCheck == date) return true

        return when (repeatAt) {
            RepeatAt.DAILY -> dateToCheck >= date
            RepeatAt.WEEKLY -> dateToCheck >= date && (ChronoUnit.DAYS.between(date, dateToCheck) % 7).toInt() == 0
            RepeatAt.MONTHLY -> dateToCheck >= date && date.dayOfMonth == dateToCheck.dayOfMonth
            RepeatAt.YEARLY -> dateToCheck >= date && date.dayOfYear == dateToCheck.dayOfYear
            RepeatAt.NEVER -> false
        }
    }

    /**
     * @return a string representation of the activity
     */
    override fun toString(): String {
        return "Activity(name='$name', date=$date, start=$startTime, end=$endTime, repeats=$repeatAt)"
    }

    /**
     * @return a string representation of the time frame of the activity
     */
    @SuppressLint("DefaultLocale")
    fun getTimeString(): String {
        val startFormatted = String.format("%02d:%02d", startTime.hour, startTime.minute)
        val endFormatted = String.format("%02d:%02d", endTime.hour, endTime.minute)
        return "$startFormatted-$endFormatted"
    }
}

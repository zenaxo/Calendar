package com.hannessjolander.calendar.manager

import com.hannessjolander.calendar.model.CalendarActivity
import java.time.LocalDate
import java.util.UUID

/**
 * Manages a collection of calendar activities.
 * Provides functionality to add, retrieve, update, and delete activities.
 */
class CalendarActivityManager {
    private val activities = mutableListOf<CalendarActivity>()

    /**
     * Adds a new activity to the calendar.
     * @param activity The activity to be added.
     */
    fun addActivity(activity: CalendarActivity) {
        activities.add(activity)
    }

    /**
     * Retrieves all activities scheduled on a specific date.
     * @param date The date to filter activities by.
     * @return A list of activities occurring on the given date.
     */
    fun getActivitiesOn(date: LocalDate): List<CalendarActivity> {
        return activities.filter { it.isPresentOn(date) }
    }

    /**
     * Updates an existing activity by its unique identifier.
     * If the activity is found, it is replaced with the new version.
     * @param newActivity The updated activity details.
     * @param id The unique identifier of the activity to update.
     * @return The updated activity if found, otherwise null.
     */
    fun updateActivityById(newActivity: CalendarActivity, id: UUID): CalendarActivity? {
        val index = activities.indexOfFirst { it.id == id }
        if (index != -1) {
            activities[index] = newActivity
            return newActivity
        }
        return null
    }

    /**
     * Deletes an activity from the calendar using its unique identifier.
     * @param id The unique identifier of the activity to delete.
     * @return true if the activity was found and removed, otherwise false.
     */
    fun deleteActivityById(id: UUID): Boolean {
        return activities.removeIf { it.id == id }
    }
}

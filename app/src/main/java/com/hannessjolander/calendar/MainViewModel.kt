package com.hannessjolander.calendar

import androidx.lifecycle.ViewModel
import com.hannessjolander.calendar.manager.CalendarActivityManager
import com.hannessjolander.calendar.model.CalendarActivity
import com.hannessjolander.calendar.util.mockData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.UUID

/**
 * The main view model of the application. Manages the state and activity manager
 */
class MainViewModel : ViewModel() {
    private val am = CalendarActivityManager()

    init {
        mockData(am) // Populate with test data
    }

    private val _activeDate = MutableStateFlow(LocalDate.now())
    /** The date selected by the user or TODAY */
    val activeDate = _activeDate.asStateFlow()

    private val _activeActivities = MutableStateFlow(am.getActivitiesOn(LocalDate.now()))
    /** The activities for the active date */
    val activeActivities = _activeActivities.asStateFlow()

    private val _showNewSheet = MutableStateFlow(false)
    /** If the sheet for creating a new activity is open */
    val showNewSheet = _showNewSheet.asStateFlow()

    private val _showEditSheet = MutableStateFlow(false)
    /** If the sheet for editing an activity is open */
    val showEditSheet = _showEditSheet.asStateFlow()

    private var _activeEdit: CalendarActivity? = null
    /** The activity which is selected to be edited */
    val activeEdit: CalendarActivity?
        get() = _activeEdit

    /**
     * Updates the active date and refreshes the shown activities
     *
     * @param date the new active date
     */
    fun setActiveDate(date: LocalDate) {
        _activeDate.value = date
        _activeActivities.value = am.getActivitiesOn(date)
    }

    /**
     * Update the CalendarActivity to be edited
     *
     * @param activity the CalendarActivity to edit
     */
    fun setActiveEdit(activity: CalendarActivity?) {
        _activeEdit = activity
        _showEditSheet.value = activity != null
    }

    /**
     * Toggle open the sheet to create a new CalendarActivity
     *
     * @param show if the sheet is open or not
     */
    fun toggleNewSheet(show: Boolean) {
        _showNewSheet.value = show
    }

    /**
     * Toggle open the sheet to edit a CalendarActivity
     *
     * @param show if the sheet is open or not
     */
    fun toggleEditSheet(show: Boolean) {
        _showEditSheet.value = show
    }

    /**
     * Add a new CalendarActivity, refresh the shown activities
     *
     * @param activity the activity to add
     */
    fun addActivity(activity: CalendarActivity) {
        am.addActivity(activity)
        _activeActivities.value = am.getActivitiesOn(_activeDate.value)
        _showNewSheet.value = false
    }

    /**
     * Update a CalendarActivity if activeEdit is not null, refresh the shown activities
     *
     * @param activity the activity to add
     */
    fun updateActivity(activity: CalendarActivity) {
        if(activeEdit != null) {
            am.updateActivityById(activity, activeEdit!!.id)
            _activeActivities.value = am.getActivitiesOn(_activeDate.value)
            _showEditSheet.value = false
        }
    }

    /**
     * Delete an activity by ID
     *
     * @param id the ID of the activity
     */
    fun deleteActivity(id: UUID) {
        am.deleteActivityById(id)
        _activeActivities.value = am.getActivitiesOn(_activeDate.value)
        _showEditSheet.value = false
    }

    /**
     * Get a Swedish short representation of a weekday
     *
     * @param dayOfWeek the day of the week
     */
     fun getDayString(dayOfWeek: DayOfWeek): String {
        return when (dayOfWeek) {
            DayOfWeek.MONDAY -> "Mån"
            DayOfWeek.TUESDAY -> "Tis"
            DayOfWeek.WEDNESDAY -> "Ons"
            DayOfWeek.THURSDAY -> "Tor"
            DayOfWeek.FRIDAY -> "Fre"
            DayOfWeek.SATURDAY -> "Lör"
            DayOfWeek.SUNDAY -> "Sön"
        }
    }

    /**
     * Get the day of the week from the Swedish short representation
     *
     * @param day a Swedish short representation of a weekday
     */
    fun getDayOfWeek(day: String): DayOfWeek {
        return when (day) {
            "Mån" -> DayOfWeek.MONDAY
            "Tis" -> DayOfWeek.TUESDAY
            "Ons" -> DayOfWeek.WEDNESDAY
            "Tors" -> DayOfWeek.THURSDAY
            "Fre" -> DayOfWeek.FRIDAY
            "Lör" -> DayOfWeek.SATURDAY
            "Sön" -> DayOfWeek.SUNDAY
            else -> throw IllegalArgumentException("Invalid weekday: $day")
        }
    }
}

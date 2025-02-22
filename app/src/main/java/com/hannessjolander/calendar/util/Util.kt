package com.hannessjolander.calendar.util

import com.hannessjolander.calendar.manager.CalendarActivityManager
import com.hannessjolander.calendar.model.CalendarActivity
import java.time.LocalDate

fun mockData(am: CalendarActivityManager) {
    am.addActivity(CalendarActivity("Jobbmöte", LocalDate.now().minusDays(3), 9, 0, 10, 30))
    am.addActivity(CalendarActivity("Laga middag", LocalDate.now().minusDays(3), 18, 30, 19, 30))
    am.addActivity(CalendarActivity("Kvällspromenad", LocalDate.now().minusDays(3), 19, 0, 20, 0))

    am.addActivity(CalendarActivity("Morgonlöpning", LocalDate.now().minusDays(2), 6, 30, 7, 30))
    am.addActivity(CalendarActivity("Teamsmöte", LocalDate.now().minusDays(2), 10, 0, 11, 0))
    am.addActivity(CalendarActivity("Lunch med vän", LocalDate.now().minusDays(2), 12, 0, 13, 30))
    am.addActivity(CalendarActivity("Onlinekurs", LocalDate.now().minusDays(2), 19, 0, 21, 0))

    am.addActivity(CalendarActivity("Besök hos tandläkaren", LocalDate.now().minusDays(1), 9, 0, 10, 0))
    am.addActivity(CalendarActivity("Gå till gymmet", LocalDate.now().minusDays(1), 10, 0, 11, 0))
    am.addActivity(CalendarActivity("Fika med kollega", LocalDate.now().minusDays(1), 15, 0, 16, 30))
    am.addActivity(CalendarActivity("Filmkväll", LocalDate.now().minusDays(1), 20, 0, 22, 30))

    am.addActivity(CalendarActivity("Äta frukost", LocalDate.now(), 8, 0, 8, 30))
    am.addActivity(CalendarActivity("Gå till gymmet", LocalDate.now(), 7, 30, 9, 0)) // Overlaps
    am.addActivity(CalendarActivity("Föreläsning UMA", LocalDate.now(), 10, 0, 12, 0))
    am.addActivity(CalendarActivity("Ring vårdcentralen", LocalDate.now(), 11, 30, 12, 0)) // Overlaps

    am.addActivity(CalendarActivity("Löpning i parken", LocalDate.now().plusDays(1), 6, 0, 7, 0))
    am.addActivity(CalendarActivity("Arbetsintervju", LocalDate.now().plusDays(1), 9, 30, 10, 30))
    am.addActivity(CalendarActivity("Jobba på projekt", LocalDate.now().plusDays(1), 10, 0, 12, 0))
    am.addActivity(CalendarActivity("Middag med familjen", LocalDate.now().plusDays(1), 18, 0, 20, 0))

    am.addActivity(CalendarActivity("Frukostmöte", LocalDate.now().plusDays(2), 8, 0, 9, 0))
    am.addActivity(CalendarActivity("Gympass", LocalDate.now().plusDays(2), 9, 0, 10, 0))
    am.addActivity(CalendarActivity("Långlunch", LocalDate.now().plusDays(2), 12, 30, 14, 0))
    am.addActivity(CalendarActivity("Bokklubbsträff", LocalDate.now().plusDays(2), 19, 0, 21, 0))

    am.addActivity(CalendarActivity("Sovmorgon", LocalDate.now().plusDays(3), 9, 0, 10, 0))
    am.addActivity(CalendarActivity("Brunch med vänner", LocalDate.now().plusDays(3), 11, 0, 13, 0))
    am.addActivity(CalendarActivity("Spela padel", LocalDate.now().plusDays(3), 14, 0, 15, 30))
    am.addActivity(CalendarActivity("Filmkväll", LocalDate.now().plusDays(3), 20, 0, 22, 0))
}
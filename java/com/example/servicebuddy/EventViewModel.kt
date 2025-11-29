package com.example.servicebuddy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class EventViewModel : ViewModel() {

    private val _events = MutableLiveData<List<MaintenanceEvent>>()
    val events: LiveData<List<MaintenanceEvent>> = _events

    private val eventList = mutableListOf<MaintenanceEvent>()

    private val statusOrder = mapOf(
        "OVERDUE" to 0,
        "PENDING" to 1,
        "UPCOMING" to 2,
        "FUTURE" to 3,
        "COMPLETED" to 4
    )

    init {
        loadDemoData()
    }

    private fun String.toDate(): Date = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(this) ?: Date()

    private fun loadDemoData() {
        eventList.addAll(listOf(
            MaintenanceEvent(id=UUID.randomUUID().toString(), vehicleIdentifier = "CJ 01 XYZ", title = "Insurance Renewal (RCA)", description = "Policy...", category = "DOCUMENT", dueDate = "2025-11-20".toDate(), status = "OVERDUE", price = 150.0),
            MaintenanceEvent(id=UUID.randomUUID().toString(), vehicleIdentifier = "CJ 01 XYZ", title = "Wiper Blade Replacement", description = null, category = "SERVICE", dueDate = "2025-11-10".toDate(), status = "UPCOMING", price = 25.5),
            MaintenanceEvent(id=UUID.randomUUID().toString(), vehicleIdentifier = "B 999 CAR", title = "Brake Pad Replacement", description = "Front pads", category = "SERVICE", dueDate = "2025-11-12".toDate(), status = "UPCOMING", price = 120.0),
            MaintenanceEvent(id=UUID.randomUUID().toString(), vehicleIdentifier = "CJ 01 XYZ", title = "Road Tax (Rovinieta)", description = null, category = "DOCUMENT", dueDate = "2026-02-15".toDate(), status = "FUTURE", price = 28.0),
            MaintenanceEvent(id=UUID.randomUUID().toString(), vehicleIdentifier = "B 999 CAR", title = "Annual Service", description = "Oil change", category = "SERVICE", dueDate = "2025-07-01".toDate(), status = "COMPLETED", price = 250.0),
            MaintenanceEvent(id=UUID.randomUUID().toString(), vehicleIdentifier = "AB 12 CDE", title = "ITP", description = "Technical inspection", category = "DOCUMENT", dueDate = "2025-12-01".toDate(), status = "PENDING", price = 50.0),
            MaintenanceEvent(id=UUID.randomUUID().toString(), vehicleIdentifier = "AB 12 CDE", title = "Tire Change", description = "Winter tires", category = "SERVICE", dueDate = "2025-11-15".toDate(), status = "UPCOMING", price = 40.0),
            MaintenanceEvent(id=UUID.randomUUID().toString(), vehicleIdentifier = "CJ 01 XYZ", title = "Air Filter Change", description = null, category = "SERVICE", dueDate = "2026-01-10".toDate(), status = "FUTURE", price = 30.0),
            MaintenanceEvent(id=UUID.randomUUID().toString(), vehicleIdentifier = "B 999 CAR", title = "CASCO Insurance", description = "Full coverage", category = "DOCUMENT", dueDate = "2026-03-20".toDate(), status = "FUTURE", price = 400.0),
            MaintenanceEvent(id=UUID.randomUUID().toString(), vehicleIdentifier = "AB 12 CDE", title = "Spark Plug Replacement", description = null, category = "SERVICE", dueDate = "2025-09-01".toDate(), status = "COMPLETED", price = 80.0),
            MaintenanceEvent(id=UUID.randomUUID().toString(), vehicleIdentifier = "CJ 01 XYZ", title = "Coolant Flush", description = null, category = "SERVICE", dueDate = "2026-05-01".toDate(), status = "FUTURE", price = 60.0),
            MaintenanceEvent(id=UUID.randomUUID().toString(), vehicleIdentifier = "B 999 CAR", title = "Battery Check", description = null, category = "SERVICE", dueDate = "2025-11-30".toDate(), status = "PENDING", price = 15.0),
            MaintenanceEvent(id=UUID.randomUUID().toString(), vehicleIdentifier = "AB 12 CDE", title = "First Aid Kit Expiry", description = "Check expiry date", category = "DOCUMENT", dueDate = "2026-06-01".toDate(), status = "FUTURE", price = 20.0)
        ))
        updateAndSortList()
    }

    private fun updateAndSortList() {
        val sortedList = eventList.sortedWith(
            compareBy<MaintenanceEvent> { statusOrder[it.status.uppercase(Locale.US)] ?: Int.MAX_VALUE }
                .thenBy { it.dueDate }
        )
        _events.value = sortedList
    }

    fun addEvent(event: MaintenanceEvent) {
        eventList.add(event)
        updateAndSortList()
    }

    fun getEventById(id: String): MaintenanceEvent? {
        return eventList.find { it.id == id }
    }

    @Deprecated("Use updateEventById instead")
    fun updateEvent(updatedEvent: MaintenanceEvent) {
        val index = eventList.indexOfFirst { it.id == updatedEvent.id }
        if (index != -1) {
            eventList[index] = updatedEvent
            updateAndSortList()
        }
    }

    fun updateEventById(oldId: String, updatedEvent: MaintenanceEvent) {
        val index = eventList.indexOfFirst { it.id == oldId }
        if (index != -1) {
            eventList[index] = updatedEvent
            updateAndSortList()
        }
    }

    fun deleteEvent(id: String) {
        eventList.removeAll { it.id == id }
        updateAndSortList()
    }
}
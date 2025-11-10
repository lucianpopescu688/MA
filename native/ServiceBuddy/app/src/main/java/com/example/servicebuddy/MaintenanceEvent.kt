package com.example.servicebuddy

import java.util.Date
import java.util.UUID

data class MaintenanceEvent(
    val id: String = UUID.randomUUID().toString(),
    var vehicleIdentifier: String,
    var title: String,
    var description: String?,
    var category: String,
    var dueDate: Date,
    var status: String,
    var price: Double
)
package com.example.clinicaldatamanagementapp.database

import java.util.Date
import java.util.UUID

data class HealthData(
    val patientId: UUID,
    val bloodPressure: String,
    val bloodOxygen: String,
    val heartBeatRate: String,
    val insertDate: Date = Date()
)

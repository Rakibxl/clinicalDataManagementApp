package com.example.clinicaldatamanagementapp.database

import java.util.Date
import java.util.UUID

public data class Patient(
    val id: Int = 0,
    val fullName: String,
    val dateOfBirth: Date,
    val location: String
)
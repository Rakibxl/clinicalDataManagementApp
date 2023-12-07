package com.example.clinicaldatamanagementapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "patients")
data class PatientEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "fullname")
    val fullName: String,
    @ColumnInfo(name = "dateofbirth")
    val dateOfBirth: Date,
    @ColumnInfo(name = "location")
    val location: String
)

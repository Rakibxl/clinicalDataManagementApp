package com.example.clinicaldatamanagementapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "patienthealthinfos",
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["id"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PatientHealthInformationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "patientId")
    val patientId: Int,

    @ColumnInfo(name = "bloodPressure")
    val bloodPressure: String,

    @ColumnInfo(name = "bloodOxygen")
    val bloodOxygen: Int,

    @ColumnInfo(name = "heartBeatRate")
    val heartBeatRate: Int,

    @ColumnInfo(name = "insertDate")
    val insertDate: Date = Date()
)

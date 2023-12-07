package com.example.clinicaldatamanagementapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.clinicaldatamanagementapp.util.Converters

@Database(entities = [UserEntity::class, PatientEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ClinicalDataManagementDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun patientDao(): PatientDao
}
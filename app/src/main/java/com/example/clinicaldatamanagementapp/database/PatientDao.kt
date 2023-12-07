package com.example.clinicaldatamanagementapp.database

import androidx.room.Insert
import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PatientDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPatients(patients: List<PatientEntity>)

    @Query("SELECT * FROM patients")
    fun getAllPatients(): List<PatientEntity>

    @Query("SELECT * FROM patients WHERE id = :id")
    suspend fun getPatientById(id: Int): PatientEntity?

}
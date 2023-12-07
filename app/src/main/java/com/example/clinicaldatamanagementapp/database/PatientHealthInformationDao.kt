package com.example.clinicaldatamanagementapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PatientHealthInformationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatientHealthInfo(patientHealthInfo: PatientHealthInformationEntity)

    @Query("SELECT * FROM patienthealthinfos")
    fun getAllPatientHealthInfo(): List<PatientHealthInformationEntity>

    @Query("SELECT * FROM patienthealthinfos WHERE patientId = :patientId")
    fun getHealthInfoByPatientId(patientId: Int): List<PatientHealthInformationEntity>

    @Query("SELECT * FROM patienthealthinfos WHERE id = :id")
    fun getHealthInfoById(id: Int): PatientHealthInformationEntity?

}

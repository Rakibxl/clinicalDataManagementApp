package com.example.clinicaldatamanagementapp.application

import android.app.Application
import androidx.room.Room
import com.example.clinicaldatamanagementapp.database.ClinicalDataManagementDatabase

class ClinicalDataManagementApp: Application() {
    companion object{
         lateinit var database : ClinicalDataManagementDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, ClinicalDataManagementDatabase::class.java, "Health").build()

    }

}
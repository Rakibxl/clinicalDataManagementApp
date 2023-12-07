package com.example.clinicaldatamanagementapp.database

import android.content.Context
import androidx.room.Room

object DatabaseBuilder {
    @Volatile
    private var INSTANCE: ClinicalDataManagementDatabase? = null

    fun getDatabase(context: Context): ClinicalDataManagementDatabase {
        // Return the existing instance if it already exists
        return INSTANCE ?: synchronized(this) {
            // Check again in case it was initialized in another thread
            INSTANCE ?: buildDatabase(context).also {
                INSTANCE = it
            }
        }
    }

    private fun buildDatabase(context: Context): ClinicalDataManagementDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ClinicalDataManagementDatabase::class.java,
            "clinicaldatamanagement.db"
        )
            .fallbackToDestructiveMigration() // Handle migration strategy as needed
            .build()
    }
}

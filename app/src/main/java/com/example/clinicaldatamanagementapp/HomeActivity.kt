package com.example.clinicaldatamanagementapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val viewPatientListButton = findViewById<Button>(R.id.viewPatientListButton)
        viewPatientListButton.setOnClickListener {
            // Navigate to Patient List Activity
            val intent = Intent(this, PatientListActivity::class.java)
            startActivity(intent)
        }

        val addPatientButton = findViewById<Button>(R.id.addPatientButton)
        addPatientButton.setOnClickListener {
            val intent = Intent(this, PatientAddActivity::class.java)
            startActivity(intent)
        }
    }
}
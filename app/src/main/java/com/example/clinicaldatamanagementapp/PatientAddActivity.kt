package com.example.clinicaldatamanagementapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.clinicaldatamanagementapp.application.ClinicalDataManagementApp.Companion.database
import com.example.clinicaldatamanagementapp.database.ClinicalDataManagementDatabase
import com.example.clinicaldatamanagementapp.database.PatientEntity
import com.example.clinicaldatamanagementapp.databinding.ActivityPatientAddBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class PatientAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatientAddBinding
    companion object {
        val patients = ArrayList<PatientEntity>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.dateofbirthEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this@PatientAddActivity,
                { _, selectedYear, monthOfYear, dayOfMonth ->
                    val selectedDate = "$selectedYear-${monthOfYear + 1}-$dayOfMonth"
                    binding.dateofbirthEditText.setText(selectedDate)
                }, year, month, day
            )
            datePickerDialog.show()
        }

        binding.submitButton.setOnClickListener {
            val fullName = binding.fullnameEditText.text.toString()
            val dateOfBirthString = binding.dateofbirthEditText.text.toString()
            val location = binding.locationText.text.toString()

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateOfBirth: Date? = try {
                dateFormat.parse(dateOfBirthString)
            } catch (e: ParseException) {
                null
            }

            if (dateOfBirth != null) {
                val newPatientEntity = PatientEntity(
                    fullName = fullName,
                    dateOfBirth = dateOfBirth,
                    location = location
                )

                lifecycleScope.launch (Dispatchers.IO){
                    database.patientDao().insertPatients(listOf(newPatientEntity))
                    runOnUiThread {
                        Toast.makeText(this@PatientAddActivity, "Patient saved to database", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@PatientAddActivity, PatientListActivity::class.java)
                        startActivity(intent)
                    }
                }
            } else {
                Toast.makeText(this@PatientAddActivity, "Invalid date format", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

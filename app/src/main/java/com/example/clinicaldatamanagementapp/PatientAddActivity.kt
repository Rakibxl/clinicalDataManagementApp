package com.example.clinicaldatamanagementapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.clinicaldatamanagementapp.database.ClinicalDataManagementDatabase
import com.example.clinicaldatamanagementapp.database.DatabaseBuilder
import com.example.clinicaldatamanagementapp.database.PatientEntity
import com.example.clinicaldatamanagementapp.database.UserEntity
import com.example.clinicaldatamanagementapp.databinding.ActivityPatientAddBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PatientAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatientAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submitButton.setOnClickListener {
            val fullName = binding.fullnameEditText.text.toString()
            val dateOfBirthString = binding.dateofbirthEditText.text.toString()
            val location = binding.locationText.text.toString()

            // Assuming the date of birth is entered in "yyyy-MM-dd" format
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateOfBirth: Date? = try {
                dateFormat.parse(dateOfBirthString)
            } catch (e: Exception) {
                null
            }

            lifecycleScope.launch {
                val newPatient =  PatientEntity(
                fullName = fullName,
                dateOfBirth = dateOfBirth ?: Date(),
                location = location
            )
                val database = DatabaseBuilder.getDatabase(context=this@PatientAddActivity)

                database.patientDao().insertPatients(listOf(newPatient))
                runOnUiThread {
                    Toast.makeText(this@PatientAddActivity, "Patient created successfully", Toast.LENGTH_SHORT).show()
//                    Toast.makeText(this@SignUpActivity, "Sign Up Successful", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
//                    startActivity(intent)
                }
            }
        }
    }

}


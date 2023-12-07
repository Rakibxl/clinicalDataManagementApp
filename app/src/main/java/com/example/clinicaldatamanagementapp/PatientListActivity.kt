package com.example.clinicaldatamanagementapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.clinicaldatamanagementapp.database.ClinicalDataManagementDatabase
import com.example.clinicaldatamanagementapp.database.DatabaseBuilder
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

data class Patient(val id: Int, val name: String, val dateOfBirth: Date)

class PatientListActivity : AppCompatActivity() {

    private lateinit var patientsRecyclerView: RecyclerView
    private val patientsAdapter = PatientsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_list)

        patientsRecyclerView = findViewById(R.id.patientsRecyclerView)
        patientsRecyclerView.layoutManager = LinearLayoutManager(this)
        patientsRecyclerView.adapter = patientsAdapter

        fetchPatients()
    }

    private fun fetchPatients() {
        lifecycleScope.launch {
            val database = DatabaseBuilder.getDatabase(context = this@PatientListActivity)
            val patientsFromDb = database.patientDao().getAllPatients()
            val patients = patientsFromDb.map { dbPatient ->
                Patient(dbPatient.id, dbPatient.fullName, dbPatient.dateOfBirth)
            }
            runOnUiThread {
                patientsAdapter.patients = patients
                patientsAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun calculateAge(dob: Date): Int {
        val dobCalendar = Calendar.getInstance().apply { time = dob }
        val today = Calendar.getInstance()

        var age = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        return age
    }

    inner class PatientsAdapter : RecyclerView.Adapter<PatientsAdapter.PatientViewHolder>() {
        var patients: List<Patient> = listOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_patient, parent, false)
            return PatientViewHolder(view)
        }

        override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
            val patient = patients[position]
            holder.bind(patient)
        }

        override fun getItemCount(): Int = patients.size

        inner class PatientViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val ivAddHealthData: ImageView = view.findViewById(R.id.ivAddHealthData)
            private val ivViewHistory: ImageView = view.findViewById(R.id.ivViewHistory)

            fun bind(patient: Patient) {
                ivAddHealthData.setOnClickListener {
                    val fragment = AddHealthDataFragment()
                    val fragmentManager = (itemView.context as AppCompatActivity).supportFragmentManager
                    fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .addToBackStack(null)
                        .commit()
                }

                ivViewHistory.setOnClickListener {
                    val fragment = ViewHealthDataFragment()
                    val fragmentManager = (itemView.context as AppCompatActivity).supportFragmentManager
                    fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

    }
}
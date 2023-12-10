package com.example.clinicaldatamanagementapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.clinicaldatamanagementapp.application.ClinicalDataManagementApp.Companion.database
import com.example.clinicaldatamanagementapp.database.ClinicalDataManagementDatabase
import com.example.clinicaldatamanagementapp.database.PatientEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PatientListActivity : AppCompatActivity() {

    private lateinit var patientsRecyclerView: RecyclerView
    private val patientsAdapter = PatientsAdapter()
    private lateinit var mapTypeToggle: ToggleButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_list)
        val toolbar : Toolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.navigationIcon = ResourcesCompat.getDrawable(resources,R.drawable.circle_left_regular,null)
        toolbar.setNavigationOnClickListener{
            finish()
        }

        mapTypeToggle = findViewById(R.id.mapTypeToggle)

        mapTypeToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
            }
        }

        patientsRecyclerView = findViewById(R.id.patientsRecyclerView)
        patientsRecyclerView.layoutManager = LinearLayoutManager(this)
        patientsRecyclerView.adapter = patientsAdapter

        fetchPatients()
    }

    private fun fetchPatients() {
        lifecycleScope.launch(Dispatchers.IO) {
            val patientsFromDb = database.patientDao().getAllPatients()
            runOnUiThread {
                patientsAdapter.patients = patientsFromDb
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
        var patients: List<PatientEntity> = listOf()

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

            private val tvPatientID: TextView = view.findViewById(R.id.tvPatientID)
            private val tvPatientName: TextView = view.findViewById(R.id.tvPatientName)
            private val tvPatientAge: TextView = view.findViewById(R.id.tvPatientAge)
            private val tvPatientLocation: TextView = view.findViewById(R.id.tvPatientLocation)
            private val ivAddHealthData: ImageView = view.findViewById(R.id.ivAddHealthData)
            private val ivViewHistory: ImageView = view.findViewById(R.id.ivViewHistory)

            fun bind(patient: PatientEntity) {
                tvPatientID.text = "ID: ${patient.id}"
                tvPatientName.text = patient.fullName
                tvPatientAge.text = "Age: ${calculateAge(patient.dateOfBirth)}"
                tvPatientLocation.text = "Location: ${patient.location}"

                ivAddHealthData.setOnClickListener {
                    val fragment = AddHealthDataFragment.newInstance(patient.id)
                    val fragmentManager = (itemView.context as AppCompatActivity).supportFragmentManager
                    fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .addToBackStack(null)
                        .commit()
                }

                ivViewHistory.setOnClickListener {
                    val fragment = ViewHealthDataFragment.newInstance(patient.id)
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

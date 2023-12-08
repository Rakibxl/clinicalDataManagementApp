package com.example.clinicaldatamanagementapp

import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clinicaldatamanagementapp.database.Patient
import androidx.room.Room
import com.example.clinicaldatamanagementapp.database.ClinicalDataManagementDatabase
import com.example.clinicaldatamanagementapp.database.DatabaseBuilder
import com.example.clinicaldatamanagementapp.viewModel.MainActivityViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID


class PatientListActivity : AppCompatActivity() {

    private lateinit var patientsRecyclerView: RecyclerView
    private val patientsAdapter = PatientsAdapter()
    private lateinit var toggleButton: ToggleButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_list)

        toggleButton = findViewById(R.id.mapTypeToggle)
        patientsRecyclerView = findViewById(R.id.patientsRecyclerView)
        patientsRecyclerView.layoutManager = LinearLayoutManager(this)
        patientsRecyclerView.adapter = patientsAdapter

        fetchPatients()

        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
            }
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        fetchPatients()
    }
    override fun onResume() {
        super.onResume()
        fetchPatients() // Refresh the list every time
    }

//    private fun fetchPatients() {
//        lifecycleScope.launch {
//            val database = DatabaseBuilder.getDatabase(context = this@PatientListActivity)
//            val patientsFromDb = database.patientDao().getAllPatients()
//            val patients = patientsFromDb.map { dbPatient ->
//                Patient(dbPatient.id, dbPatient.fullName, dbPatient.dateOfBirth)
//            }
//            runOnUiThread {
//                patientsAdapter.patients = patients
//                patientsAdapter.notifyDataSetChanged()
//            }
//        }
//    }

    private fun fetchPatients() {

        val patients = PatientAddActivity.patients

        // Update the adapter's data and notify the adapter about the data set change.
        patientsAdapter.patients = patients
        patientsAdapter.notifyDataSetChanged()

//        val dummyPatients = listOf(
//            Patient(UUID.randomUUID(), "John Doe", parseDate("1992-05-07"), "Toronto"),
//            Patient(UUID.randomUUID(), "Jane Smith", parseDate("1985-05-15"), "Montreal"),
//            Patient(UUID.randomUUID(), "Emily Johnson", parseDate("2000-12-20"), "Edmonton")
//            // Add more patients as needed
//        )
//
//        patientsAdapter.patients = dummyPatients
//        patientsAdapter.notifyDataSetChanged()

//        patientsAdapter.patients = dummyPatients
//        patientsAdapter.notifyDataSetChanged()
//
//        val patients = PatientAddActivity.patients
//        patientsAdapter.patients = ArrayList(patients)
//        patientsAdapter.notifyDataSetChanged()
    }

    private fun parseDate(dateString: String): Date {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.parse(dateString) ?: Date() // Returns the current date if parsing fails
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

            private val tvPatientID: TextView = view.findViewById(R.id.tvPatientID)
            private val tvPatientName: TextView = view.findViewById(R.id.tvPatientName)
            private val tvPatientAge: TextView = view.findViewById(R.id.tvPatientAge)
            private val tvPatientLocation: TextView = view.findViewById(R.id.tvPatientLocation)
            private val ivAddHealthData: ImageView = view.findViewById(R.id.ivAddHealthData)
            private val ivViewHistory: ImageView = view.findViewById(R.id.ivViewHistory)

            fun bind(patient: Patient) {

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
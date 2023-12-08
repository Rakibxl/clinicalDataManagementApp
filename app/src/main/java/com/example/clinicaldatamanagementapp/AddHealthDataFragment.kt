package com.example.clinicaldatamanagementapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.clinicaldatamanagementapp.database.HealthData
import com.example.clinicaldatamanagementapp.database.Patient
import java.util.UUID

class AddHealthDataFragment : Fragment() {

    private lateinit var bloodPressureEditText: EditText
    private lateinit var bloodOxygenEditText: EditText
    private lateinit var heartBeatRateEditText: EditText
    private lateinit var saveButton: Button
    private var patientId: UUID? = null
    private var patientsList: ArrayList<Patient>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_health_data, container, false)

        // Initialize views
        val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
        bloodPressureEditText = view.findViewById(R.id.bloodPressureEditText)
        bloodOxygenEditText = view.findViewById(R.id.bloodOxygenEditText)
        heartBeatRateEditText = view.findViewById(R.id.heartBeatRateEditText)
        saveButton = view.findViewById(R.id.saveButton) // Assuming you have a save button with ID saveButton

        arguments?.let {
            patientId = it.getSerializable("patientId") as UUID?
            val patientName = PatientAddActivity.patients.find { patient -> patient.id == patientId }?.fullName
            titleTextView.text = patientName?.let { name -> "Add Health Data for Patient $name" } ?: "New Patient"
        }

        patientId?.let {
            saveButton.setOnClickListener { handleSaveData() }
        } ?: run {
            saveButton.isEnabled = false
            Toast.makeText(activity, "Error: Patient ID is not available.", Toast.LENGTH_LONG).show()
        }

        return view
    }

    private fun handleSaveData() {
        val bloodPressure = bloodPressureEditText.text.toString()
        val bloodOxygen = bloodOxygenEditText.text.toString()
        val heartBeatRate = heartBeatRateEditText.text.toString()

        // Assuming patientId is passed as a UUID from the activity
        val patientId = arguments?.getSerializable("patientId") as UUID? ?: run {
            Toast.makeText(activity, "Error: Patient ID is not available.", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a new HealthData object
        val newHealthData = HealthData(
            patientId = patientId,
            bloodPressure = bloodPressure,
            bloodOxygen = bloodOxygen,
            heartBeatRate = heartBeatRate
        )

        healthDataRecords.add(newHealthData)

        Toast.makeText(activity, "Health Data Saved Locally", Toast.LENGTH_SHORT).show()
    }

    companion object {
        val healthDataRecords = ArrayList<HealthData>()

        fun newInstance(patientId: UUID): AddHealthDataFragment {
            val fragment = AddHealthDataFragment()
            val args = Bundle()
            args.putSerializable("patientId", patientId)
            fragment.arguments = args
            return fragment
        }
    }
}
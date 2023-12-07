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

class AddHealthDataFragment : Fragment() {

    private lateinit var bloodPressureEditText: EditText
    private lateinit var respiratoryRateEditText: EditText
    private lateinit var bloodOxygenEditText: EditText
    private lateinit var heartBeatRateEditText: EditText
//    private lateinit var saveButton: Button
    private var patientId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_health_data, container, false)

        // Initialize views
        val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
        bloodPressureEditText = view.findViewById(R.id.bloodPressureEditText)
//        respiratoryRateEditText = view.findViewById(R.id.respiratoryRateEditText)
//        bloodOxygenEditText = view.findViewById(R.id.bloodOxygenEditText)
//        heartBeatRateEditText = view.findViewById(R.id.heartBeatRateEditText)
//        saveButton = view.findViewById(R.id.saveButton)

//        saveButton.setOnClickListener { handleSaveData() }

        arguments?.let {
            patientId = it.getInt("patientId")
            titleTextView.text = "Add Health Data for Patient $patientId"
        }

        return view
    }

    private fun handleSaveData() {
        // Logic to handle data locally
        val bloodPressure = bloodPressureEditText.text.toString()
        val respiratoryRate = respiratoryRateEditText.text.toString()
        val bloodOxygen = bloodOxygenEditText.text.toString()
        val heartBeatRate = heartBeatRateEditText.text.toString()

        // Example: Display a toast message
        Toast.makeText(activity, "Data Saved Locally:\nBlood Pressure: $bloodPressure\nRespiratory Rate: $respiratoryRate\nBlood Oxygen: $bloodOxygen\nHeart Beat Rate: $heartBeatRate", Toast.LENGTH_LONG).show()

    }

    companion object {
        fun newInstance(patientId: Int): AddHealthDataFragment {
            val fragment = AddHealthDataFragment()
            val args = Bundle()
            args.putInt("patientId", patientId)
            fragment.arguments = args
            return fragment
        }
    }
}
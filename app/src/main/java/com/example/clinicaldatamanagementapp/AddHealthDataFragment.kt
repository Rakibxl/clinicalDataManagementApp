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
    import androidx.lifecycle.lifecycleScope
    import androidx.room.Room
    import com.example.clinicaldatamanagementapp.application.ClinicalDataManagementApp.Companion.database
    import com.example.clinicaldatamanagementapp.database.ClinicalDataManagementDatabase
    import com.example.clinicaldatamanagementapp.database.PatientHealthInformationEntity
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.launch

    class AddHealthDataFragment : Fragment() {

        private lateinit var bloodPressureEditText: EditText
        private lateinit var bloodOxygenEditText: EditText
        private lateinit var heartBeatRateEditText: EditText
        private lateinit var saveButton: Button
        private var patientId: Int? = null

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_add_health_data, container, false)


            bloodPressureEditText = view.findViewById(R.id.bloodPressureEditText)
            bloodOxygenEditText = view.findViewById(R.id.bloodOxygenEditText)
            heartBeatRateEditText = view.findViewById(R.id.heartBeatRateEditText)
            saveButton = view.findViewById(R.id.saveButton)
            val titleTextView = view.findViewById<TextView>(R.id.titleTextView)

            patientId = arguments?.getInt("patientId")

            patientId?.let {
                fetchPatientNameAndSet(it, titleTextView)
            }

            saveButton.setOnClickListener { handleSaveData() }

            return view
        }

        private fun fetchPatientNameAndSet(patientId: Int, titleTextView: TextView) {
            lifecycleScope.launch(Dispatchers.IO) {
                val patient = database.patientDao().getPatientById(patientId)
                activity?.runOnUiThread {
                    titleTextView.text = "Add Health Data for ${patient?.fullName}"
                }
            }
        }

        private fun handleSaveData() {
            val bloodPressure = bloodPressureEditText.text.toString()
            val bloodOxygen = bloodOxygenEditText.text.toString().toIntOrNull() ?: 0
            val heartBeatRate = heartBeatRateEditText.text.toString().toIntOrNull() ?: 0

            val patientId = this.patientId ?: return

            val newHealthData = PatientHealthInformationEntity(
                patientId = patientId,
                bloodPressure = bloodPressure,
                bloodOxygen = bloodOxygen,
                heartBeatRate = heartBeatRate
            )

            lifecycleScope.launch(Dispatchers.IO) {
                database.patientHealthInformationDao().insertPatientHealthInfo(newHealthData)
                activity?.runOnUiThread {
                    Toast.makeText(activity, "Health Data Saved", Toast.LENGTH_SHORT).show()
                }
            }
        }
        companion object {
            fun newInstance(patientId: Int): AddHealthDataFragment {
                val fragment = AddHealthDataFragment()
                val args = Bundle().apply {
                    putInt("patientId", patientId)
                }
                fragment.arguments = args
                return fragment
            }
        }
    }

package com.example.clinicaldatamanagementapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.clinicaldatamanagementapp.application.ClinicalDataManagementApp.Companion.database
import com.example.clinicaldatamanagementapp.database.ClinicalDataManagementDatabase
import com.example.clinicaldatamanagementapp.database.PatientHealthInformationEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class ViewHealthDataFragment : Fragment() {
    private var patientId: Int? = null
    private lateinit var healthDataRecyclerView: RecyclerView
    private val healthDataAdapter = HealthDataAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_health_data, container, false)

        healthDataRecyclerView = view.findViewById(R.id.healthDataRecyclerView)
        healthDataRecyclerView.layoutManager = LinearLayoutManager(context)
        healthDataRecyclerView.adapter = healthDataAdapter

        val titleTextView = view.findViewById<TextView>(R.id.titleTextView)

        patientId = arguments?.getInt("patientId")

        patientId?.let {
            fetchPatientNameAndSet(it, titleTextView)
        }

        patientId?.let { pid ->
            lifecycleScope.launch(Dispatchers.IO) {
                val healthData = database.patientHealthInformationDao().getHealthInfoByPatientId(pid)
                activity?.runOnUiThread {
                    healthDataAdapter.healthDataList = healthData
                    healthDataAdapter.notifyDataSetChanged()
                }
            }
        }

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

    companion object {
        fun newInstance(patientId: Int): ViewHealthDataFragment {
            val fragment = ViewHealthDataFragment()
            val args = Bundle().apply {
                putInt("patientId", patientId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private class HealthDataAdapter : RecyclerView.Adapter<HealthDataAdapter.HealthDataViewHolder>() {
        var healthDataList: List<PatientHealthInformationEntity> = listOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthDataViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_health_data, parent, false)
            return HealthDataViewHolder(view)
        }

        override fun onBindViewHolder(holder: HealthDataViewHolder, position: Int) {
            val healthData = healthDataList[position]
            holder.bind(healthData)
        }

        override fun getItemCount(): Int = healthDataList.size

        class HealthDataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val bloodPressureTextView: TextView = view.findViewById(R.id.bloodPressureTextView)
            private val bloodOxygenTextView: TextView = view.findViewById(R.id.bloodOxygenTextView)
            private val heartBeatRateTextView: TextView = view.findViewById(R.id.heartBeatRateTextView)
            private val timestampTextView: TextView = view.findViewById(R.id.timestampTextView)

            fun bind(healthData: PatientHealthInformationEntity) {
                bloodPressureTextView.text = "Blood Pressure: ${healthData.bloodPressure}"
                bloodOxygenTextView.text = "Blood Oxygen: ${healthData.bloodOxygen}"
                heartBeatRateTextView.text = "Heart Beat Rate: ${healthData.heartBeatRate}"
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                timestampTextView.text = "Date: ${dateFormat.format(healthData.insertDate)}"
            }
        }
    }
}

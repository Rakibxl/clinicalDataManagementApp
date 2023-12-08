package com.example.clinicaldatamanagementapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clinicaldatamanagementapp.database.HealthData
import com.example.clinicaldatamanagementapp.database.Patient
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID


class ViewHealthDataFragment : Fragment() {
    private var patientId: UUID? = null
    private lateinit var healthDataRecyclerView: RecyclerView
    private val healthDataAdapter = HealthDataAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_health_data, container, false)

        val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
        healthDataRecyclerView = view.findViewById(R.id.healthDataRecyclerView)
        healthDataRecyclerView.layoutManager = LinearLayoutManager(context)
        healthDataRecyclerView.adapter = healthDataAdapter

        arguments?.let {
            patientId = it.getSerializable("patientId") as UUID?
            val patientName = PatientAddActivity.patients.find { patient -> patient.id == patientId }?.fullName
            titleTextView.text = patientName?.let { name -> "Add Health Data for Patient $name" } ?: "New Patient"
        }

        // Filter and display health data for the specific patient
        val patientSpecificData = AddHealthDataFragment.healthDataRecords.filter { it.patientId == patientId }
        healthDataAdapter.healthDataList = patientSpecificData
        healthDataAdapter.notifyDataSetChanged()

        return view
    }

    companion object {
        fun newInstance(patientId: UUID): ViewHealthDataFragment {
            val fragment = ViewHealthDataFragment()
            val args = Bundle()
            args.putSerializable("patientId", patientId)
            fragment.arguments = args
            return fragment
        }
    }

    private class HealthDataAdapter : RecyclerView.Adapter<HealthDataAdapter.HealthDataViewHolder>() {
        var healthDataList: List<HealthData> = listOf()

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

            fun bind(healthData: HealthData) {
                bloodPressureTextView.text = "Blood Pressure: ${healthData.bloodPressure}"
                bloodOxygenTextView.text = "Blood Oxygen: ${healthData.bloodOxygen}"
                heartBeatRateTextView.text = "Heart Beat Rate: ${healthData.heartBeatRate}"
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                timestampTextView.text = "Date: ${dateFormat.format(healthData.insertDate)}"
            }
        }
    }
}
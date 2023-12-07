package com.example.clinicaldatamanagementapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class HealthData(
    val timestamp: String,
    val bloodPressure: String,
    val respiratoryRate: String,
    val bloodOxygen: String,
    val heartBeatRate: String
)

class ViewHealthDataFragment : Fragment() {
    private var patientId: Int = -1
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

        // Here you can populate your health data list
        // For now, let's use some static data for demonstration
        val staticHealthData = listOf(
            HealthData("2023-03-01T12:00:00", "120/80", "16", "98%", "70 bpm"),
            // Add more data here
        )
        healthDataAdapter.healthDataList = staticHealthData
        healthDataAdapter.notifyDataSetChanged()

        arguments?.let {
            patientId = it.getInt("patientId")
            titleTextView.text = "Health Data History for Patient $patientId"
        }

        return view
    }

    companion object {
        fun newInstance(patientId: Int): ViewHealthDataFragment {
            val fragment = ViewHealthDataFragment()
            val args = Bundle()
            args.putInt("patientId", patientId)
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
            private val respiratoryRateTextView: TextView = view.findViewById(R.id.respiratoryRateTextView)
            private val bloodOxygenTextView: TextView = view.findViewById(R.id.bloodOxygenTextView)
            private val heartBeatRateTextView: TextView = view.findViewById(R.id.heartBeatRateTextView)
            private val timestampTextView: TextView = view.findViewById(R.id.timestampTextView)

            fun bind(healthData: HealthData) {
                bloodPressureTextView.text = "Blood Pressure: ${healthData.bloodPressure}"
                respiratoryRateTextView.text = "Respiratory Rate: ${healthData.respiratoryRate}"
                bloodOxygenTextView.text = "Blood Oxygen: ${healthData.bloodOxygen}"
                heartBeatRateTextView.text = "Heart Beat Rate: ${healthData.heartBeatRate}"
                timestampTextView.text = "Date: ${healthData.timestamp}"
            }
        }
    }
}
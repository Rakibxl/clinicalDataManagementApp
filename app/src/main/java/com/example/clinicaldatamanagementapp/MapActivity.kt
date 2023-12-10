package com.example.clinicaldatamanagementapp

import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.example.clinicaldatamanagementapp.application.ClinicalDataManagementApp.Companion.database
import com.example.clinicaldatamanagementapp.databinding.ActivityMapBinding
import com.example.clinicaldatamanagementapp.viewModel.MainActivityViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException


class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private var map: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityMapBinding.inflate(layoutInflater)
//        setContentView(binding.root)
         setContentView(R.layout.activity_map)

//        setupToolbar()

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
//
//    private fun setupToolbar() {
//        val toolbar: Toolbar = findViewById(R.id.topAppBar)
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowHomeEnabled(true)
//        toolbar.setNavigationOnClickListener {
//            finish()
//        }
//    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.uiSettings?.isZoomControlsEnabled = true

        lifecycleScope.launch(Dispatchers.IO) {
            val patients = database.patientDao().getAllPatients()
            val cities = patients.map { it.location }.distinct()

            withContext(Dispatchers.Main) {
                addCityMarkers(cities)
                // Optionally, adjust the camera
                cities.firstOrNull()?.let { city ->
                    val cityLocation = geocodeCity(city)
                    cityLocation?.let { location ->
                        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
                    }
                }
            }
        }
    }

    private fun addCityMarkers(cities: List<String>) {
        cities.forEach { city ->
            val cityLocation = geocodeCity(city)
            if (cityLocation != null) {
                map?.addMarker(MarkerOptions().position(cityLocation).title(city))
            }
        }
    }

    private fun geocodeCity(city: String): LatLng? {
        val geocoder = Geocoder(this)
        try {
            val addresses = geocoder.getFromLocationName(city, 1)
            // Check if 'addresses' is not null and not empty before accessing it
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                return LatLng(address.latitude, address.longitude)
            }
        } catch (e: IOException) {
            Log.e("MapActivity", "Error geocoding city: $city", e)
        }
        return null
    }

}







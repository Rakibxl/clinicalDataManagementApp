package com.example.clinicaldatamanagementapp

import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.clinicaldatamanagementapp.databinding.ActivityMapBinding
import com.example.clinicaldatamanagementapp.viewModel.MainActivityViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapBinding
    private var map: GoogleMap? = null
    private val cities = listOf("Toronto", "New York", "London")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun geocodeCity(city: String): LatLng? {
        val geocoder = Geocoder(this)
        val addresses = geocoder.getFromLocationName(city, 1)
        return if (!addresses.isNullOrEmpty()) {
            LatLng(addresses[0].latitude, addresses[0].longitude)
        } else {
            null
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.uiSettings?.isZoomControlsEnabled = true

        // Get cities from patients and add markers
        val cities = PatientAddActivity.patients.map { it.location }.distinct()
        addCityMarkers(cities)
        // Optionally, move the camera to the first city's location
        cities.firstOrNull()?.let {
            val firstCityLocation = geocodeCity(it)
            firstCityLocation?.let { location ->
                map?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
            }
        }
    }

    private fun addCityMarkers(cities: List<String>) {
        cities.forEach { city ->
            geocodeCity(city)?.let { cityLocation ->
                map?.addMarker(MarkerOptions().position(cityLocation).title(city))
            }
        }
    }

}







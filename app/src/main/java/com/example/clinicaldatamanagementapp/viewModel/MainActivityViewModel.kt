package com.example.clinicaldatamanagementapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clinicaldatamanagementapp.model.PlaceResponse
import com.example.clinicaldatamanagementapp.repo.MapsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    private val _placesStateFlow = MutableStateFlow(emptyList<PlaceResponse>())
    val placesStateFlow: StateFlow<List<PlaceResponse>> = _placesStateFlow.asStateFlow()

    fun getPlacesByType(latitude: Double,
                        longitude: Double,
                        type: String) {
        viewModelScope.launch {
            val places = MapsRepo().getPlacesByType(
                latitude,
                longitude,
                10000,
                type
            )
            _placesStateFlow.value = places
        }
    }
}
package com.example.clinicaldatamanagementapp.repo

import com.example.clinicaldatamanagementapp.model.PlaceResponse
import com.example.clinicaldatamanagementapp.network.MapsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MapsRepo {

    suspend fun getPlacesByType(latitude: Double,
                                longitude: Double,
                                radius: Int,
                                type: String) : List<PlaceResponse> {
        return withContext(Dispatchers.IO) {
            try {
                MapsService
                    .service
                    .getPlaces(
                        "$latitude,$longitude",
                        radius.toString(),
                        type,
                        "AIzaSyA7ORzkNvOYlCEk6c9mRbq88brQ3SGhJ54"
                    ).results
                    .ifEmpty {
                        emptyList()
                    }
            } catch (ex: Exception) {
                emptyList()
            }
        }
    }
}
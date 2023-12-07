package com.example.clinicaldatamanagementapp.model

import com.google.gson.annotations.SerializedName

data class PlacesResponse (
    @SerializedName("error_message")
    val errorMessage: String,
    val results: List<PlaceResponse>
)
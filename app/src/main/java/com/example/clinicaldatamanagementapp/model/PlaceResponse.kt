package com.example.clinicaldatamanagementapp.model

import com.google.gson.annotations.SerializedName

data class PlaceResponse (
    val name: String,
    val geometry: GeometryResponse
)
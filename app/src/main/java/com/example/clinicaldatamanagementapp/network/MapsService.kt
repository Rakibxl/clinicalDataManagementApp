package com.example.clinicaldatamanagementapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MapsService {

    private val retrofit = Retrofit
        .Builder()
        .baseUrl("https://maps.googleapis.com/maps/api/place/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: MapsApi = retrofit.create(MapsApi::class.java)
}
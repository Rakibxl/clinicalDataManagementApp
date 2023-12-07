package com.example.clinicaldatamanagementapp.network

import com.example.clinicaldatamanagementapp.model.PlacesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MapsApi {

    @GET("nearbysearch/json")
    suspend fun getPlaces(@Query("location") location: String,
                          @Query("radius") radius: String,
                          @Query("type") type: String,
                          @Query("key") apiKey: String) : PlacesResponse

    /*location=-33.8670522%2C151.1957362
    &radius=1500
    &type=restaurant
    &key=YOUR_API_KEY*/
}
package com.warpdevelopment.warpweatherapp.data.remote

import com.warpdevelopment.warpweatherapp.data.repository.WeatherDataEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("weather")
    suspend fun searchCities(
        @Query("q") query: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
        @Query("lang") lang: String,
    ): WeatherDataEntity
}
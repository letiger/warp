package com.warpdevelopment.warpweatherapp.data.repository

import com.warpdevelopment.warpweatherapp.data.remote.WeatherApiService
import com.warpdevelopment.warpweatherapp.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val api: WeatherApiService,
    private val apiKey: String
) : WeatherRepository {

    override suspend fun weatherByCity(query: String): Result<WeatherDataEntity> {
        return try {
            val results = api.searchCities(
                apiKey = apiKey,
                query = query,
                units = "metric",
                lang = "en",
            )
            Result.success(results)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}
package com.warpdevelopment.warpweatherapp.data.repository

import com.warpdevelopment.warpweatherapp.data.remote.WeatherApiService
import com.warpdevelopment.warpweatherapp.data.repository.WeatherDataEntity.Companion.toWeatherData
import com.warpdevelopment.warpweatherapp.domain.model.WeatherData
import com.warpdevelopment.warpweatherapp.domain.repository.WeatherRepository

internal const val UNIT_OF_MEASURE = "metric"
internal const val LANGUAGE = "en"

class WeatherRepositoryImpl(
    private val api: WeatherApiService,
    private val apiKey: String
) : WeatherRepository {

    override suspend fun weatherByCity(query: String): Result<WeatherData> {
        return try {
            val results = api.searchCities(
                apiKey = apiKey,
                query = query,
                units = UNIT_OF_MEASURE,
                lang = LANGUAGE,
            ).toWeatherData()
            Result.success(results)
        } catch (exception: Throwable) {
            Result.failure(exception)
        }
    }
}
package com.warpdevelopment.warpweatherapp.domain.repository

import com.warpdevelopment.warpweatherapp.domain.model.WeatherData

interface WeatherRepository {
    suspend fun weatherByCity(query: String): Result<WeatherData>
}
package com.warpdevelopment.warpweatherapp.domain.repository

import com.warpdevelopment.warpweatherapp.data.repository.WeatherDataEntity

interface WeatherRepository {
    suspend fun weatherByCity(query: String): Result<WeatherDataEntity>
}
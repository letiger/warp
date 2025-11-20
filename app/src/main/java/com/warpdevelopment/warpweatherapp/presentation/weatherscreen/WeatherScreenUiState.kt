package com.warpdevelopment.warpweatherapp.presentation.weatherscreen

import com.warpdevelopment.warpweatherapp.domain.model.WeatherData

data class WeatherScreenUiState(
    val isLoading: Boolean = false,
    val weather: WeatherData? = null,
    val errorMessage: String? = null,
    val city: String = "",
)
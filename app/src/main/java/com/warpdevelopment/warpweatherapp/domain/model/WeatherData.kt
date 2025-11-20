package com.warpdevelopment.warpweatherapp.domain.model

import java.math.BigDecimal

data class WeatherData(
    val city: String,
    val temperature: BigDecimal,
    val condition: String,
    val description: String,
    val iconRes: String,
)

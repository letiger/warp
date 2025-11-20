package com.warpdevelopment.warpweatherapp.data.repository

data class WeatherDataEntity(
    val coord: CoordEntity,
    val weather: List<WeatherEntity>,
    val base: String,
    val main: MainEntity,
    val visibility: Long,
    val wind: WindEntity,
    val clouds: CloudsEntity,
    val dt: Long,
    val sys: SysEntity,
    val timezone: Long,
    val id: Long,
    val name: String,
    val cod: Long,
)

data class CoordEntity(
    val lon: Double,
    val lat: Double,
)

data class WeatherEntity(
    val id: Long,
    val main: String,
    val description: String,
    val icon: String,
)

data class MainEntity(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Long,
    val humidity: Long,
    val sea_level: Long,
    val grnd_level: Long,
)

data class WindEntity(
    val speed: Double,
    val deg: Long,
)

data class CloudsEntity(
    val all: Long,
)

data class SysEntity(
    val type: Long,
    val id: Long,
    val country: String,
    val sunrise: Long,
    val sunset: Long,
)
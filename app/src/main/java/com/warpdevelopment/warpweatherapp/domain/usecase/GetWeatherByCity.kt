package com.warpdevelopment.warpweatherapp.domain.usecase

import android.os.Build
import androidx.annotation.RequiresExtension
import com.warpdevelopment.warpweatherapp.core.DispatcherProvider
import com.warpdevelopment.warpweatherapp.core.Resource
import com.warpdevelopment.warpweatherapp.domain.model.WeatherData
import com.warpdevelopment.warpweatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject

private const val NO_RESULTS = "no results for search"
private const val NO_NETWORK = "Failed to connect to network"

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class GetWeatherByCity @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    operator fun invoke(parameters: Params) = flow {
        emit(Resource.Loading())

        try {
            val execute: WeatherData = weatherRepository.weatherByCity(parameters.city)
            emit(Resource.Success(execute))
        } catch (exception: HttpException) {
            when (exception.code()) {
                404 -> emit(Resource.Error(message = NO_RESULTS))
                else -> emit(
                    Resource.Error(message = exception.message(), code = exception.code())
                )
            }
        } catch (_: Exception) {
            emit(Resource.Error(message = NO_NETWORK))
        }
    }.flowOn(dispatcherProvider.io)

    data class Params(val city: String)
}
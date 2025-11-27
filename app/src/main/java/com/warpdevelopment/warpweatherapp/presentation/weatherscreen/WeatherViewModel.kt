package com.warpdevelopment.warpweatherapp.presentation.weatherscreen

import android.os.Build
import androidx.annotation.RequiresExtension
import com.warpdevelopment.warpweatherapp.core.BaseViewModel
import com.warpdevelopment.warpweatherapp.core.DispatcherProvider
import com.warpdevelopment.warpweatherapp.core.Resource
import com.warpdevelopment.warpweatherapp.domain.usecase.GetWeatherByCity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

private const val TIME_OUT = 2000L

@HiltViewModel
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class WeatherViewModel @Inject constructor(
    private val getWeatherByCity: GetWeatherByCity,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel(dispatcherProvider) {
    private val _viewState = MutableStateFlow(WeatherScreenUiState())
    val viewState: StateFlow<WeatherScreenUiState> = _viewState.asStateFlow()

    private var searchJob: Job? = null

    fun onSearch(city: String) {
        _viewState.update {
            it.copy(isLoading = true, city = city)
        }
        searchJob?.cancel()

        searchJob = launchOnIO {
            delay(TIME_OUT)
            val params = GetWeatherByCity.Params(city.trim())
            getWeatherByCity.invoke(params).collect { response ->
                when (response) {
                    is Resource.Error -> {
                        _viewState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = response.message,
                                weather = null
                            )
                        }
                    }

                    is Resource.Loading -> {
                        if (_viewState.value.isLoading.not()) {
                            _viewState.update {
                                it.copy(isLoading = true, weather = null)
                            }
                        }
                    }

                    is Resource.Success -> response.data?.apply {
                        _viewState.update {
                            it.copy(
                                isLoading = false,
                                weather = this
                            )
                        }
                    }
                }

            }
        }
    }

    fun onReset(clearText: Boolean) {
        _viewState.update {
            it.copy(
                isLoading = false,
                errorMessage = null,
                weather = if (clearText) null else _viewState.value.weather,
                city = if (clearText) "" else _viewState.value.city
            )
        }
    }
}
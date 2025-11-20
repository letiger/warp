package com.warpdevelopment.warpweatherapp.presentation.weatherscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.warpdevelopment.warpweatherapp.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
) : ViewModel() {
    private val _viewState = MutableStateFlow(WeatherScreenUiState())
    val viewState: StateFlow<WeatherScreenUiState> = _viewState

    private var searchJob: Job? = null

    fun onSearch(city: String) {
        _viewState.update { it.copy(city = city, isLoading = true) }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(350) // debounce
            weatherRepository.weatherByCity(city).fold(
                onSuccess = { weatherData ->
                    _viewState.update {
                        it.copy(
                            isLoading = false,
                            weather = weatherData
                        )
                    }
                },
                onFailure = {
                    _viewState.update {
                        it.copy(
                            isLoading = false,
                            weather = null,
                            errorMessage = "no results for your search"
                        )
                    }
                }
            )
        }
    }

    fun onReset(clearText: Boolean) {
        _viewState.update {
            it.copy(
                isLoading = false,
                weather = if (clearText) null else viewState.value.weather,
                errorMessage = null,
                city = if (clearText) "" else viewState.value.city
            )
        }
    }
}
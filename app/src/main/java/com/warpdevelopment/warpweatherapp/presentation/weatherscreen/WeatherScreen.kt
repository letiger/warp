package com.warpdevelopment.warpweatherapp.presentation.weatherscreen

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.warpdevelopment.warpweatherapp.R
import com.warpdevelopment.warpweatherapp.domain.model.WeatherData
import com.warpdevelopment.warpweatherapp.ui.theme.WarpWeatherAppTheme
import java.math.BigDecimal
import java.math.RoundingMode

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun WeatherScreenContainer(
    viewModel: WeatherViewModel = hiltViewModel()
) {

    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    WeatherScreen(
        viewState = viewState,
        onSearch = viewModel::onSearch,
        onReset = viewModel::onReset,
    )
}

@Composable
private fun WeatherScreen(
    viewState: WeatherScreenUiState,
    onSearch: (String) -> Unit,
    onReset: (clearText: Boolean) -> Unit,
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val actionLabel = stringResource(R.string.ok)
    LaunchedEffect(viewState.errorMessage) {
        viewState.errorMessage?.let { msg ->
            snackBarHostState.showSnackbar(
                message = msg,
                actionLabel = actionLabel
            )
            onReset(false)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier.imePadding(),
                hostState = snackBarHostState
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(dimensionResource(R.dimen.dimension_all_medium)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val icon = with(Icons.Default) {
                if (viewState.city.isEmpty()) Search else Clear
            }
            OutlinedTextField(
                value = viewState.city,
                onValueChange = { input ->
                    input.takeIf { it.isNotEmpty() }?.let { onSearch(it) }
                },
                shape = RoundedCornerShape(
                    dimensionResource(R.dimen.dimension_all_medium_small)
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text(stringResource(R.string.enter_city_name)) },
                trailingIcon = {
                    IconButton(
                        onClick = { if (icon == Icons.Default.Clear) onReset(true) },
                        content = {
                            Icon(
                                imageVector = icon,
                                contentDescription = stringResource(R.string.search)
                            )
                        },
                        enabled = icon == Icons.Default.Clear,
                    )
                },
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.dimension_all_big)))

            if (viewState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(
                            top = dimensionResource(R.dimen.dimension_all_medium)
                        )
                        .size(dimensionResource(R.dimen.dimension_all_enormous))
                        .align(Alignment.CenterHorizontally)
                )
                return@Scaffold
            }

            if (icon == Icons.Default.Search) {
                Text(
                    text = stringResource(R.string.use_search_bar_to_get_forecast),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                viewState.weather?.apply {
                    WeatherCard(this)
                }
            }
        }
    }
}

@Composable
private fun WeatherCard(weather: WeatherData) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.dimension_all_medium)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.dimension_all_small_tiny)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.dimension_all_medium_big)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(weather.city, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(dimensionResource(R.dimen.dimension_all_small)))
                Text(
                    stringResource(R.string.temperature, weather.temperature),
                    style = MaterialTheme.typography.displaySmall
                )
                Spacer(Modifier.height(dimensionResource(R.dimen.dimension_all_small_tiny)))
                Text(weather.condition, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = weather.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data("https://openweathermap.org/img/wn/${weather.icon}@4x.png")
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }
    }
}


@Preview
@Composable
private fun WeatherScreenSuccessPreview() {
    val weatherData = WeatherData(
        city = "Cape Town",
        temperature = BigDecimal(36.99).setScale(2, RoundingMode.HALF_EVEN),
        condition = "Sunny",
        description = "Clear Sky",
        icon = "R.drawable.ic_launcher_foreground"
    )
    MaterialTheme {
        WeatherScreen(
            viewState = WeatherScreenUiState(
                isLoading = false,
                errorMessage = null,
                weather = weatherData,
            ),
            onSearch = {},
            onReset = {},
        )
    }
}

@Preview
@Composable
private fun WeatherScreenFailurePreview() {
    WarpWeatherAppTheme {
        WeatherScreen(
            viewState = WeatherScreenUiState(
                isLoading = false,
                errorMessage = "Something went wrong"
            ),
            onSearch = {},
            onReset = {},
        )
    }
}

@Preview
@Composable
private fun WeatherScreenLoadingPreview() {
    WarpWeatherAppTheme {
        WeatherScreen(
            viewState = WeatherScreenUiState(
                isLoading = true,
                errorMessage = null,
            ),
            onSearch = {},
            onReset = {},
        )
    }
}

@Preview
@Composable
private fun WeatherScreenErrorPreview() {
    WarpWeatherAppTheme {
        WeatherScreen(
            viewState = WeatherScreenUiState(
                isLoading = false,
                errorMessage = "no results for your search",
            ),
            onSearch = {},
            onReset = {},
        )
    }
}

@Preview
@Composable
private fun WeatherCardPreview() {
    val weatherData = WeatherData(
        city = "Cape Town",
        temperature = BigDecimal(36).setScale(2, RoundingMode.HALF_EVEN),
        condition = "Sunny",
        description = "Clear Sky",
        icon = "R.drawable.ic_launcher_foreground",
    )
    WarpWeatherAppTheme {
        WeatherCard(weatherData)
    }
}
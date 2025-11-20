package com.warpdevelopment.warpweatherapp.di

import com.warpdevelopment.warpweatherapp.BuildConfig
import com.warpdevelopment.warpweatherapp.core.DefaultDispatcherProvider
import com.warpdevelopment.warpweatherapp.core.DispatcherProvider
import com.warpdevelopment.warpweatherapp.data.remote.WeatherApiService
import com.warpdevelopment.warpweatherapp.data.repository.WeatherRepositoryImpl
import com.warpdevelopment.warpweatherapp.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Binds
    fun bindDispatcherProvider(dispatcherProvider: DefaultDispatcherProvider): DispatcherProvider

    companion object {
        @Provides
        fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }


        @Provides
        fun provideOkHttpClient(
            httpLoggingInterceptor: HttpLoggingInterceptor
        ): OkHttpClient =
            OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .build()

        @Provides
        fun provideRetrofit(okHttpClient: OkHttpClient): WeatherApiService = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)

        @Provides
        fun provideWeatherApiRepository(
            weatherApiService: WeatherApiService
        ): WeatherRepository = WeatherRepositoryImpl(
            api = weatherApiService,
            apiKey = BuildConfig.OPEN_WEATHER_KEY,
        )
    }
}
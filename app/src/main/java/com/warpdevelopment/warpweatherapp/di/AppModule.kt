package com.warpdevelopment.warpweatherapp.di

import com.warpdevelopment.warpweatherapp.core.DefaultDispatcherProvider
import com.warpdevelopment.warpweatherapp.core.DispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Binds
    fun bindDispatcherProvider(dispatcherProvider: DefaultDispatcherProvider): DispatcherProvider
}
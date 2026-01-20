package com.example.chargescheduler.di

import com.example.chargescheduler.data.Repository.SampleDataRepository
import com.example.chargescheduler.domain.scheduler.ChargingSchedulerAlgorithm
import com.example.chargescheduler.domain.usecase.GetAvailableHoursUseCase
import com.example.chargescheduler.domain.usecase.GetChargerUseCase
import com.example.chargescheduler.domain.usecase.GetTruckUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSampleDataRepository(): SampleDataRepository {
        return SampleDataRepository()
    }

    @Provides
    fun provideGetAvailableHoursUseCase(repository: SampleDataRepository): GetAvailableHoursUseCase {
        return GetAvailableHoursUseCase(repository)
    }

    @Provides
    fun provideGetChargerUseCase(repository: SampleDataRepository): GetChargerUseCase {
        return GetChargerUseCase(repository)
    }

    @Provides
    fun provideGetTruckUseCase(repository: SampleDataRepository): GetTruckUseCase {
        return GetTruckUseCase(repository)
    }

    @Provides
    fun provideChargingSchedulerAlgorithm(): ChargingSchedulerAlgorithm {
        return ChargingSchedulerAlgorithm()
    }
}
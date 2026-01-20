package com.example.chargescheduler.domain.usecase

import com.example.chargescheduler.data.Repository.SampleDataRepository
import com.example.chargescheduler.domain.model.Truck
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetTruckUseCase(
    private val sampleDataRepository: SampleDataRepository,
) {
    fun execute(): Flow<List<Truck>> = flow {
        emit(sampleDataRepository.getTrucks())
    }.flowOn(Dispatchers.IO)
}
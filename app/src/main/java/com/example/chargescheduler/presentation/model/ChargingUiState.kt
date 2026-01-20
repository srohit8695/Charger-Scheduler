package com.example.chargescheduler.presentation.model

import com.example.chargescheduler.domain.model.Charger
import com.example.chargescheduler.domain.model.Truck


data class ChargingUiState(
    val availableHours: Int = 0,
    val chargers: List<Charger> = emptyList(),
    val trucks: List<Truck> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
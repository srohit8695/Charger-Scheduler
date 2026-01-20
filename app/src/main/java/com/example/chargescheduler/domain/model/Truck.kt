package com.example.chargescheduler.domain.model

data class Truck(
    val id: String,
    val batteryCapacityKWh: Double,
    val currentChargePercent: Double
) {
    fun getEnergyNeededToFull(): Double {
        return batteryCapacityKWh * (1 - currentChargePercent / 100.0)
    }
}

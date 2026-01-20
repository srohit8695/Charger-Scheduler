package com.example.chargescheduler.domain.scheduler

import com.example.chargescheduler.domain.model.Charger
import com.example.chargescheduler.domain.model.Truck

interface ChargingScheduler {
    fun createSchedule(
        trucks: List<Truck>,
        chargers: List<Charger>,
        availableHours: Int
    ): ChargingSchedule
}
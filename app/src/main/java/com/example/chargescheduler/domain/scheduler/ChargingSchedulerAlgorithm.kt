package com.example.chargescheduler.domain.scheduler

import android.util.Log
import com.example.chargescheduler.domain.model.Charger
import com.example.chargescheduler.domain.model.ChargerSlot
import com.example.chargescheduler.domain.model.ChargingTask
import com.example.chargescheduler.domain.model.Truck
import java.util.PriorityQueue

class ChargingSchedulerAlgorithm : ChargingScheduler {

    override fun createSchedule(
        trucks: List<Truck>,
        chargers: List<Charger>,
        availableHours: Int
    ): ChargingSchedule {

        val schedule = ChargingSchedule()

        val chargerQueue = PriorityQueue<ChargerSlot>(compareBy { it.currentTime })

        chargers.forEach { charger ->
            chargerQueue.add(ChargerSlot(charger, 0.0))
        }

        val sortedTrucks = trucks.sortedBy { it.getEnergyNeededToFull() }

        for (truck in sortedTrucks) {

            val slot = chargerQueue.poll()

            slot?.let {
                val energyNeeded = truck.getEnergyNeededToFull()
                val hoursNeeded = energyNeeded / it.charger.chargingRateKW
                if (it.currentTime + hoursNeeded <= availableHours) {
                    schedule.assignTask(
                        it.charger,
                        ChargingTask(truck, hoursNeeded)
                    )
                    it.currentTime += hoursNeeded
                }
                chargerQueue.add(it)
            }
        }

        return schedule
    }

}
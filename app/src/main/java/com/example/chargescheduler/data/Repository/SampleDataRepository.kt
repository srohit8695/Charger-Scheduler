package com.example.chargescheduler.data.Repository

import com.example.chargescheduler.domain.model.Charger
import com.example.chargescheduler.domain.model.Truck
import kotlinx.coroutines.flow.Flow

class SampleDataRepository {

    fun getTrucks(): List<Truck> = listOf(
        Truck("Truck_1", 100.0, 20.0),
        Truck("Truck_2", 120.0, 30.0),
        Truck("Truck_3", 80.0, 80.0),
        Truck("Truck_4", 60.0, 40.0),
        Truck("Truck_5", 200.0, 60.0),
        Truck("Truck_6", 50.0, 30.0),
        Truck("Truck_7", 60.0, 10.0),
        Truck("Truck_8", 130.0, 60.0)
    )

    fun getChargers(): List<Charger> = listOf(
        Charger("Charger_1", 20.0),
        Charger("Charger_2", 15.0)
    )

    fun getAvailableHours(): Int = 8

}
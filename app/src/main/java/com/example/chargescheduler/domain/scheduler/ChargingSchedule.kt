package com.example.chargescheduler.domain.scheduler

import com.example.chargescheduler.domain.model.Charger
import com.example.chargescheduler.domain.model.ChargingTask

class ChargingSchedule {

    private val schedule: MutableMap<Charger, MutableList<ChargingTask>> = mutableMapOf()

    fun assignTask(charger: Charger, task: ChargingTask) {
        val tasks = schedule.getOrPut(charger) { mutableListOf() }
        tasks.add(task)
    }

    fun getSchedule(): Map<Charger, List<ChargingTask>> = schedule
}
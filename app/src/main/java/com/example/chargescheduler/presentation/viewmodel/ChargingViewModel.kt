package com.example.chargescheduler.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chargescheduler.data.Repository.SampleDataRepository
import com.example.chargescheduler.domain.scheduler.ChargingSchedule
import com.example.chargescheduler.domain.scheduler.ChargingSchedulerAlgorithm
import com.example.chargescheduler.domain.usecase.GetAvailableHoursUseCase
import com.example.chargescheduler.domain.usecase.GetChargerUseCase
import com.example.chargescheduler.domain.usecase.GetTruckUseCase
import com.example.chargescheduler.presentation.model.ChargingUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class ChargingViewModel() : ViewModel() {

    private val repository: SampleDataRepository = SampleDataRepository()

    private val getAvailableHoursUseCase = GetAvailableHoursUseCase(repository)

    private val getChargerUseCase = GetChargerUseCase(repository)

    private val getTruckUseCase = GetTruckUseCase(repository)

    private val scheduler = ChargingSchedulerAlgorithm()
    private val _uiState = MutableStateFlow(ChargingUiState())
    val uiState: StateFlow<ChargingUiState> = _uiState.asStateFlow()

    private val _schedule = MutableStateFlow<ChargingSchedule?>(null)
    val schedule: StateFlow<ChargingSchedule?> = _schedule

    init {
        loadChargingData()
    }

    private fun loadChargingData() {
        viewModelScope.launch {
            combine(
                getAvailableHoursUseCase.execute(),
                getChargerUseCase.execute(),
                getTruckUseCase.execute()
            ) { hours, chargers, trucks ->

                Triple(hours, chargers, trucks)
            }
                .distinctUntilChanged()
                .catch { throwable ->
                    _uiState.value = ChargingUiState(
                        isLoading = false,
                        error = throwable.message
                    )
                }
                .collect { (hours, chargers, trucks) ->

                    val schedule = scheduler.createSchedule(
                        trucks = trucks,
                        chargers = chargers,
                        availableHours = hours
                    )
                    _schedule.value = schedule
                    _uiState.value = ChargingUiState(
                        availableHours = hours,
                        chargers = chargers,
                        trucks = trucks,
                        isLoading = false
                    )

                }
        }
    }
}
package com.example.chargescheduler.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chargescheduler.domain.scheduler.ChargingSchedule
import com.example.chargescheduler.domain.scheduler.ChargingSchedulerAlgorithm
import com.example.chargescheduler.domain.usecase.GetAvailableHoursUseCase
import com.example.chargescheduler.domain.usecase.GetChargerUseCase
import com.example.chargescheduler.domain.usecase.GetTruckUseCase
import com.example.chargescheduler.presentation.model.ChargingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChargingViewModel @Inject constructor(
    private val getAvailableHoursUseCase: GetAvailableHoursUseCase,
    private val getChargerUseCase: GetChargerUseCase,
    private val getTruckUseCase: GetTruckUseCase,
    private val scheduler: ChargingSchedulerAlgorithm
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChargingUiState())
    val uiState: StateFlow<ChargingUiState> = _uiState

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
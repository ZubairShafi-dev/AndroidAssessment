package com.example.androidassessment.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Singleton shared state bus between [BreedsViewModel] and [SettingsViewModel].
 *
 * BreedsViewModel writes to this holder whenever polling starts/stops or a
 * refresh completes. SettingsViewModel reads from it to display live status.
 *
 * This avoids tight coupling between the two ViewModels while keeping the
 * data flow unidirectional.
 */
@Singleton
class AppStateHolder @Inject constructor() {

    private val _isPollingActive = MutableStateFlow(false)
    val isPollingActive: StateFlow<Boolean> = _isPollingActive.asStateFlow()

    private val _lastFetchTime = MutableStateFlow<Instant?>(null)
    val lastFetchTime: StateFlow<Instant?> = _lastFetchTime.asStateFlow()

    fun setPollingActive(active: Boolean) {
        _isPollingActive.value = active
    }

    fun setLastFetchTime(time: Instant) {
        _lastFetchTime.value = time
    }
}

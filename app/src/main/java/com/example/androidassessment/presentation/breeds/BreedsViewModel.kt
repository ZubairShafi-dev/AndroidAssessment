package com.example.androidassessment.presentation.breeds

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidassessment.domain.model.Breed
import com.example.androidassessment.domain.usecase.GetBreedsUseCase
import com.example.androidassessment.domain.usecase.RefreshBreedsUseCase
import com.example.androidassessment.util.AppLogger
import com.example.androidassessment.util.AppStateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

data class BreedsUiState(
    val breeds: List<Breed> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val lastUpdated: Instant? = null,
    val isPollingActive: Boolean = false
)

@HiltViewModel
class BreedsViewModel @Inject constructor(
    private val getBreeds: GetBreedsUseCase,
    private val refreshBreeds: RefreshBreedsUseCase,
    private val appStateHolder: AppStateHolder
) : ViewModel() {

    companion object {
        private const val TAG = "BreedsViewModel"
        private const val POLL_INTERVAL_MS = 35_000L
    }

    private val _uiState = MutableStateFlow(BreedsUiState())
    val uiState: StateFlow<BreedsUiState> = _uiState.asStateFlow()

    private var pollingJob: Job? = null

    init {
        getBreeds()
            .onEach { breeds -> _uiState.update { it.copy(breeds = breeds) } }
            .launchIn(viewModelScope)
    }

    fun startPolling() {
        if (pollingJob?.isActive == true) return
        pollingJob = viewModelScope.launch {
            AppLogger.d(TAG, "Polling started (interval=${POLL_INTERVAL_MS}ms)")
            _uiState.update { it.copy(isPollingActive = true) }
            appStateHolder.setPollingActive(true)
            while (isActive) {
                doRefresh()
                delay(POLL_INTERVAL_MS)
            }
        }
    }

    fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
        _uiState.update { it.copy(isPollingActive = false) }
        appStateHolder.setPollingActive(false)
        AppLogger.d(TAG, "Polling stopped")
    }

    fun refresh() {
        viewModelScope.launch { doRefresh() }
    }

    private suspend fun doRefresh() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        try {
            refreshBreeds()
            val now = Instant.now()
            _uiState.update { it.copy(isLoading = false, lastUpdated = now, error = null) }
            appStateHolder.setLastFetchTime(now)
            AppLogger.d(TAG, "Breeds refreshed at $now")
        } catch (e: kotlinx.coroutines.CancellationException) {
            _uiState.update { it.copy(isLoading = false) }
            throw e
        } catch (e: Exception) {
            val msg = e.message ?: "Unknown error"
            AppLogger.e(TAG, "Refresh failed: $msg", e)
            _uiState.update { it.copy(isLoading = false, error = msg) }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    val lifecycleObserver: DefaultLifecycleObserver = object : DefaultLifecycleObserver {
        override fun onStart(owner: LifecycleOwner) {
            AppLogger.d(TAG, "onStart → startPolling()")
            startPolling()
        }

        override fun onStop(owner: LifecycleOwner) {
            AppLogger.d(TAG, "onStop → stopPolling()")
            stopPolling()
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopPolling()
        AppLogger.d(TAG, "onCleared — polling job cancelled")
    }
}

package com.example.androidassessment.presentation.randomusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidassessment.domain.model.RandomUser
import com.example.androidassessment.domain.usecase.GetRandomUsersUseCase
import com.example.androidassessment.domain.usecase.RefreshRandomUsersUseCase
import com.example.androidassessment.util.AppLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RandomUsersUiState(
    val users: List<RandomUser> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class RandomUsersViewModel @Inject constructor(
    private val getRandomUsers: GetRandomUsersUseCase,
    private val refreshRandomUsers: RefreshRandomUsersUseCase
) : ViewModel() {

    companion object { private const val TAG = "RandomUsersViewModel" }

    private val _uiState = MutableStateFlow(RandomUsersUiState())
    val uiState: StateFlow<RandomUsersUiState> = _uiState.asStateFlow()

    init {
        getRandomUsers().onEach { users ->
            _uiState.update { it.copy(users = users) }
        }.launchIn(viewModelScope)
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                refreshRandomUsers()
                _uiState.update { it.copy(isLoading = false) }
                AppLogger.d(TAG, "Random users refreshed")
            } catch (e: kotlinx.coroutines.CancellationException) {
                _uiState.update { it.copy(isLoading = false) }
                throw e
            } catch (e: Exception) {
                val msg = e.message ?: "Unknown error"
                AppLogger.e(TAG, "Refresh failed: $msg", e)
                _uiState.update { it.copy(isLoading = false, error = msg) }
            }
        }
    }
}

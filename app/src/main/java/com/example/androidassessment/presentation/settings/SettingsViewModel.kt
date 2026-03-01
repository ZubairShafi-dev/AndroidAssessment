package com.example.androidassessment.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidassessment.BuildConfig
import com.example.androidassessment.util.AppStateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appStateHolder: AppStateHolder
) : ViewModel() {

    companion object {
        const val DOG_API_BASE_URL   = "https://dog.ceo/api/"
        const val USERS_API_BASE_URL = "https://jsonplaceholder.typicode.com/"
        const val POSTS_API_BASE_URL = "https://jsonplaceholder.typicode.com/"
        const val RANDOM_USER_API_URL = "https://randomuser.me/"
    }

    val flavor: String = BuildConfig.FLAVOR

    val isPollingActive: StateFlow<Boolean> = appStateHolder.isPollingActive
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val lastFetchTime: StateFlow<Instant?> = appStateHolder.lastFetchTime
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}

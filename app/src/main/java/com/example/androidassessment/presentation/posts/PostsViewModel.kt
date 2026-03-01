package com.example.androidassessment.presentation.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidassessment.domain.model.Post
import com.example.androidassessment.domain.usecase.GetPostsUseCase
import com.example.androidassessment.domain.usecase.RefreshPostsUseCase
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

data class PostsUiState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val getPosts: GetPostsUseCase,
    private val refreshPosts: RefreshPostsUseCase
) : ViewModel() {

    companion object { private const val TAG = "PostsViewModel" }

    private val _uiState = MutableStateFlow(PostsUiState())
    val uiState: StateFlow<PostsUiState> = _uiState.asStateFlow()

    init {
        getPosts().onEach { posts ->
            _uiState.update { it.copy(posts = posts) }
        }.launchIn(viewModelScope)
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                refreshPosts()
                _uiState.update { it.copy(isLoading = false) }
                AppLogger.d(TAG, "Posts refreshed")
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

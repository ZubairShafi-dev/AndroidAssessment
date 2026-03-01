package com.example.androidassessment.presentation.posts

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidassessment.domain.model.Post
import com.example.androidassessment.domain.usecase.GetPostByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val getPostById: GetPostByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val postId: Int = checkNotNull(savedStateHandle.get<Int>("postId"))

    private val _post = MutableStateFlow<Post?>(null)
    val post: StateFlow<Post?> = _post.asStateFlow()

    init {
        viewModelScope.launch {
            _post.value = getPostById(postId)
        }
    }
}

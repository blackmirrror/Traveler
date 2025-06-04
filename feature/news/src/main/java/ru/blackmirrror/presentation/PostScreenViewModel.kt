package ru.blackmirrror.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.blackmirrror.core.state.ResultState
import ru.blackmirrror.core.state.ScreenState
import ru.blackmirrror.data.PostDto
import ru.blackmirrror.domain.PostRepository
import ru.blackmirrror.navigator.TravelerNavigator
import javax.inject.Inject

@HiltViewModel
class PostScreenViewModel @Inject constructor(
    private val travelerNavigator: TravelerNavigator,
    private val postRepository: PostRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), TravelerNavigator by travelerNavigator {

    private val _state = MutableStateFlow<ScreenState<List<PostDto>>>(ScreenState.Loading())
    val state: StateFlow<ScreenState<List<PostDto>>> = _state.asStateFlow()

    init {
        loadPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            postRepository.getAllPosts().collect { posts ->
                when(posts) {
                    is ResultState.Loading -> _state.value = ScreenState.Loading(posts.data)
                    is ResultState.Success -> _state.value = ScreenState.Success(posts.data)
                    is ResultState.Error -> _state.value = ScreenState.Error(posts.error, posts.data)
                }
            }
        }
    }
}
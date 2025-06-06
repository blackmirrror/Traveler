package ru.blackmirrror.map.presentation.show

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
import ru.blackmirrror.destinations.MapShowMarkDestination.MARK_ID_PARAM
import ru.blackmirrror.map.data.MarkDto
import ru.blackmirrror.map.domain.MapRepository
import ru.blackmirrror.navigator.TravelerNavigator
import javax.inject.Inject

@HiltViewModel
class ShowMarkViewModel @Inject constructor(
    private val mapRepository: MapRepository,
    private val travelerNavigator: TravelerNavigator,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), TravelerNavigator by travelerNavigator {

    private var _state = MutableStateFlow<ScreenState<MarkDto>>(ScreenState.Loading())
    val state: StateFlow<ScreenState<MarkDto>> = _state.asStateFlow()

    init {
        val id = savedStateHandle.get<Long>(MARK_ID_PARAM)

        viewModelScope.launch {
            id?.let { mapRepository.getMark(it) }?.collect { mark ->
                when(mark) {
                    is ResultState.Success -> {
                        _state.value = ScreenState.Success(mark.data)
                    }
                    else -> Unit
                }
            }
        }
    }
}

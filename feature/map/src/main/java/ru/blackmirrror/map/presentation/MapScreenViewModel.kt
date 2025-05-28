package ru.blackmirrror.map.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.blackmirrror.core.state.ResultState
import ru.blackmirrror.core.state.ScreenState
import ru.blackmirrror.destinations.SearchFilterDestination
import ru.blackmirrror.map.domain.Category
import ru.blackmirrror.map.domain.MapRepository
import ru.blackmirrror.map.domain.model.Mark
import ru.blackmirrror.navigator.TravelerNavigator
import javax.inject.Inject

@HiltViewModel
class MapScreenViewModel @Inject constructor(
    private val travelerNavigator: TravelerNavigator,
    private val mapRepository: MapRepository
) : ViewModel(), TravelerNavigator by travelerNavigator {

    private val _state = MutableStateFlow<ScreenState<List<Mark>>>(ScreenState.Loading())
    val state: StateFlow<ScreenState<List<Mark>>> = _state.asStateFlow()

    init {
        processEvent(MapEvent.LoadMarks())
    }

    fun processEvent(event: MapEvent) {
        when (event) {
            is MapEvent.LoadMarks -> loadMarks(
                event.category,
                event.radius,
                event.minRating
            )
            is MapEvent.ToSearchFilters -> navigate(SearchFilterDestination.route())
            is MapEvent.ToDescription -> navigate(SearchFilterDestination.route())
        }
    }

    private fun loadMarks(category: Category?, radius: Int?, minRating: Int?) {
        viewModelScope.launch {
            mapRepository.loadMarks().collect { marks ->
                when(marks) {
                    is ResultState.Loading -> _state.value = ScreenState.Loading(marks.data)
                    is ResultState.Success -> _state.value = ScreenState.Success(marks.data)
                    is ResultState.Error -> _state.value = ScreenState.Error(marks.error, marks.data)
                }
            }
        }
    }
}
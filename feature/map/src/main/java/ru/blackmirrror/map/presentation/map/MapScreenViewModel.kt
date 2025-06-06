package ru.blackmirrror.map.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.blackmirrror.core.state.ResultState
import ru.blackmirrror.core.state.ScreenState
import ru.blackmirrror.destinations.MapCreateMarkDestination
import ru.blackmirrror.destinations.MapFilterDestination
import ru.blackmirrror.destinations.MapShowMarkDestination
import ru.blackmirrror.map.data.MarkLatLngDto
import ru.blackmirrror.map.domain.Category
import ru.blackmirrror.map.domain.MapRepository
import ru.blackmirrror.map.domain.toMarkCategoryDto
import ru.blackmirrror.navigator.NavigatorResult
import ru.blackmirrror.navigator.TravelerNavigator
import javax.inject.Inject

@HiltViewModel
class MapScreenViewModel @Inject constructor(
    private val travelerNavigator: TravelerNavigator,
    private val mapRepository: MapRepository
) : ViewModel(), TravelerNavigator by travelerNavigator {

    private val _state = MutableStateFlow<ScreenState<List<MarkLatLngDto>>>(ScreenState.Loading())
    val state: StateFlow<ScreenState<List<MarkLatLngDto>>> = _state.asStateFlow()

    init {
        observeNavigationResults()
        processEvent(MapEvent.LoadMarks())
    }

    private fun observeNavigationResults() {
        viewModelScope.launch {
            results.collect { result ->
                when (result) {
                    is NavigatorResult.FiltersApplied -> {
                        loadMarks(
                            categories = result.categories as? List<Category>?,
                            distance = result.radius,
                            minRating = result.minRating,

                        )
                    }
                    is NavigatorResult.CreateMark -> {
                        processEvent(MapEvent.LoadMarks())
                    }
                    else -> Unit
                }
            }
        }
    }

    fun processEvent(event: MapEvent) {
        when (event) {
            is MapEvent.LoadMarks -> loadMarks(
                minRating = event.minRating?.toDouble(),
                distance = event.radius?.toDouble(),
                lat = event.lat,
                lon = event.lon,
                categories = event.categories,
            )
            is MapEvent.ToSearchFilters -> navigate(MapFilterDestination.createMapFilterRoute())
            is MapEvent.ToShowMark -> navigate(MapShowMarkDestination.createMapShowMarkRoute(event.id))
            is MapEvent.ToCreateMark -> createMark(
                lat = event.lat.toString(),
                long = event.long.toString()
            )
        }
    }

    private fun loadMarks(
        minRating: Double? = null,
        distance: Double? = null,
        lat: Double? = null,
        lon: Double? = null,
        categories: List<Category>? = null
    ) {
        viewModelScope.launch {
            mapRepository.getAllMarks(
                minRating, distance, lat, lon, categories?.map { it.toMarkCategoryDto() }
            ).collect { marks ->
                when(marks) {
                    is ResultState.Loading -> _state.value = ScreenState.Loading(marks.data)
                    is ResultState.Success -> _state.value = ScreenState.Success(marks.data)
                    is ResultState.Error -> _state.value = ScreenState.Error(marks.error, marks.data)
                }
            }
        }
    }

    private fun createMark(lat: String, long: String) {
        navigate(MapCreateMarkDestination.createMapCreateMarkRoute(lat = lat, long = long))
    }
}

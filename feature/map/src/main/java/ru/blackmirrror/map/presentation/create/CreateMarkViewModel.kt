package ru.blackmirrror.map.presentation.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.blackmirrror.core.exception.ImageNotUploaded
import ru.blackmirrror.core.state.ResultState
import ru.blackmirrror.core.state.ScreenState
import ru.blackmirrror.destinations.MapCreateMarkDestination.LAT_PARAM
import ru.blackmirrror.destinations.MapCreateMarkDestination.LON_PARAM
import ru.blackmirrror.map.data.MarkDto
import ru.blackmirrror.map.domain.Category
import ru.blackmirrror.map.domain.MapRepository
import ru.blackmirrror.map.domain.toMarkCategoryDto
import ru.blackmirrror.navigator.NavigatorResult
import ru.blackmirrror.navigator.TravelerNavigator
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CreateMarkViewModel @Inject constructor(
    private val travelerNavigator: TravelerNavigator,
    private val mapRepository: MapRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), TravelerNavigator by travelerNavigator {

    private val _state = MutableStateFlow<ScreenState<MarkDto>>(ScreenState.Loading())
    val state: StateFlow<ScreenState<MarkDto>> = _state.asStateFlow()

    private val file = MutableStateFlow<ScreenState<File>>(ScreenState.Loading())

    init {
        val lat = savedStateHandle.get<String>(LAT_PARAM)?.lowercase()!!
        val lon = savedStateHandle.get<String>(LON_PARAM)!!

        _state.value = ScreenState.Success(
            data = MarkDto(
                latitude = lat.toDouble(),
                longitude = lon.toDouble()
            )
        )
    }

    fun processEvent(event: CreateMarkEvent) {
        when (event) {
            CreateMarkEvent.Create -> create()
            is CreateMarkEvent.EditImageFile -> file.value.data = event.file
            is CreateMarkEvent.EditTitle -> editTitle(event.title)
            is CreateMarkEvent.EditDescription -> editDescription(event.description)
            is CreateMarkEvent.EditRating -> editRating(event.rating)
            is CreateMarkEvent.CategorySelected -> categorySelected(event.category)
        }
    }

    private fun create() {
        viewModelScope.launch {
            state.value.data?.let { mapRepository.createMark(it) }?.collect { mark ->
                when(mark) {
                    is ResultState.Loading -> {}
                    is ResultState.Success -> {
                        travelerNavigator.sendResult(
                            NavigatorResult.CreateMark
                        )
                        travelerNavigator.popBackStack()
                    }
                    is ResultState.Error -> {
                        when (mark.error) {
                            ImageNotUploaded -> {
                                travelerNavigator.sendResult(
                                    NavigatorResult.CreateMark
                                )
                                travelerNavigator.popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun editTitle(title: String) {
        val current = state.value.data ?: return
        _state.value = ScreenState.Success(
            data = current.copy(title = title)
        )
    }

    private fun editDescription(description: String) {
        val current = state.value.data ?: return
        _state.value = ScreenState.Success(
            data = current.copy(description = description)
        )
    }

    private fun editRating(rating: Int) {
        val current = state.value.data ?: return
        _state.value = ScreenState.Success(
            data = current.copy(averageRating = rating.toDouble())
        )
    }

    private fun categorySelected(category: Category) {
        var selectedCategories = state.value.data?.categories ?: emptySet()
        selectedCategories = if (selectedCategories.contains(category.toMarkCategoryDto())) {
            selectedCategories - category.toMarkCategoryDto()
        } else {
            selectedCategories + category.toMarkCategoryDto()
        }

        val current = state.value.data ?: return
        _state.value = ScreenState.Success(
            data = current.copy(categories = selectedCategories)
        )
    }
}
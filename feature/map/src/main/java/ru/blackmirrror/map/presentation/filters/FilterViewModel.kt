package ru.blackmirrror.map.presentation.filters

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.blackmirrror.map.domain.MapRepository
import ru.blackmirrror.navigator.NavigatorResult
import ru.blackmirrror.navigator.TravelerNavigator
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val travelerNavigator: TravelerNavigator,
    private val mapRepository: MapRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), TravelerNavigator by travelerNavigator {

    fun applyFilters() {
        travelerNavigator.sendResult(
            NavigatorResult.FiltersApplied(
                categories = null,
                radius = null,
                minRating = null
            )
        )
        travelerNavigator.popBackStack()
    }
}
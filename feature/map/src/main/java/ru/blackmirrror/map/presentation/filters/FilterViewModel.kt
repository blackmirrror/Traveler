package ru.blackmirrror.map.presentation.filters

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.blackmirrror.map.domain.Category
import ru.blackmirrror.navigator.NavigatorResult
import ru.blackmirrror.navigator.TravelerNavigator
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val travelerNavigator: TravelerNavigator
) : ViewModel(), TravelerNavigator by travelerNavigator {

    fun processEvent(event: FilterEvent) {
        when (event) {
            is FilterEvent.ApplyFilters -> applyFilters(
                categories = event.categories,
                radius = event.radius,
                minRating = event.minRating
            )
        }
    }

    private fun applyFilters(
        categories: List<Category>? = null,
        radius: Double? = null,
        minRating: Double = 1.0
    ) {
        travelerNavigator.sendResult(
            NavigatorResult.FiltersApplied(
                categories = categories,
                radius = radius,
                minRating = minRating
            )
        )
        travelerNavigator.popBackStack()
    }
}

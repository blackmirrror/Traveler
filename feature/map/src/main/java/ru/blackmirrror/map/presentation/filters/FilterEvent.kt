package ru.blackmirrror.map.presentation.filters

import ru.blackmirrror.map.domain.Category

sealed class FilterEvent {

    data class ApplyFilters(
        val categories: List<Category>? = null,
        val radius: Double? = null,
        val minRating: Double = 1.0
    ): FilterEvent()
}

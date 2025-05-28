package ru.blackmirrror.map.presentation

import ru.blackmirrror.map.domain.Category

sealed class MapEvent {
    data class LoadMarks(
        val category: Category ?= null,
        val radius: Int ?= null,
        val minRating: Int ?= null,
    ): MapEvent()
    object ToSearchFilters: MapEvent()
    object ToDescription: MapEvent()
}
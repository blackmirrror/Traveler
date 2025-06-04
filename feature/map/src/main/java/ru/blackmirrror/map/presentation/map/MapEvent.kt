package ru.blackmirrror.map.presentation.map

import ru.blackmirrror.map.domain.Category

sealed class MapEvent {

    data class LoadMarks(
        val minRating: Int ?= null,
        val radius: Int ?= null,
        val lat: Double ?= null,
        val lon: Double? = null,
        val categories: List<Category> ?= null
    ): MapEvent()

    data class ToSearchFilters(val lat: Double, val lon: Double): MapEvent()

    data class ToShowMark(val id: Long): MapEvent()

    data class ToCreateMark(val lat: Double, val long: Double): MapEvent()
}
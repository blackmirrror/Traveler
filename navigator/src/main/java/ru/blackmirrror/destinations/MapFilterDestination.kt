package ru.blackmirrror.destinations

import androidx.navigation.NamedNavArgument
import ru.blackmirrror.navigator.NavigationDestination

object MapFilterDestination : NavigationDestination {

    override fun route(): String = MAP_FILTERS_BOTTOM_NAV_ROUTE

    override val arguments: List<NamedNavArgument>
        get() = listOf()

    private const val MAP_FILTERS_ROUTE = "map_filter_marks"
    private const val MAP_FILTERS_BOTTOM_NAV_ROUTE = MAP_FILTERS_ROUTE

    fun createMapFilterRoute() = MAP_FILTERS_ROUTE
}

package ru.blackmirrror.destinations

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import ru.blackmirrror.navigator.NavigationDestination

object SearchFilterDestination : NavigationDestination {

    override fun route(): String = MAP_FILTERS_BOTTOM_NAV_ROUTE

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument(CURRENT_LAT_PARAM) { type = NavType.StringType },
            navArgument(CURRENT_LON_PARAM) { type = NavType.StringType }
        )

    const val CURRENT_LAT_PARAM = "current_lat"
    const val CURRENT_LON_PARAM = "current_lon"

    private const val MAP_FILTERS_ROUTE = "map_filter_marks"
    private const val MAP_FILTERS_BOTTOM_NAV_ROUTE = "$MAP_FILTERS_ROUTE/{$CURRENT_LAT_PARAM}&{$CURRENT_LON_PARAM}"

    fun createMapFilterRoute(lat: String, lon: String) = "$MAP_FILTERS_ROUTE/${lat}&${lon}"
}
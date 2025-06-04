package ru.blackmirrror.destinations

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import ru.blackmirrror.navigator.NavigationDestination

object MapCreateMarkDestination : NavigationDestination {

    override fun route(): String = MAP_CREATE_MARK_BOTTOM_NAV_ROUTE

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument(LAT_PARAM) { type = NavType.StringType },
            navArgument(LON_PARAM) { type = NavType.StringType }
        )

    const val LAT_PARAM = "lat"
    const val LON_PARAM = "long"

    private const val MAP_CREATE_MARK_ROUTE = "map_create_mark"
    private const val MAP_CREATE_MARK_BOTTOM_NAV_ROUTE = "$MAP_CREATE_MARK_ROUTE/{$LAT_PARAM}&{$LON_PARAM}"

    fun createMapCreateMarkRoute(lat: String = "", long: String = "") = "$MAP_CREATE_MARK_ROUTE/${lat}&${long}"
}
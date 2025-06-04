package ru.blackmirrror.destinations

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import ru.blackmirrror.navigator.NavigationDestination

object MapShowMarkDestination : NavigationDestination {

    override fun route(): String = MAP_SHOW_MARK_BOTTOM_NAV_ROUTE

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument(MARK_ID_PARAM) { type = NavType.LongType }
        )

    const val MARK_ID_PARAM = "mark_id"

    private const val MAP_SHOW_MARK_ROUTE = "map_show_mark"
    private const val MAP_SHOW_MARK_BOTTOM_NAV_ROUTE = "$MAP_SHOW_MARK_ROUTE/{$MARK_ID_PARAM}"

    fun createMapShowMarkRoute(markId: Long = 0L) = "$MAP_SHOW_MARK_ROUTE/${markId}"
}
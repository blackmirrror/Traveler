package ru.blackmirrror.map.ui

import ru.blackmirrror.navigator.NavigationDestination

object SearchFilterDestination : NavigationDestination {

    private const val ROUTE = "SearchFilterDestination"
    override fun route(): String = ROUTE
}
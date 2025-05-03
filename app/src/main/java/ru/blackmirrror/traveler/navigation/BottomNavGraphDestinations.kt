package ru.blackmirrror.traveler.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.blackmirrror.account.presentation.account.Account
import ru.blackmirrror.bottom_navigation.BottomNavigationEntry
import ru.blackmirrror.bottom_navigation.NewsRoute
import ru.blackmirrror.bottom_navigation.ChatsRoute
import ru.blackmirrror.bottom_navigation.MapRoute
import ru.blackmirrror.bottom_navigation.AccountRoute
import ru.blackmirrror.chats.ChatsScreen
import ru.blackmirrror.map.ui.Map
import ru.blackmirrror.news.News

private val destinationsBottomNav: Map<BottomNavigationEntry, @Composable (NavHostController) -> Unit> =
    mapOf(
        MapRoute to { Map() },
        NewsRoute to { News() },
        ChatsRoute to { ChatsScreen() },
        AccountRoute to { Account() },
    )


fun NavGraphBuilder.addBottomNavigationDestinations(navController: NavHostController) {
    destinationsBottomNav.forEach { entry ->
        val destination = entry.key
        composable(destination.route) {
            entry.value(navController)
        }
    }
}

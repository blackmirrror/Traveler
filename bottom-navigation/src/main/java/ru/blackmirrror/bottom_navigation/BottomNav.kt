package ru.blackmirrror.bottom_navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

object MapRoute : BottomNavigationEntry(MAP, R.string.bottom_nav_title_map)
object NewsRoute : BottomNavigationEntry(NEWS, R.string.bottom_nav_title_news)
object ChatsRoute : BottomNavigationEntry(CHATS, R.string.bottom_nav_title_chats)
object AccountRoute : BottomNavigationEntry(ACCOUNT, R.string.bottom_nav_title_account)

val bottomNavigationEntries =
    listOf(
        BottomNavigationUiEntry(
            MapRoute,
            Icons.Filled.Place
        ),
        BottomNavigationUiEntry(
            NewsRoute,
            Icons.Filled.DateRange
        ),
        BottomNavigationUiEntry(
            ChatsRoute,
            Icons.Filled.Email
        ),
        BottomNavigationUiEntry(
            AccountRoute,
            Icons.Filled.AccountCircle
        ),
    )

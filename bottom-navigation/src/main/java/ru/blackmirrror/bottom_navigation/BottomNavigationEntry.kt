package ru.blackmirrror.bottom_navigation

import androidx.annotation.StringRes

sealed class BottomNavigationEntry(val route: String, @StringRes val resourceID: Int) {

    companion object {
        const val MAP = "map"
        const val NEWS = "news"
        const val CHATS = "chats"
        const val ACCOUNT = "account"
    }
}

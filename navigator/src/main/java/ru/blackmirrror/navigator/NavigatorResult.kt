package ru.blackmirrror.navigator

sealed class NavigatorResult {
    data class FiltersApplied(
        val categories: List<Any>?,
        val radius: Int?,
        val minRating: Int?
    ) : NavigatorResult()

    object CreateMark: NavigatorResult()

    object UpdMess: NavigatorResult()
}
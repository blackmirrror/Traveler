package ru.blackmirrror.navigator

sealed class NavigatorResult {

    data class FiltersApplied(
        val categories: List<Any>?,
        val radius: Double?,
        val minRating: Double?
    ) : NavigatorResult()

    object CreateMark: NavigatorResult()

    object ChatUpdated: NavigatorResult()

    object UserUpdated: NavigatorResult()
}

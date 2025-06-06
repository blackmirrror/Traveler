package ru.blackmirrror.core.state

sealed class ScreenState<T>(var data: T? = null) {
    class Loading<T>(data: T? = null): ScreenState<T>(data)
    class Success<T>(data: T): ScreenState<T>(data)
    class Error<T>(val error: Exception, data: T? = null): ScreenState<T>(data)
}

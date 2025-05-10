package ru.blackmirrror.core.exception

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import ru.blackmirrror.core.state.ResultState

object ApiErrorHandler {
    fun <T> handleErrors(action: suspend () -> Flow<T>): Flow<T> {
        return flow {
            try {
                emitAll(action())
            } catch (e: Exception) {
                emit(ResultState.Error<T>(ServerError) as T)
            }
        }
    }
}
package ru.blackmirrror.account.domain

import kotlinx.coroutines.flow.Flow
import ru.blackmirrror.account.domain.model.User
import ru.blackmirrror.core.api.UserDto
import ru.blackmirrror.core.state.ResultState

interface AccountRepository {

    suspend fun getUser(): Flow<ResultState<User>>

    suspend fun updateUser(user: User): Flow<ResultState<User>>

    fun isAuthenticated(): Boolean

    suspend fun logout(): Result<Unit>

    fun isInternetConnection(): Boolean

    fun getUserFromPrefs(): UserDto?
}

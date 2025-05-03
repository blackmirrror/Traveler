package ru.blackmirrror.account.domain

import ru.blackmirrror.account.domain.model.User

interface AccountRepository {
    suspend fun getUser(): User
    suspend fun updateUser(): User

    fun isAuthenticated(): Boolean
    suspend fun logout(): Result<Unit>
    fun isInternetConnection(): Boolean
}
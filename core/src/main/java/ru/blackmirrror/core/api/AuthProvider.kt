package ru.blackmirrror.core.api

import ru.blackmirrror.core.model.UserDto

interface AuthProvider {
    //fun getAuthToken(): String?
    fun isUserAuthenticated(): Boolean
    suspend fun logout(): Result<Unit>
    //fun getCurrentUser(): UserDto?
}
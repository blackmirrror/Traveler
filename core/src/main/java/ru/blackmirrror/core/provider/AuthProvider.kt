package ru.blackmirrror.core.provider

interface AuthProvider {
    //fun getAuthToken(): String?
    fun isUserAuthenticated(): Boolean
    fun getPhoneNumber(): String?
    suspend fun logout(): Result<Unit>
    //fun getCurrentUser(): UserDto?
}
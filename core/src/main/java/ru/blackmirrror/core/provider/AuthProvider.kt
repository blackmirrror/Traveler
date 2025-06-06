package ru.blackmirrror.core.provider

interface AuthProvider {

    fun isUserAuthenticated(): Boolean

    fun getPhoneNumber(): String?

    suspend fun logout(): Result<Unit>
}

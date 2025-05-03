package ru.blackmirrror.auth.domain

interface AuthRepository {
    fun isAuthenticated(): Boolean
    suspend fun sendPhoneOtp(phone: String): Result<Unit>
    suspend fun verifyPhoneOtp(code: String): Result<Unit>
    //fun getAuthToken(): String?

    //suspend fun getCurrentUser(): User?
    suspend fun logout(): Result<Unit>
}
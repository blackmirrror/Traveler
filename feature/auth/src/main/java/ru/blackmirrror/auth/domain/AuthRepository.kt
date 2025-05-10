package ru.blackmirrror.auth.domain

import kotlinx.coroutines.flow.Flow
import ru.blackmirrror.core.state.ResultState

interface AuthRepository {
    fun isAuthenticated(): Boolean
    suspend fun sendPhoneOtp(phone: String, isUpdate: Boolean = false): Flow<ResultState<Unit>>
    suspend fun verifyPhoneOtp(code: String, isUpdate: Boolean = false): Flow<ResultState<Unit>>
    //fun getAuthToken(): String?

    //suspend fun getCurrentUser(): User?
    fun getPhoneNumber(): String?
    suspend fun logout(): Result<Unit>
}

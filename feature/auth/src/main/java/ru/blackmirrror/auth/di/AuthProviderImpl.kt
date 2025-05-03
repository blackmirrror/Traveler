package ru.blackmirrror.auth.di

import ru.blackmirrror.auth.domain.AuthRepository
import ru.blackmirrror.core.api.AuthProvider
import ru.blackmirrror.core.model.UserDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthProviderImpl @Inject constructor(
    private val authRepository: AuthRepository
) : AuthProvider {

    override fun isUserAuthenticated(): Boolean {
        return authRepository.isAuthenticated()
    }

    override suspend fun logout(): Result<Unit> {
        return authRepository.logout()
    }

//    override fun getCurrentUser(): UserDto? {
//        authRepository.getCu
//    }

    //override fun getAuthToken(): String? = authRepository.getAuthToken()
}

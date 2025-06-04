package ru.blackmirrror.auth.di

import ru.blackmirrror.auth.domain.AuthRepository
import ru.blackmirrror.core.provider.AuthProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthProviderImpl @Inject constructor(
    private val authRepository: AuthRepository
) : AuthProvider {

    override fun isUserAuthenticated(): Boolean {
        return authRepository.isAuthenticated()
    }

    override fun getPhoneNumber(): String? {
        return authRepository.getPhoneNumber()
    }

    override suspend fun logout(): Result<Unit> {
        return authRepository.logout()
    }

    override fun getUserId(): Long {
        return 4
    }

//    override fun getCurrentUser(): UserDto? {
//        authRepository.getCu
//    }

    //override fun getAuthToken(): String? = authRepository.getAuthToken()
}

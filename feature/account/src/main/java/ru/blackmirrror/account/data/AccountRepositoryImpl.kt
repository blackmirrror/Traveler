package ru.blackmirrror.account.data

import ru.blackmirrror.account.domain.AccountRepository
import ru.blackmirrror.account.domain.model.User
import ru.blackmirrror.account.presentation.toDate
import ru.blackmirrror.core.api.AuthProvider
import ru.blackmirrror.core.api.NetworkProvider
import javax.inject.Inject

internal class AccountRepositoryImpl @Inject constructor(
    private val authProvider: AuthProvider,
    private val networkProvider: NetworkProvider,
    private val accountSharedPrefs: AccountSharedPrefs
): AccountRepository {
    override suspend fun getUser(): User {
        if (networkProvider.isInternetConnection()) {
            return User(
                firstName = "Анастасия",
                lastName = "Скворцова",
                phone = "9009999999",
                email = "anastsas@gmail.com",
                birthDate = null,
                photoUrl = "https://images.unsplash.com/photo-1505968409348-bd000797c92e?q=80&w=1771&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            )
        }
        else {
            return User(
                firstName = accountSharedPrefs.firstName,
                lastName = accountSharedPrefs.lastName,
                phone = accountSharedPrefs.phoneNumber!!,
                email = accountSharedPrefs.email,
                birthDate = accountSharedPrefs.birthDate.toDate(),
                photoUrl = null
            )
        }
    }

    override suspend fun updateUser(): User {
        return User(
            firstName = "Вера",
            lastName = "Скворцова",
            phone = "9009999999",
            email = "anastsas@gmail.com",
            birthDate = null,
            photoUrl = "https://images.unsplash.com/photo-1505968409348-bd000797c92e?q=80&w=1771&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
        )
    }

    override fun isAuthenticated(): Boolean {
        return authProvider.isUserAuthenticated()
    }

    override suspend fun logout(): Result<Unit> {
        return authProvider.logout()
    }

}
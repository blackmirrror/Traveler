package ru.blackmirrror.account.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.blackmirrror.account.data.api.AccountApiService
import ru.blackmirrror.account.domain.AccountRepository
import ru.blackmirrror.account.domain.model.User
import ru.blackmirrror.account.domain.model.toDomain
import ru.blackmirrror.account.domain.model.toDto
import ru.blackmirrror.account.presentation.toDate
import ru.blackmirrror.core.exception.ApiErrorHandler
import ru.blackmirrror.core.exception.EmptyData
import ru.blackmirrror.core.exception.NoInternet
import ru.blackmirrror.core.exception.ServerError
import ru.blackmirrror.core.provider.AuthProvider
import ru.blackmirrror.core.provider.NetworkProvider
import ru.blackmirrror.core.state.ResultState
import javax.inject.Inject

internal class AccountRepositoryImpl @Inject constructor(
    private val authProvider: AuthProvider,
    private val networkProvider: NetworkProvider,
    private val apiService: AccountApiService,
    private val accountSharedPrefs: AccountSharedPrefs
): AccountRepository {

    override fun isInternetConnection(): Boolean {
        return networkProvider.isInternetConnection()
    }

    override suspend fun getUser(): Flow<ResultState<User>> {
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading(accountSharedPrefs.getUserFromPrefs()))
                if (isInternetConnection()) {
                    val res = apiService.getUserByPhone(authProvider.getPhoneNumber()!!)
                    if (res.isSuccessful) {
                        val user = res.body()
                        if (user != null) {
                            val userDomain = user.toDomain()
                            emit(ResultState.Success(userDomain))
                            accountSharedPrefs.saveUserToPrefs(userDomain)
                        }
                        else {
                            emit(ResultState.Error(EmptyData))
                        }
                    } else
                        emit(ResultState.Error(ServerError, accountSharedPrefs.getUserFromPrefs()))
                } else {
                    emit(ResultState.Error(NoInternet, accountSharedPrefs.getUserFromPrefs()))
                }
            }
        }
    }

    override suspend fun updateUser(user: User): Flow<ResultState<User>> {
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading(accountSharedPrefs.getUserFromPrefs()))
                if (isInternetConnection()) {
                    val oldUser = apiService.getUserByPhone(authProvider.getPhoneNumber()!!)
                    if (oldUser.isSuccessful) {
                        val updatedUser = user.copy(
                            id = oldUser.body()!!.id,
                            phone = oldUser.body()!!.phone,
                            email = oldUser.body()!!.email
                        )

                        val res = apiService.updateUser(updatedUser.id!!, updatedUser.toDto())
                        if (res.isSuccessful) {
                            val userDomain = res.body()!!.toDomain()
                            emit(ResultState.Success(userDomain))
                            accountSharedPrefs.saveUserToPrefs(userDomain)
                        }
                        else {
                            emit(ResultState.Error(EmptyData))
                        }
                    } else
                        emit(ResultState.Error(ServerError, accountSharedPrefs.getUserFromPrefs()))
                } else {
                    emit(ResultState.Error(NoInternet, accountSharedPrefs.getUserFromPrefs()))
                }
                getUser()
            }
        }
    }

    override fun isAuthenticated(): Boolean {
        return authProvider.isUserAuthenticated()
    }

    override suspend fun logout(): Result<Unit> {
        val logoutResult = authProvider.logout()
        if (logoutResult.isSuccess) {
            accountSharedPrefs.clearAll()
        }
        return logoutResult
    }
}
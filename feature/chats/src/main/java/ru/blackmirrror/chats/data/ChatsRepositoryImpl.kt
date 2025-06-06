package ru.blackmirrror.chats.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.blackmirrror.chats.domain.ChatsRepository
import ru.blackmirrror.core.exception.ApiErrorHandler
import ru.blackmirrror.core.exception.EmptyData
import ru.blackmirrror.core.exception.NoInternet
import ru.blackmirrror.core.exception.ServerError
import ru.blackmirrror.core.provider.AccountProvider
import ru.blackmirrror.core.provider.AuthProvider
import ru.blackmirrror.core.provider.NetworkProvider
import ru.blackmirrror.core.state.ResultState
import javax.inject.Inject

class ChatsRepositoryImpl @Inject constructor(
    private val chatsApiService: ChatsApiService,
    private val messageApiService: MessageApiService,
    private val accountProvider: AccountProvider,
    private val authProvider: AuthProvider,
    private val networkProvider: NetworkProvider
) : ChatsRepository {

    override fun isAuthenticated(): Boolean {
        return authProvider.isUserAuthenticated()
    }

    override suspend fun getChats(): Flow<ResultState<List<ChatDto>>> {
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                if (isInternetConnection()) {
                    val res = chatsApiService.getChats(getUserId())
                    if (res.isSuccessful) {
                        if (res.body() != null)
                            emit(ResultState.Success(res.body()!!.map { getChat(it.id) }
                                .filterNotNull()))
                        else
                            emit(ResultState.Error(EmptyData))
                    }
                    emit(ResultState.Error(ServerError))
                } else {
                    emit(ResultState.Error(NoInternet))
                }
            }
        }
    }

    override suspend fun getMessages(chatId: Long): Flow<ResultState<List<MessageDto>>> {
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                if (isInternetConnection()) {
                    val res = messageApiService.getMessages(chatId)
                    if (res.isSuccessful) {
                        if (res.body() != null)
                            emit(ResultState.Success(res.body()!!))
                        else
                            emit(ResultState.Error(EmptyData))
                    }
                    emit(ResultState.Error(ServerError))
                } else {
                    emit(ResultState.Error(NoInternet))
                }
            }
        }
    }

    private suspend fun getChat(chatId: Long): ChatDto? {
        if (isInternetConnection()) {
            val res = chatsApiService.getChat(chatId)
            if (res.isSuccessful) {
                return res.body()
            }
        }
        return null
    }

    private fun isInternetConnection(): Boolean {
        return networkProvider.isInternetConnection()
    }

    override fun getUserId(): Long {
        return accountProvider.getUser()?.id ?: 0
    }
}

package ru.blackmirrror.chats.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.blackmirrror.chats.data.ChatWebSocketRepository
import ru.blackmirrror.chats.data.ChatsApiService
import ru.blackmirrror.chats.data.ChatsRepositoryImpl
import ru.blackmirrror.chats.data.MessageApiService
import ru.blackmirrror.chats.domain.ChatsRepository
import ru.blackmirrror.core.di.BackendRetrofit
import ru.blackmirrror.core.provider.AccountProvider
import ru.blackmirrror.core.provider.AuthProvider
import ru.blackmirrror.core.provider.NetworkProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ChatModule {

    @Provides
    @Singleton
    fun provideChatsApiService(@BackendRetrofit retrofit: Retrofit): ChatsApiService {
        return retrofit.create(ChatsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMessageApiService(@BackendRetrofit retrofit: Retrofit): MessageApiService {
        return retrofit.create(MessageApiService::class.java)
    }

    @Provides
    fun provideChatsRepository(
        chatsApiService: ChatsApiService,
        messageApiService: MessageApiService,
        accountProvider: AccountProvider,
        authProvider: AuthProvider,
        networkProvider: NetworkProvider
    ): ChatsRepository {
        return ChatsRepositoryImpl(
            chatsApiService = chatsApiService,
            messageApiService = messageApiService,
            accountProvider = accountProvider,
            authProvider = authProvider,
            networkProvider = networkProvider
        )
    }

    @Provides
    fun provideChatWebSocketRepository(
        authProvider: AuthProvider,
        accountProvider: AccountProvider,
        networkProvider: NetworkProvider
    ): ChatWebSocketRepository {
        return ChatWebSocketRepository(
            authProvider = authProvider,
            accountProvider = accountProvider,
            networkProvider = networkProvider
        )
    }
}

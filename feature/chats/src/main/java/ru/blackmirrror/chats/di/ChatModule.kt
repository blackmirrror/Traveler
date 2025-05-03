package ru.blackmirrror.chats.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.blackmirrror.chats.data.ChatsRepositoryImpl
import ru.blackmirrror.chats.domain.ChatsRepository
import ru.blackmirrror.core.api.AuthProvider
import ru.blackmirrror.core.api.NetworkProvider

@Module
@InstallIn(SingletonComponent::class)
class ChatModule {

    @Provides
    fun provideChatsRepository(
        authProvider: AuthProvider,
        networkProvider: NetworkProvider
    ): ChatsRepository {
        return ChatsRepositoryImpl(
            authProvider = authProvider,
            networkProvider = networkProvider
        )
    }
}

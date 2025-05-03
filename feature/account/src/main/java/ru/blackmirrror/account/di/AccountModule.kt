package ru.blackmirrror.account.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.blackmirrror.account.data.AccountRepositoryImpl
import ru.blackmirrror.account.data.AccountSharedPrefs
import ru.blackmirrror.account.domain.AccountRepository
import ru.blackmirrror.core.api.AuthProvider
import ru.blackmirrror.core.api.NetworkProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AccountModule {

    @Provides
    @Singleton
    fun provideAuthSharedPrefs(@ApplicationContext context: Context): AccountSharedPrefs {
        return AccountSharedPrefs(context)
    }

    @Provides
    fun provideAccountRepository(
        authProvider: AuthProvider,
        networkProvider: NetworkProvider,
        accountSharedPrefs: AccountSharedPrefs
    ): AccountRepository {
        return AccountRepositoryImpl(
            authProvider = authProvider,
            networkProvider = networkProvider,
            accountSharedPrefs = accountSharedPrefs
        )
    }
}
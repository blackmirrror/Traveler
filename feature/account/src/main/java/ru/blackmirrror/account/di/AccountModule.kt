package ru.blackmirrror.account.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.blackmirrror.account.data.AccountRepositoryImpl
import ru.blackmirrror.account.data.AccountSharedPrefs
import ru.blackmirrror.account.data.api.AccountApiService
import ru.blackmirrror.account.domain.AccountRepository
import ru.blackmirrror.core.di.BackendRetrofit
import ru.blackmirrror.core.provider.AccountProvider
import ru.blackmirrror.core.provider.AuthProvider
import ru.blackmirrror.core.provider.NetworkProvider
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
    @Singleton
    fun provideAccountApiService(@BackendRetrofit retrofit: Retrofit): AccountApiService {
        return retrofit.create(AccountApiService::class.java)
    }

    @Provides
    fun provideAccountRepository(
        authProvider: AuthProvider,
        networkProvider: NetworkProvider,
        apiService: AccountApiService,
        accountSharedPrefs: AccountSharedPrefs
    ): AccountRepository {
        return AccountRepositoryImpl(
            authProvider = authProvider,
            networkProvider = networkProvider,
            apiService = apiService,
            accountSharedPrefs = accountSharedPrefs
        )
    }

    @Provides
    @Singleton
    fun provideAccountProvider(accountProviderImpl: AccountProviderImpl): AccountProvider {
        return accountProviderImpl
    }
}

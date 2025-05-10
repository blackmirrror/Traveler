package ru.blackmirrror.auth.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import ru.blackmirrror.auth.data.AuthRepositoryImpl
import ru.blackmirrror.auth.domain.AuthRepository
import ru.blackmirrror.core.provider.AuthProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.blackmirrror.auth.data.AuthSharedPrefs
import ru.blackmirrror.auth.data.api.AuthApiService
import ru.blackmirrror.core.di.BackendRetrofit
import ru.blackmirrror.core.provider.NetworkProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthSharedPrefs(@ApplicationContext context: Context): AuthSharedPrefs {
        return AuthSharedPrefs(context)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthApiService(@BackendRetrofit retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        networkProvider: NetworkProvider,
        firebaseAuth: FirebaseAuth,
        authApiService: AuthApiService,
        authSharedPrefs: AuthSharedPrefs
    ): AuthRepository {
        return AuthRepositoryImpl(
            networkProvider,
            firebaseAuth,
            authApiService,
            authSharedPrefs
        )
    }

    @Provides
    @Singleton
    fun provideAuthProvider(authProviderImpl: AuthProviderImpl): AuthProvider {
        return authProviderImpl
    }
}

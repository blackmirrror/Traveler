package ru.blackmirrror.auth.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import ru.blackmirrror.auth.data.AuthRepositoryImpl
import ru.blackmirrror.auth.domain.AuthRepository
import ru.blackmirrror.core.api.AuthProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.blackmirrror.auth.data.AuthSharedPrefs
import ru.blackmirrror.core.api.ActivityProvider
import ru.blackmirrror.core.api.NetworkProvider
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
    fun provideAuthRepository(
//        activityProvider: ActivityProvider,
        networkProvider: NetworkProvider,
        firebaseAuth: FirebaseAuth,
        authSharedPrefs: AuthSharedPrefs
    ): AuthRepository {
        return AuthRepositoryImpl(
//            activityProvider,
            networkProvider,
            firebaseAuth,
            authSharedPrefs
        )
    }

    @Provides
    @Singleton
    fun provideAuthProvider(authProviderImpl: AuthProviderImpl): AuthProvider {
        return authProviderImpl
    }
}

package ru.blackmirrror.map.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.blackmirrror.core.di.BackendRetrofit
import ru.blackmirrror.core.provider.AuthProvider
import ru.blackmirrror.core.provider.NetworkProvider
import ru.blackmirrror.map.data.MapApiService
import ru.blackmirrror.map.data.MapRepositoryImpl
import ru.blackmirrror.map.domain.MapRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MapModule {

    @Provides
    @Singleton
    fun provideMapApiService(@BackendRetrofit retrofit: Retrofit): MapApiService {
        return retrofit.create(MapApiService::class.java)
    }

    @Provides
    fun provideMapRepository(
        authProvider: AuthProvider,
        networkProvider: NetworkProvider,
        apiService: MapApiService
    ): MapRepository {
        return MapRepositoryImpl(
            authProvider = authProvider,
            networkProvider = networkProvider,
            apiService = apiService
        )
    }
}
package ru.blackmirrror.map.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.blackmirrror.core.di.BackendRetrofit
import ru.blackmirrror.core.image_storage.FileRepository
import ru.blackmirrror.core.provider.AccountProvider
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
        accountProvider: AccountProvider,
        networkProvider: NetworkProvider,
        apiService: MapApiService,
        fileRepository: FileRepository
    ): MapRepository {
        return MapRepositoryImpl(
            accountProvider = accountProvider,
            networkProvider = networkProvider,
            apiService = apiService,
            fileRepository = fileRepository
        )
    }
}

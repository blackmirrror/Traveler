package ru.blackmirrror.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.blackmirrror.core.api.BackendApiFactory
import ru.blackmirrror.core.image_storage.FileRepository
import ru.blackmirrror.core.image_storage.FileRepositoryImpl
import ru.blackmirrror.core.image_storage.FileStorageApiFactory
import ru.blackmirrror.core.image_storage.FileStorageApiService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoreModule {

    @Provides
    @Singleton
    @BackendRetrofit
    fun provideRetrofit(): Retrofit {
        return BackendApiFactory.createBackendRetrofit()
    }

    @Provides
    @Singleton
    @FileStorageRetrofit
    fun provideFileStorageRetrofit(): Retrofit {
        return FileStorageApiFactory.createFileStorageRetrofit()
    }

    @Provides
    @Singleton
    fun provideFileStorageApiService(@FileStorageRetrofit retrofit: Retrofit): FileStorageApiService {
        return retrofit.create(FileStorageApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideFileStorageRepository(
        apiService: FileStorageApiService
    ): FileRepository {
        return FileRepositoryImpl(apiService)
    }
}

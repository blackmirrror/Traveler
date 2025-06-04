package ru.blackmirrror.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.blackmirrror.core.di.BackendRetrofit
import ru.blackmirrror.core.image_storage.FileRepository
import ru.blackmirrror.core.provider.AuthProvider
import ru.blackmirrror.core.provider.NetworkProvider
import ru.blackmirrror.data.PostApiService
import ru.blackmirrror.data.PostRepositoryImpl
import ru.blackmirrror.domain.PostRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PostModule {

    @Provides
    @Singleton
    fun provideMapApiService(@BackendRetrofit retrofit: Retrofit): PostApiService {
        return retrofit.create(PostApiService::class.java)
    }

    @Provides
    fun providePostRepository(
        authProvider: AuthProvider,
        networkProvider: NetworkProvider,
        apiService: PostApiService,
        fileRepository: FileRepository
    ): PostRepository {
        return PostRepositoryImpl(
            authProvider = authProvider,
            networkProvider = networkProvider,
            apiService = apiService,
            fileRepository = fileRepository
        )
    }
}
package ru.blackmirrror.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.blackmirrror.core.exception.ApiErrorHandler
import ru.blackmirrror.core.exception.EmptyData
import ru.blackmirrror.core.exception.ImageNotUploaded
import ru.blackmirrror.core.exception.NoInternet
import ru.blackmirrror.core.exception.ServerError
import ru.blackmirrror.core.image_storage.FileRepository
import ru.blackmirrror.core.provider.AccountProvider
import ru.blackmirrror.core.provider.NetworkProvider
import ru.blackmirrror.core.state.ResultState
import ru.blackmirrror.domain.PostRepository
import java.io.File
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val accountProvider: AccountProvider,
    private val networkProvider: NetworkProvider,
    private val apiService: PostApiService,
    private val fileRepository: FileRepository
): PostRepository {

    override fun isInternetConnection(): Boolean {
        return networkProvider.isInternetConnection()
    }

    private fun getUserId(): Long {
        return accountProvider.getUser()?.id ?: 0
    }

    override suspend fun getAllPosts(): Flow<ResultState<List<PostDto>>> {
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                if (isInternetConnection()) {
                    val res = apiService.getAllPosts()
                    if (res.isSuccessful) {
                        if (res.body() != null)
                            emit(ResultState.Success(res.body()!!))
                        else
                            emit(ResultState.Error(EmptyData))
                    }
                    emit(ResultState.Error(ServerError))
                } else {
                    emit(ResultState.Error(NoInternet))
                }
            }
        }
    }

    override suspend fun getPost(postId: Long): Flow<ResultState<PostDto>> {
        val userId = getUserId()
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                if (isInternetConnection()) {
                    val res = apiService.getPost(postId, userId)
                    if (res.isSuccessful) {
                        if (res.body() != null)
                            emit(ResultState.Success(res.body()!!))
                        else
                            emit(ResultState.Error(EmptyData))
                    }
                    emit(ResultState.Error(ServerError))
                } else {
                    emit(ResultState.Error(NoInternet))
                }
            }
        }
    }

    override suspend fun createPost(post: PostDto, file: File?): Flow<ResultState<PostDto>> {
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                if (isInternetConnection()) {
                    val res = apiService.createPost(post)
                    if (res.isSuccessful) {
                        if (res.body() != null) {
                            if (file != null) {
                                fileRepository.uploadImage(file).collect { result ->
                                    when (result) {
                                        is ResultState.Loading -> {}
                                        is ResultState.Success -> {
                                            val imageUrl = fileRepository.getImageUrl(file.name)
                                            res.body()!!.imageUrl = imageUrl
                                            emit(ResultState.Success(res.body()!!))
                                        }
                                        is ResultState.Error -> {
                                            Log.d("DDD", "createPost: ")
                                            res.body()!!.imageUrl = null
                                            emit(ResultState.Error(ImageNotUploaded, res.body()!!))
                                        }
                                    }
                                }
                            }
                        }
                        else
                            emit(ResultState.Error(EmptyData))
                    }
                    emit(ResultState.Error(ServerError))
                } else {
                    emit(ResultState.Error(NoInternet))
                }
            }
        }
    }
}

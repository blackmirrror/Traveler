package ru.blackmirrror.map.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
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
import ru.blackmirrror.database.dao.MarkDao
import ru.blackmirrror.map.domain.MapRepository
import java.io.File
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val accountProvider: AccountProvider,
    private val networkProvider: NetworkProvider,
    private val apiService: MapApiService,
    private val markDao: MarkDao,
    private val fileRepository: FileRepository
) : MapRepository {

    override fun isInternetConnection(): Boolean {
        return networkProvider.isInternetConnection()
    }

    private fun getUserId(): Long {
        return accountProvider.getUser()?.id ?: 0
    }

    override fun getAllMarks(
        minRating: Double?,
        distance: Double?,
        lat: Double?,
        lon: Double?,
        categories: List<MarkCategoryDto>?
    ): Flow<ResultState<List<MarkLatLngDto>>> {
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                if (isInternetConnection()) {
                    val res = apiService.getAllMarks(
                        minRating, distance, lat, lon, categories
                    )
                    if (res.isSuccessful) {
                        if (res.body() != null)
                            emit(ResultState.Success(res.body()!!))
                        else
                            emit(ResultState.Error(EmptyData))
                    }
                    emitAll(getAllMarksLocal(ServerError))
                } else {
                    emitAll(getAllMarksLocal(NoInternet))
                }
            }
        }
    }

    private fun getAllMarksLocal(error: Exception? = null): Flow<ResultState<List<MarkLatLngDto>>> =
        flow {
            val result = markDao.getAllMarks()
            if (result.isEmpty()) {
                emit(ResultState.Error(EmptyData))
            } else if (error != null) {
                emit(ResultState.Error(error, result.map { it.toMarkLatLngDto() }))
            }
        }

    override suspend fun getMark(markId: Long): Flow<ResultState<MarkDto>> {
        val userId = getUserId()
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                if (isInternetConnection()) {
                    val res = apiService.getMark(markId, userId)
                    if (res.isSuccessful) {
                        if (res.body() != null)
                            emit(ResultState.Success(res.body()!!))
                        else
                            emit(ResultState.Error(EmptyData))
                    }
                    emitAll(getMarkLocal(markId, ServerError))
                } else {
                    emitAll(getMarkLocal(markId, NoInternet))
                }
            }
        }
    }

    private fun getMarkLocal(markId: Long, error: Exception? = null): Flow<ResultState<MarkDto>> =
        flow {
            val result = markDao.getMarkById(markId)
            if (result == null) {
                emit(ResultState.Error(EmptyData))
            } else if (error != null) {
                emit(ResultState.Error(error, result.toMarkDto()))
            }
        }

    override suspend fun createMark(mark: MarkDto, file: File?): Flow<ResultState<MarkDto>> {
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                if (isInternetConnection()) {
                    val res = apiService.createMark(mark)
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
                                            Log.d("DDD", "createMark: ")
                                            res.body()!!.imageUrl = null
                                            emit(ResultState.Error(ImageNotUploaded, res.body()!!))
                                        }
                                    }
                                }
                            }
                        } else
                            emit(ResultState.Error(EmptyData))
                    }
                    emit(ResultState.Error(ServerError))
                } else {
                    emit(ResultState.Error(NoInternet))
                }
            }
        }
    }

    override suspend fun updateMark(id: Long, mark: MarkDto): Flow<ResultState<MarkDto>> {
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                if (isInternetConnection()) {
                    val res = apiService.updateMark(id, mark)
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

    override suspend fun deleteMark(id: Long): Flow<ResultState<Unit>> {
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                if (isInternetConnection()) {
                    val res = apiService.deleteMark(id)
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

    override suspend fun likeMark(markId: Long, userId: Long): Flow<ResultState<Unit>> {
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                if (isInternetConnection()) {
                    val res = apiService.likeMark(markId, userId)
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

    override suspend fun unlikeMark(markId: Long, userId: Long): Flow<ResultState<Unit>> {
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                if (isInternetConnection()) {
                    val res = apiService.unlikeMark(markId, userId)
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

    override suspend fun reviewMark(
        markId: Long,
        userId: Long,
        review: MarkReviewDto
    ): Flow<ResultState<MarkReviewDto>> {
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                if (isInternetConnection()) {
                    val res = apiService.reviewMark(markId, userId, review)
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

    override suspend fun getMarkReviews(markId: Long): Flow<ResultState<List<MarkReviewDto>>> {
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                if (isInternetConnection()) {
                    val res = apiService.getMarkReviews(markId)
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
}

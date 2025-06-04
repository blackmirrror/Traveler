package ru.blackmirrror.map.domain

import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import ru.blackmirrror.core.state.ResultState
import ru.blackmirrror.map.data.MarkCategoryDto
import ru.blackmirrror.map.data.MarkDto
import ru.blackmirrror.map.data.MarkLatLngDto
import ru.blackmirrror.map.data.MarkReviewDto
import ru.blackmirrror.map.domain.model.Mark
import java.io.File

interface MapRepository {

    fun isInternetConnection(): Boolean

    fun getAllMarks(
        minRating: Double? = null,
        distance: Double? = null,
        lat: Double? = null,
        lon: Double? = null,
        categories: List<MarkCategoryDto>? = null
    ): Flow<ResultState<List<MarkLatLngDto>>>

    suspend fun getMark(markId: Long): Flow<ResultState<MarkDto>>

    suspend fun createMark(mark: MarkDto, file: File? = null): Flow<ResultState<MarkDto>>

    suspend fun updateMark(id: Long, mark: MarkDto): Flow<ResultState<MarkDto>>

    suspend fun deleteMark(id: Long): Flow<ResultState<Unit>>

    suspend fun likeMark(markId: Long, userId: Long): Flow<ResultState<Unit>>

    suspend fun unlikeMark(markId: Long, userId: Long): Flow<ResultState<Unit>>

    suspend fun reviewMark(
        markId: Long,
        userId: Long,
        review: MarkReviewDto
    ): Flow<ResultState<MarkReviewDto>>

    suspend fun getMarkReviews(markId: Long): Flow<ResultState<List<MarkReviewDto>>>
}

package ru.blackmirrror.map.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MapApiService {

    @GET("/marks")
    suspend fun getAllMarks(
        @Query("minRating") minRating: Double? = null,
        @Query("distance") distance: Double? = null,
        @Query("lat") lat: Double? = null,
        @Query("lon") lon: Double? = null,
        @Query("categories") categories: List<MarkCategoryDto>? = null
    ): Response<List<MarkLatLngDto>>

    @GET("/marks/{markId}")
    suspend fun getMark(@Path("markId") markId: Long, @Query("userId") userId: Long): Response<MarkDto>

    @POST("/marks")
    suspend fun createMark(@Body mark: MarkDto): Response<MarkDto>

    @PUT("marks/{id}")
    suspend fun updateMark(@Path("id") id: Long, @Body mark: MarkDto): Response<MarkDto>

    @DELETE("/marks/{id}")
    suspend fun deleteMark(@Path("id") id: Long): Response<Unit>

    @POST("/marks/{markId}/like")
    suspend fun likeMark(@Path("markId") markId: Long, @Query("userId") userId: Long): Response<Unit>

    @DELETE("/marks/{markId}/unlike")
    suspend fun unlikeMark(@Path("markId") markId: Long, @Query("userId") userId: Long): Response<Unit>

    @POST("/marks/{markId}/review")
    suspend fun reviewMark(
        @Path("markId") markId: Long,
        @Query("userId") userId: Long,
        @Body review: MarkReviewDto
    ): Response<MarkReviewDto>

    @GET("/marks/{markId}/reviews")
    suspend fun getMarkReviews(@Path("markId") markId: Long): Response<List<MarkReviewDto>>
}
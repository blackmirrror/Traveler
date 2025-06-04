package ru.blackmirrror.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PostApiService {

    @GET("/posts")
    fun getAllMarks(
    ): Response<List<PostDto>>

    @GET("/posts")
    suspend fun getAllPosts(): Response<List<PostDto>>

    @GET("/marks/{markId}")
    suspend fun getMark(@Path("markId") markId: Long, @Query("userId") userId: Long): Response<PostDto>

    @POST("/marks")
    suspend fun createPost(@Body mark: PostDto): Response<PostDto>
}
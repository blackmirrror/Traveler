package ru.blackmirrror.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PostApiService {

    @GET("/posts")
    suspend fun getAllPosts(): Response<List<PostDto>>

    @GET("/posts/{postId}")
    suspend fun getPost(@Path("postId") postId: Long, @Query("userId") userId: Long): Response<PostDto>

    @POST("/posts")
    suspend fun createPost(@Body post: PostDto): Response<PostDto>
}

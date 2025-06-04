package ru.blackmirrror.domain

import kotlinx.coroutines.flow.Flow
import ru.blackmirrror.core.state.ResultState
import ru.blackmirrror.data.PostDto
import java.io.File

interface PostRepository {
    fun isInternetConnection(): Boolean
    suspend fun getAllPosts(): Flow<ResultState<List<PostDto>>>
    suspend fun getPost(markId: Long): Flow<ResultState<PostDto>>
    suspend fun createPost(post: PostDto, file: File?): Flow<ResultState<PostDto>>
}
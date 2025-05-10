package ru.blackmirrror.core.image_storage

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface FileStorageApiService {

    @PUT("object/{bucket}/{filename}")
    suspend fun uploadFile(
        @Path("bucket") bucket: String,
        @Path("filename") filename: String,
        @Body file: RequestBody
    ): Response<ResponseBody>

    @GET("object/public/{bucket}/{filename}")
    fun getFileUrl(
        @Path("bucket") bucket: String,
        @Path("filename") filename: String
    ): Call<ResponseBody>
}

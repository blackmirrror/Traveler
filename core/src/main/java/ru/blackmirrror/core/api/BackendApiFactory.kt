package ru.blackmirrror.core.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.blackmirrror.core.BuildConfig
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

object BackendApiFactory {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(makeInterceptor())
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private fun makeInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = originalRequest.newBuilder().build()
            try {
                chain.proceed(newRequest)
            } catch (e: SocketTimeoutException) {
                throw IOException("SocketTimeoutException")
            }
        }
    }

    fun createBackendRetrofit(): Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BACKEND_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}

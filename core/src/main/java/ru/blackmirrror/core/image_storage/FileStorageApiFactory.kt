package ru.blackmirrror.core.image_storage

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.blackmirrror.core.BuildConfig
import java.util.concurrent.TimeUnit

object FileStorageApiFactory {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(makeInterceptor())
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private fun makeInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${BuildConfig.SUPABASE_API_KEY}")
                .build()
            chain.proceed(request)
        }
    }

    fun createFileStorageRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.SUPABASE_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
}

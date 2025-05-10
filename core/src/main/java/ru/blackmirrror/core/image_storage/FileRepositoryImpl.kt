package ru.blackmirrror.core.image_storage

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import ru.blackmirrror.core.BuildConfig
import ru.blackmirrror.core.exception.NoInternet
import ru.blackmirrror.core.exception.ServerError
import ru.blackmirrror.core.state.ResultState
import java.io.File
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
    private val service: FileStorageApiService
): FileRepository {
    override fun uploadImage(file: File): Flow<ResultState<Unit>> = flow {
        emit(ResultState.Loading())
        val body = file.asRequestBody("image/*".toMediaTypeOrNull())
        val response = service.uploadFile(BuildConfig.SUPABASE_BUCKET_NAME, file.name, body)
        if (response.isSuccessful) {
            emit(ResultState.Success(Unit))
        } else {
            emit(ResultState.Error(ServerError))
        }
    }.catch { e ->
        e.printStackTrace()
        emit(ResultState.Error(NoInternet))
    }

    override fun getImageUrl(fileName: String): String {
        return "${BuildConfig.SUPABASE_BASE_URL}object/public/${BuildConfig.SUPABASE_BUCKET_NAME}//$fileName"
    }
}


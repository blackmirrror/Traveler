package ru.blackmirrror.core.image_storage

import kotlinx.coroutines.flow.Flow
import ru.blackmirrror.core.state.ResultState
import java.io.File

interface FileRepository {

    fun uploadImage(file: File): Flow<ResultState<Unit>>

    fun getImageUrl(fileName: String): String
}

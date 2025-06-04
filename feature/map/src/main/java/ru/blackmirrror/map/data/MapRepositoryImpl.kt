package ru.blackmirrror.map.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.blackmirrror.core.api.UserDto
import ru.blackmirrror.core.exception.ApiErrorHandler
import ru.blackmirrror.core.exception.EmptyData
import ru.blackmirrror.core.exception.ImageNotUploaded
import ru.blackmirrror.core.exception.NoInternet
import ru.blackmirrror.core.exception.ServerError
import ru.blackmirrror.core.image_storage.FileRepository
import ru.blackmirrror.core.provider.AuthProvider
import ru.blackmirrror.core.provider.NetworkProvider
import ru.blackmirrror.core.state.ResultState
import ru.blackmirrror.map.domain.Category
import ru.blackmirrror.map.domain.MapRepository
import ru.blackmirrror.map.domain.model.Mark
import java.io.File
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val authProvider: AuthProvider,
    private val networkProvider: NetworkProvider,
    private val apiService: MapApiService,
    private val fileRepository: FileRepository
): MapRepository {

    override fun isInternetConnection(): Boolean {
        return networkProvider.isInternetConnection()
    }

    private fun getUserId(): Long {
        return authProvider.getUserId()
    }

    override fun getAllMarks(
        minRating: Double?,
        distance: Double?,
        lat: Double?,
        lon: Double?,
        categories: List<MarkCategoryDto>?
    ): Flow<ResultState<List<MarkLatLngDto>>> {
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                if (isInternetConnection()) {
                    val res = apiService.getAllMarks(
                        minRating, distance, lat, lon, categories
                    )
                    if (res.isSuccessful) {
                        if (res.body() != null)
                            emit(ResultState.Success(res.body()!!))
                        else
                            emit(ResultState.Error(EmptyData))
                    }
                    emit(ResultState.Error(ServerError))
                } else {
                    emit(ResultState.Error(NoInternet))
                }
            }
        }
    }

    override suspend fun getMark(markId: Long): Flow<ResultState<MarkDto>> {
        val userId = getUserId()
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                if (isInternetConnection()) {
                    val res = apiService.getMark(markId, userId)
                    if (res.isSuccessful) {
                        if (res.body() != null)
                            emit(ResultState.Success(res.body()!!))
                        else
                            emit(ResultState.Error(EmptyData))
                    }
                    emit(ResultState.Error(ServerError))
                } else {
                    emit(ResultState.Error(NoInternet))
                }
            }
        }
    }

    override suspend fun createMark(mark: MarkDto, file: File?): Flow<ResultState<MarkDto>> {
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                if (isInternetConnection()) {
                    val res = apiService.createMark(mark)
                    if (res.isSuccessful) {
                        if (res.body() != null) {
                            if (file != null) {
                                fileRepository.uploadImage(file).collect { result ->
                                    when (result) {
                                        is ResultState.Loading -> {}
                                        is ResultState.Success -> {
                                            val imageUrl = fileRepository.getImageUrl(file.name)
                                            res.body()!!.imageUrl = imageUrl
                                            emit(ResultState.Success(res.body()!!))
                                        }
                                        is ResultState.Error -> {
                                            Log.d("DDD", "createMark: ")
                                            res.body()!!.imageUrl = null
                                            emit(ResultState.Error(ImageNotUploaded, res.body()!!))
                                        }
                                    }
                                }
                            }
                        }
                        else
                            emit(ResultState.Error(EmptyData))
                    }
                    emit(ResultState.Error(ServerError))
                } else {
                    emit(ResultState.Error(NoInternet))
                }
            }
        }
    }

    override suspend fun updateMark(id: Long, mark: MarkDto): Flow<ResultState<MarkDto>> {
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                if (isInternetConnection()) {
                    val res = apiService.updateMark(id, mark)
                    if (res.isSuccessful) {
                        if (res.body() != null)
                            emit(ResultState.Success(res.body()!!))
                        else
                            emit(ResultState.Error(EmptyData))
                    }
                    emit(ResultState.Error(ServerError))
                } else {
                    emit(ResultState.Error(NoInternet))
                }
            }
        }
    }

    override suspend fun deleteMark(id: Long): Flow<ResultState<Unit>> {
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                if (isInternetConnection()) {
                    val res = apiService.deleteMark(id)
                    if (res.isSuccessful) {
                        if (res.body() != null)
                            emit(ResultState.Success(res.body()!!))
                        else
                            emit(ResultState.Error(EmptyData))
                    }
                    emit(ResultState.Error(ServerError))
                } else {
                    emit(ResultState.Error(NoInternet))
                }
            }
        }
    }

    override suspend fun likeMark(markId: Long, userId: Long): Flow<ResultState<Unit>> {
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                if (isInternetConnection()) {
                    val res = apiService.likeMark(markId, userId)
                    if (res.isSuccessful) {
                        if (res.body() != null)
                            emit(ResultState.Success(res.body()!!))
                        else
                            emit(ResultState.Error(EmptyData))
                    }
                    emit(ResultState.Error(ServerError))
                } else {
                    emit(ResultState.Error(NoInternet))
                }
            }
        }
    }

    override suspend fun unlikeMark(markId: Long, userId: Long): Flow<ResultState<Unit>> {
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                if (isInternetConnection()) {
                    val res = apiService.unlikeMark(markId, userId)
                    if (res.isSuccessful) {
                        if (res.body() != null)
                            emit(ResultState.Success(res.body()!!))
                        else
                            emit(ResultState.Error(EmptyData))
                    }
                    emit(ResultState.Error(ServerError))
                } else {
                    emit(ResultState.Error(NoInternet))
                }
            }
        }
    }

    override suspend fun reviewMark(
        markId: Long,
        userId: Long,
        review: MarkReviewDto
    ): Flow<ResultState<MarkReviewDto>> {
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                if (isInternetConnection()) {
                    val res = apiService.reviewMark(markId, userId, review)
                    if (res.isSuccessful) {
                        if (res.body() != null)
                            emit(ResultState.Success(res.body()!!))
                        else
                            emit(ResultState.Error(EmptyData))
                    }
                    emit(ResultState.Error(ServerError))
                } else {
                    emit(ResultState.Error(NoInternet))
                }
            }
        }
    }

    override suspend fun getMarkReviews(markId: Long): Flow<ResultState<List<MarkReviewDto>>> {
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                if (isInternetConnection()) {
                    val res = apiService.getMarkReviews(markId)
                    if (res.isSuccessful) {
                        if (res.body() != null)
                            emit(ResultState.Success(res.body()!!))
                        else
                            emit(ResultState.Error(EmptyData))
                    }
                    emit(ResultState.Error(ServerError))
                } else {
                    emit(ResultState.Error(NoInternet))
                }
            }
        }
    }

    private suspend fun uploadImage(file: File) {
        fileRepository.uploadImage(file).collect { result ->
            when (result) {
                is ResultState.Loading -> {}
                is ResultState.Success -> {
                    val imageUrl = fileRepository.getImageUrl(file.name)
                }
                is ResultState.Error -> {}
            }
        }
    }

    private val testMarks = listOf(
        Mark(1, 55.7450, 37.6050, "Парк Горького", "Центральный парк культуры и отдыха.",
            "https://example.com/gorky_park.jpg", 175, 5, listOf(Category.HIKE), UserDto(1, "naturefan", "", phone = "", online = false), System.currentTimeMillis(), System.currentTimeMillis() - 604800000),
        Mark(2, 55.8295, 37.6384, "Лосиный остров", "Национальный парк с богатой флорой и фауной.",
            "https://example.com/losiny_ostrov.jpg", 112, 4, listOf(Category.BARBEQUE, Category.HIKE), UserDto(2, "forestman", "", phone = "", online = false), System.currentTimeMillis(), System.currentTimeMillis() - 259200000),
        Mark(3, 55.7330, 37.5252, "Нескучный сад", "Исторический парк с живописными тропами.",
            "https://example.com/neskuchny.jpg", 87, 5, listOf(Category.HIKE), UserDto(3, "walker", "", phone = "", online = false), System.currentTimeMillis(), System.currentTimeMillis() - 86400000),
        Mark(4, 55.8012, 37.6963, "Сокольники", "Один из старейших парков Москвы.",
            "https://example.com/sokolniki.jpg", 143, 5, listOf(Category.HIKE), UserDto(4, "citynature", "", phone = "", online = false), System.currentTimeMillis(), System.currentTimeMillis() - 172800000),
        Mark(5, 55.5613, 37.5765, "Битцевский лес", "Природная территория на юге Москвы.",
            "https://example.com/bitza.jpg", 96, 3, listOf(Category.BARBEQUE, Category.HIKE), UserDto(5, "southern", "", phone = "", online = false), System.currentTimeMillis(), System.currentTimeMillis() - 345600000),
        Mark(6, 55.7811, 37.4094, "Кусково", "Усадьба с парком и прудом.",
            "https://example.com/kuskovo.jpg", 134, 3, listOf(Category.HIKE), UserDto(6, "oldparks", "", phone = "", online = false), System.currentTimeMillis(), System.currentTimeMillis() - 1209600000),
        Mark(7, 55.8950, 37.4767, "Долгие пруды", "Популярное место для прогулок и рыбалки.",
            "https://example.com/dolgie_prudy.jpg", 78, 3, listOf(Category.HIKE), UserDto(7, "fisherman", "", phone = "", online = false), System.currentTimeMillis(), System.currentTimeMillis() - 43200000),
        Mark(8, 55.7214, 37.6386, "Парк Тюфелева роща", "Современный урбанистический парк.",
            "https://example.com/tyufelev.jpg", 58, 4, listOf(Category.HIKE), UserDto(8, "urban", "", phone = "", online = false), System.currentTimeMillis(), System.currentTimeMillis() - 3600000),
        Mark(9, 55.8676, 37.8312, "Пироговское водохранилище", "Большое водохранилище для купания и спорта.",
            "https://example.com/pir_waters.jpg", 200, 5, listOf(Category.BARBEQUE, Category.HIKE, Category.FISHING), UserDto(9, "swimmer", "", phone = "", online = false), System.currentTimeMillis(), System.currentTimeMillis() - 604800000),
        Mark(10, 55.5935, 37.7020, "Музей-заповедник Коломенское", "Исторический природный ландшафт.",
            "https://example.com/kolomenskoe.jpg", 155, 5, listOf(Category.HIKE), UserDto(10, "historywalker", "", phone = "", online = false), System.currentTimeMillis(), System.currentTimeMillis() - 100000000),
        Mark(11, 56.0048, 37.2144, "Звенигородский лес", "Тихий сосновый лес в Подмосковье.",
            "https://example.com/zven_forest.jpg", 89, 4, listOf(Category.HIKE), UserDto(11, "silentpath", "", phone = "", online = false), System.currentTimeMillis(), System.currentTimeMillis() - 252000000),
        Mark(12, 56.0210, 37.4622, "Озеро Сенеж", "Живописное озеро с пляжами.",
            "https://example.com/senezh.jpg", 176, 5, listOf(Category.BARBEQUE, Category.HIKE, Category.FISHING), UserDto(12, "beachgoer", "", phone = "", online = false), System.currentTimeMillis(), System.currentTimeMillis() - 95000000),
        Mark(13, 55.5800, 38.0020, "Озеро Белое", "Один из самых чистых водоёмов в области.",
            "https://example.com/beloe_lake.jpg", 144, 5, listOf(Category.BARBEQUE, Category.HIKE, Category.FISHING), UserDto(13, "lakefan", "", phone = "", online = false), System.currentTimeMillis(), System.currentTimeMillis() - 72000000),
        Mark(14, 55.8263, 38.0056, "Радужный парк", "Малолюдное и зелёное место для отдыха.",
            "https://example.com/rainbow_park.jpg", 47, 4, listOf(Category.HIKE), UserDto(14, "quietone", "", phone = "", online = false), System.currentTimeMillis(), System.currentTimeMillis() - 8640000),
        Mark(15, 55.8356, 37.6865, "Измайловский парк", "Один из крупнейших парков в Европе.",
            "https://example.com/izmailovo.jpg", 190, 4, listOf(Category.HIKE), UserDto(15, "runner", "", phone = "", online = false), System.currentTimeMillis(), System.currentTimeMillis() - 64000000),
        Mark(16, 56.0330, 37.9750, "Национальный парк Мещера", "Тихие леса и болота на востоке области.",
            "https://example.com/meshhera.jpg", 120, 3, listOf(Category.HIKE), UserDto(16, "wildlife", "", phone = "", online = false), System.currentTimeMillis(), System.currentTimeMillis() - 54000000),
        Mark(17, 55.6805, 36.8600, "Парк усадьбы Архангельское", "Пейзажный парк с историей.",
            "https://example.com/arkhangelskoe.jpg", 110, 5, listOf(Category.HIKE), UserDto(17, "romantic", "", phone = "", online = false), System.currentTimeMillis(), System.currentTimeMillis() - 333000000),
        Mark(18, 55.8888, 37.5194, "Парк Северное Тушино", "Парк на берегу канала им. Москвы.",
            "https://example.com/severnoye.jpg", 65, 3, listOf(Category.BARBEQUE, Category.HIKE, Category.FISHING), UserDto(18, "canallover", "", phone = "", online = false), System.currentTimeMillis(), System.currentTimeMillis() - 100000000),
        Mark(19, 55.8990, 37.3705, "Парк Красногорска", "Современный парк с набережной.",
            "https://example.com/krasnogorsk.jpg", 71, 4, listOf(Category.HIKE), UserDto(19, "localfan", "", phone = "", online = false), System.currentTimeMillis(), System.currentTimeMillis() - 212000000),
        Mark(20, 55.7503, 37.7760, "Парк Садовники", "Уютный парк у Нагатинской набережной.",
            "https://example.com/sadovniki.jpg", 93, 4, listOf(Category.BARBEQUE, Category.HIKE, Category.FISHING), UserDto(20, "parksurfer", "", phone = "", online = false), System.currentTimeMillis(), System.currentTimeMillis() - 129600000)
    )


}
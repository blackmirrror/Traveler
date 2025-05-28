package ru.blackmirrror.map.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.blackmirrror.core.api.UserDto
import ru.blackmirrror.core.exception.ApiErrorHandler
import ru.blackmirrror.core.exception.EmptyData
import ru.blackmirrror.core.exception.NoInternet
import ru.blackmirrror.core.exception.ServerError
import ru.blackmirrror.core.provider.AuthProvider
import ru.blackmirrror.core.provider.NetworkProvider
import ru.blackmirrror.core.state.ResultState
import ru.blackmirrror.map.domain.Category
import ru.blackmirrror.map.domain.MapRepository
import ru.blackmirrror.map.domain.model.Mark
import ru.blackmirrror.map.domain.toDomain
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val authProvider: AuthProvider,
    private val networkProvider: NetworkProvider,
    private val apiService: MapApiService
): MapRepository {

    override fun isInternetConnection(): Boolean {
        return networkProvider.isInternetConnection()
    }

    override fun loadMarks(
        category: Category?,
        radius: Int?,
        minRating: Int?
    ): Flow<ResultState<List<Mark>>> {
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                if (isInternetConnection()) {
                    emit(ResultState.Success(testMarks))
//                    val res = apiService.getAllMarks()
//                    if (res.isSuccessful) {
//                        val marks = res.body()
//                        if (marks != null) {
//                            val userDomain = marks.map { it.toDomain() }
//                            emit(ResultState.Success(userDomain))
//                        }
//                        else {
//                            emit(ResultState.Error(EmptyData))
//                        }
//                    } else
//                        emit(ResultState.Error(ServerError))
                } else {
                    emit(ResultState.Error(NoInternet))
                }
            }
        }
    }

    val testMarks = listOf(
        Mark(
            id = 1L,
            latitude = 55.7539,
            longitude = 37.6208,
            title = "Красная площадь",
            description = "Главная площадь Москвы, историческое место.",
            imageUrl = "https://example.com/red_square.jpg",
            likes = 123,
            author = UserDto(id = 1L, username = "historyFan", phone = ""),
            dateChanges = System.currentTimeMillis(),
            dateCreate = System.currentTimeMillis() - 86400000
        ),
        Mark(
            id = 2L,
            latitude = 55.7520,
            longitude = 37.6175,
            title = "Кремль",
            description = "Официальная резиденция Президента РФ.",
            imageUrl = "https://example.com/kremlin.jpg",
            likes = 256,
            author = UserDto(id = 2L, firstName = "tourist123", phone = ""),
            dateChanges = System.currentTimeMillis(),
            dateCreate = System.currentTimeMillis() - 172800000
        ),
        Mark(
            id = 3L,
            latitude = 55.7601,
            longitude = 37.6187,
            title = "Большой театр",
            description = "Известный театр оперы и балета.",
            imageUrl = "https://example.com/bolshoi.jpg",
            likes = 98,
            author = UserDto(
                id = 3L, firstName = "artlover", phone = ""),
            dateChanges = System.currentTimeMillis(),
            dateCreate = System.currentTimeMillis() - 43200000
        ),
        Mark(
            id = 4L,
            latitude = 55.7450,
            longitude = 37.6050,
            title = "Парк Горького",
            description = "Центральный парк культуры и отдыха.",
            imageUrl = "https://example.com/gorky_park.jpg",
            likes = 175,
            author = UserDto(id = 4L, firstName = "naturefan", phone = ""),
            dateChanges = System.currentTimeMillis(),
            dateCreate = System.currentTimeMillis() - 604800000
        )
    )

}
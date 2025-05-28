package ru.blackmirrror.map.domain

import kotlinx.coroutines.flow.Flow
import ru.blackmirrror.core.state.ResultState
import ru.blackmirrror.map.domain.model.Mark

interface MapRepository {

    fun isInternetConnection(): Boolean

    fun loadMarks(
        category: Category? = null,
        radius: Int? = null,
        minRating: Int? = null
    ): Flow<ResultState<List<Mark>>>
}

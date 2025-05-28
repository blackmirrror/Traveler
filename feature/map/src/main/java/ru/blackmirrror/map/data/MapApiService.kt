package ru.blackmirrror.map.data

import retrofit2.Response
import retrofit2.http.GET

interface MapApiService {

    @GET("/marks")
    fun getAllMarks(): Response<List<MarkDto>>
}
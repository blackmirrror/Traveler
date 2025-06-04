package ru.blackmirrror.map.domain.model

import ru.blackmirrror.core.api.UserDto
import ru.blackmirrror.map.domain.Category

data class Mark(
    val id: Long?,
    val latitude: Double?,
    val longitude: Double?,
    val title: String?,
    val description: String?,
    val imageUrl: String?,
    val likes: Int?,
    val rating: Int?,
    val categories: List<Category>?,
    val author: UserDto?,
    val dateChanges: Long?,
    val dateCreate: Long?
)
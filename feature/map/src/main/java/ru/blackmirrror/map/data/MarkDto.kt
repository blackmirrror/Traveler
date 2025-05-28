package ru.blackmirrror.map.data

import ru.blackmirrror.core.api.UserDto

data class MarkDto(
    val id: Long?,
    val latitude: Double?,
    val longitude: Double?,
    val title: String?,
    val description: String?,
    val imageUrl: String?,
    val likes: Int?,
    val author: UserDto?,
    val dateChanges: Long?,
    val dateCreate: Long?
)
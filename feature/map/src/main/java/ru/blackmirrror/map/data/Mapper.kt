package ru.blackmirrror.map.data

import ru.blackmirrror.core.api.UserDto
import ru.blackmirrror.database.entity.MarkEntity

fun MarkDto.toMarkEntity(): MarkEntity {
    return MarkEntity(
        id = id ?: 0,
        title = title,
        description = description,
        latitude = latitude ?: 0.0,
        longitude = longitude ?: 0.0,
        imageUrl = imageUrl,
        averageRating = averageRating,
        likeCount = likeCount,
        dateChange = dateChange,
        dateCreate = dateCreate,
        authorId = author?.id ?: 0
    )
}

fun MarkEntity.toMarkDto(): MarkDto {
    return MarkDto(
        id = id,
        title = title,
        description = description,
        latitude = latitude,
        longitude = longitude,
        imageUrl = imageUrl,
        averageRating = averageRating,
        likeCount = likeCount,
        dateChange = dateChange,
        dateCreate = dateCreate,
        author = UserDto(authorId, phone = "")
    )
}

fun MarkEntity.toMarkLatLngDto(): MarkLatLngDto {
    return MarkLatLngDto(
        id = id,
        lat = latitude,
        lon = longitude
    )
}

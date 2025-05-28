package ru.blackmirrror.map.domain

import ru.blackmirrror.map.data.MarkDto
import ru.blackmirrror.map.domain.model.Mark

fun Mark.toDto(): MarkDto {
    return MarkDto(
        id = id,
        latitude = latitude,
        longitude = longitude,
        title = title,
        description = description,
        imageUrl = imageUrl,
        likes = likes,
        author = author,
        dateChanges = dateChanges,
        dateCreate = dateCreate
    )
}

fun MarkDto.toDomain(): Mark {
    return Mark(
        id = id,
        latitude = latitude,
        longitude = longitude,
        title = title,
        description = description,
        imageUrl = imageUrl,
        likes = likes,
        author = author,
        dateChanges = dateChanges,
        dateCreate = dateCreate
    )
}

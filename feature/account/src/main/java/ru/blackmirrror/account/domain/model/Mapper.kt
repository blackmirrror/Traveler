package ru.blackmirrror.account.domain.model

import ru.blackmirrror.account.data.api.UserDto


fun UserDto.toDomain(): User = User(
    id = id,
    username = username,
    email = email,
    phone = phone,
    firstName = firstName,
    lastName = lastName,
    birthDate = birthDate,
    photoUrl = photoUrl,
    isOnline = isOnline,
    lastSeen = lastSeen,
)

fun User.toDto(): UserDto = UserDto(
    id = id,
    username = username,
    email = email,
    phone = phone,
    firstName = firstName,
    lastName = lastName,
    birthDate = birthDate,
    photoUrl = photoUrl,
    isOnline = isOnline,
    lastSeen = lastSeen,
)
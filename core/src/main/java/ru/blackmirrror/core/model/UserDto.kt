package ru.blackmirrror.core.model

import java.util.Date

data class UserDto(
    val id: Long,
    val phone: String,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
    val birthDate: Date,
    val lastSeen: Date
)
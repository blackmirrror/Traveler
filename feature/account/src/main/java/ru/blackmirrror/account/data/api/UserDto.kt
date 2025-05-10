package ru.blackmirrror.account.data.api

data class UserDto(
    val id: Long? = null,
    val username: String? = null,
    val email: String? = null,
    val phone: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val birthDate: Long? = null,
    val photoUrl: String? = null,
    val isOnline: Boolean = false,
    val lastSeen: Long? = null
)
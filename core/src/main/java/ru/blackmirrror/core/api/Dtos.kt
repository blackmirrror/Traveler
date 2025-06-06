package ru.blackmirrror.core.api

data class UserDto(
    val id: Long? = null,
    val username: String? = null,
    val email: String? = null,
    val phone: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val birthDate: Long? = null,
    val photoUrl: String? = null,
    val online: Boolean = false,
    val lastSeen: Long? = null,
    val markCount: Int = 0,
    val postCount: Int = 0
)

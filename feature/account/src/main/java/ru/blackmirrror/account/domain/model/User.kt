package ru.blackmirrror.account.domain.model

data class User(
    val id: Long? = null,
    val username: String? = null,
    val email: String? = null,
    val phone: String,
    var firstName: String? = null,
    var lastName: String? = null,
    val birthDate: Long? = null,
    val photoUrl: String? = null,
    val isOnline: Boolean = false,
    val lastSeen: Long? = null
)
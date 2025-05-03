package ru.blackmirrror.account.domain.model

import java.util.Date

data class User(
    val firstName: String?,
    val lastName: String?,
    val phone: String,
    val email: String?,
    val birthDate: Date?,
    val photoUrl: String?
)
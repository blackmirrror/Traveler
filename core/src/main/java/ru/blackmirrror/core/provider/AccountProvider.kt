package ru.blackmirrror.core.provider

import ru.blackmirrror.core.api.UserDto

interface AccountProvider {

    fun getUser(): UserDto?
}

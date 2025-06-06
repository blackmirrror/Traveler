package ru.blackmirrror.account.di

import ru.blackmirrror.account.domain.AccountRepository
import ru.blackmirrror.core.api.UserDto
import ru.blackmirrror.core.provider.AccountProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountProviderImpl @Inject constructor(
    private val accountRepository: AccountRepository
) : AccountProvider {

    override fun getUser(): UserDto? {
        return accountRepository.getUserFromPrefs()
    }
}

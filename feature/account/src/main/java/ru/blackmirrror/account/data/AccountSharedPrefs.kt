package ru.blackmirrror.account.data

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AccountSharedPrefs @Inject constructor(
    @ApplicationContext context: Context
) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var firstName: String?
        get() = sharedPreferences.getString(KEY_FIRST_NAME, null)
        set(value) = sharedPreferences.edit().putString(KEY_FIRST_NAME, value).apply()

    var lastName: String?
        get() = sharedPreferences.getString(KEY_LAST_NAME, null)
        set(value) = sharedPreferences.edit().putString(KEY_LAST_NAME, value).apply()

    var phoneNumber: String?
        get() = sharedPreferences.getString(KEY_PHONE_NUMBER, null)
        set(value) = sharedPreferences.edit().putString(KEY_PHONE_NUMBER, value).apply()

    var email: String?
        get() = sharedPreferences.getString(KEY_EMAIL, null)
        set(value) = sharedPreferences.edit().putString(KEY_EMAIL, value).apply()

    var birthDate: Long
        get() = sharedPreferences.getLong(KEY_BIRTH_DATE, 0L)
        set(value) = sharedPreferences.edit().putLong(KEY_BIRTH_DATE, value).apply()

    companion object {
        private const val PREFS_NAME = "accountPrefs"
        private const val KEY_FIRST_NAME = "firstName"
        private const val KEY_LAST_NAME = "lastName"
        private const val KEY_PHONE_NUMBER = "phoneNumber"
        private const val KEY_EMAIL = "email"
        private const val KEY_BIRTH_DATE = "birthDate"
    }
}
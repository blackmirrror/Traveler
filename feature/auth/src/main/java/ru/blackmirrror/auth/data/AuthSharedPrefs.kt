package ru.blackmirrror.auth.data

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthSharedPrefs @Inject constructor(
    @ApplicationContext context: Context
) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var isAuthenticated: Boolean
        get() = sharedPreferences.getBoolean(KEY_IS_AUTHENTICATED, false)
        set(value) = sharedPreferences.edit().putBoolean(KEY_IS_AUTHENTICATED, value).apply()

    var phoneNumber: String?
        get() = sharedPreferences.getString(KEY_PHONE_NUMBER, null)
        set(value) = sharedPreferences.edit().putString(KEY_PHONE_NUMBER, value).apply()

    var verificationId: String?
        get() = sharedPreferences.getString(KEY_VERIFICATION_ID, null)
        set(value) = sharedPreferences.edit().putString(KEY_VERIFICATION_ID, value).apply()

    companion object {
        private const val PREFS_NAME = "authPrefs"
        private const val KEY_IS_AUTHENTICATED = "isAuthenticated"
        private const val KEY_PHONE_NUMBER = "phoneNumber"
        private const val KEY_VERIFICATION_ID = "verificationId"
    }

    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}

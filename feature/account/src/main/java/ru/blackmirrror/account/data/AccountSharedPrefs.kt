package ru.blackmirrror.account.data

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.blackmirrror.account.domain.model.User
import java.util.Date
import javax.inject.Inject

class AccountSharedPrefs @Inject constructor(
    @ApplicationContext context: Context
) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var id: Long
        get() = sharedPreferences.getLong(KEY_ID, 0L)
        set(value) = sharedPreferences.edit().putLong(KEY_ID, value).apply()

    var username: String?
        get() = sharedPreferences.getString(KEY_USERNAME, null)
        set(value) = sharedPreferences.edit().putString(KEY_USERNAME, value).apply()

    var firstName: String?
        get() = sharedPreferences.getString(KEY_FIRST_NAME, null)
        set(value) = sharedPreferences.edit().putString(KEY_FIRST_NAME, value).apply()

    var lastName: String?
        get() = sharedPreferences.getString(KEY_LAST_NAME, null)
        set(value) = sharedPreferences.edit().putString(KEY_LAST_NAME, value).apply()

    var phone: String?
        get() = sharedPreferences.getString(KEY_PHONE_NUMBER, null)
        set(value) = sharedPreferences.edit().putString(KEY_PHONE_NUMBER, value).apply()

    var email: String?
        get() = sharedPreferences.getString(KEY_EMAIL, null)
        set(value) = sharedPreferences.edit().putString(KEY_EMAIL, value).apply()

    var birthDate: Long
        get() = sharedPreferences.getLong(KEY_BIRTH_DATE, 0L)
        set(value) = sharedPreferences.edit().putLong(KEY_BIRTH_DATE, value).apply()

    var photoUrl: String?
        get() = sharedPreferences.getString(KEY_PHOTO_URL, null)
        set(value) = sharedPreferences.edit().putString(KEY_PHOTO_URL, value).apply()

    var lastSeen: Long
        get() = sharedPreferences.getLong(KEY_LAST_SEEN, 0L)
        set(value) = sharedPreferences.edit().putLong(KEY_LAST_SEEN, value).apply()

    companion object {
        private const val PREFS_NAME = "accountPrefs"
        private const val KEY_ID = "id"
        private const val KEY_USERNAME = "username"
        private const val KEY_FIRST_NAME = "firstName"
        private const val KEY_LAST_NAME = "lastName"
        private const val KEY_PHONE_NUMBER = "phoneNumber"
        private const val KEY_EMAIL = "email"
        private const val KEY_BIRTH_DATE = "birthDate"
        private const val KEY_PHOTO_URL = "photoUrl"
        private const val KEY_LAST_SEEN = "lastSeen"
    }

    fun saveUserToPrefs(user: User) {
        user.id?.let { id = it }
        username = user.username
        email = user.email
        phone = user.phone
        firstName = user.firstName
        lastName = user.lastName
        user.birthDate?.let { birthDate = birthDate }
        photoUrl = user.photoUrl
        lastSeen = lastSeen
    }

    fun getUserFromPrefs(): User? {
        if (phone == null)
            return null
        return User(
            id = id,
            username = username,
            email = email,
            phone = phone!!,
            firstName = firstName,
            lastName = lastName,
            birthDate = birthDate,
            photoUrl = photoUrl,
            isOnline = false,
            lastSeen = lastSeen,
        )
    }

    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}
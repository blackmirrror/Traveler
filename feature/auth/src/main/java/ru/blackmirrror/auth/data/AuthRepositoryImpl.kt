package ru.blackmirrror.auth.data

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import ru.blackmirrror.auth.domain.AuthRepository
import ru.blackmirrror.core.api.ActivityProvider
import ru.blackmirrror.core.api.NetworkProvider
import ru.blackmirrror.core.exception.NoInternet
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class AuthRepositoryImpl @Inject constructor(
//    private val activityProvider: ActivityProvider,
    private val networkProvider: NetworkProvider,
    private val firebaseAuth: FirebaseAuth,
    private val authSharedPrefs: AuthSharedPrefs
) : AuthRepository {

    override fun isAuthenticated(): Boolean {
        if (networkProvider.isInternetConnection()) {
            return firebaseAuth.currentUser != null
        }
        return authSharedPrefs.isAuthenticated
    }

    override suspend fun sendPhoneOtp(phone: String): Result<Unit> {
        authSharedPrefs.phoneNumber = phone

        if (!networkProvider.isInternetConnection())
            return Result.failure(NoInternet)

        return suspendCoroutine { continuation ->
            val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
//                .setActivity(activityProvider.getActivity())
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        firebaseAuth.signInWithCredential(credential)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    continuation.resume(Result.success(Unit))
                                } else {
                                    continuation.resume(
                                        Result.failure(
                                            task.exception ?: Exception("Auth failed")
                                        )
                                    )
                                }
                            }
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        continuation.resume(Result.failure(e))
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        authSharedPrefs.verificationId = verificationId
                        continuation.resume(Result.success(Unit))
                    }
                })
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    override suspend fun verifyPhoneOtp(code: String): Result<Unit> {
        if (!networkProvider.isInternetConnection())
            return Result.failure(NoInternet)

        return suspendCoroutine { continuation ->
            val credential =
                authSharedPrefs.verificationId?.let { PhoneAuthProvider.getCredential(it, code) }
            if (credential != null) {
                firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            authSharedPrefs.isAuthenticated = true
                            continuation.resume(Result.success(Unit))
                        } else {
                            continuation.resume(
                                Result.failure(
                                    task.exception ?: Exception("Verification failed")
                                )
                            )
                        }
                    }
            }
        }
    }

    //    override suspend fun getCurrentUser(): User? {
//        return firebaseAuth.currentUser?.let {
//            User(it.uid, it.phoneNumber)
//        }
//    }
//
    override suspend fun logout(): Result<Unit> {
        if (!networkProvider.isInternetConnection())
            return Result.failure(NoInternet)

        firebaseAuth.signOut()
        authSharedPrefs.isAuthenticated = false
        return Result.success(Unit)
    }
//
//    override fun getAuthToken(): String? {
//        return sharedPreferences.getString("auth_token", null)
//    }
}

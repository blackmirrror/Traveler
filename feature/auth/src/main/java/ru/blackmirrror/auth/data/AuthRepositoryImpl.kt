package ru.blackmirrror.auth.data

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import ru.blackmirrror.auth.data.api.AuthApiService
import ru.blackmirrror.auth.data.api.UserDto
import ru.blackmirrror.auth.domain.AuthRepository
import ru.blackmirrror.core.exception.ApiErrorHandler
import ru.blackmirrror.core.exception.EmptyData
import ru.blackmirrror.core.provider.NetworkProvider
import ru.blackmirrror.core.exception.NoInternet
import ru.blackmirrror.core.exception.ServerError
import ru.blackmirrror.core.state.ResultState
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val networkProvider: NetworkProvider,
    private val firebaseAuth: FirebaseAuth,
    private val authApiService: AuthApiService,
    private val authSharedPrefs: AuthSharedPrefs
) : AuthRepository {

    var currentPhone = authSharedPrefs.phoneNumber

    override fun isAuthenticated(): Boolean {
        if (networkProvider.isInternetConnection()) {
            return firebaseAuth.currentUser != null
        }
        return authSharedPrefs.isAuthenticated
    }

    override suspend fun sendPhoneOtp(phone: String, isUpdate: Boolean): Flow<ResultState<Unit>> = flow {
        emit(ResultState.Loading())

        authSharedPrefs.phoneNumber = phone

        if (!networkProvider.isInternetConnection()) {
            emit(ResultState.Error(NoInternet))
            return@flow
        }

        suspendCancellableCoroutine { continuation ->
            val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        if (isUpdate) {
                            firebaseAuth.currentUser?.updatePhoneNumber(credential)
                                ?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        continuation.resume(Unit)
                                    } else {
                                        continuation.resumeWithException(ServerError)
                                    }
                                }
                        } else {
                            firebaseAuth.signInWithCredential(credential)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        continuation.resume(Unit)
                                    } else {
                                        continuation.resumeWithException(ServerError)
                                    }
                                }
                        }
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        continuation.resumeWithException(e)
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        authSharedPrefs.verificationId = verificationId
                        continuation.resume(Unit)
                    }
                })
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)
        }

        emit(ResultState.Success(Unit))
    }.catch {
        emit(ResultState.Error(ServerError))
    }

    override suspend fun verifyPhoneOtp(code: String, isUpdate: Boolean): Flow<ResultState<Unit>> = flow {
        emit(ResultState.Loading())

        if (!networkProvider.isInternetConnection()) {
            emit(ResultState.Error(NoInternet))
            return@flow
        }

        val verificationId = authSharedPrefs.verificationId
        val credential = verificationId?.let { PhoneAuthProvider.getCredential(it, code) }

        if (credential == null) {
            emit(ResultState.Error(ServerError))
            return@flow
        }

        suspendCancellableCoroutine { continuation ->
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Unit)
                    } else {
                        continuation.resumeWithException(ServerError)
                    }
                }
        }

        if (isUpdate) {
            emitAll(updateUser())
        } else {
            emitAll(registerUser())
        }
    }.catch {
        emit(ResultState.Error(ServerError))
    }


//    @OptIn(ExperimentalCoroutinesApi::class)
//    override suspend fun sendPhoneOtp(phone: String, isUpdate: Boolean): Flow<ResultState<Unit>> = flow {
//        emit(ResultState.Loading())
//
//        authSharedPrefs.phoneNumber = phone
//
//        if (!networkProvider.isInternetConnection()) {
//            emit(ResultState.Error(NoInternet))
//            return@flow
//        }
//
//        suspendCancellableCoroutine { continuation ->
//            val options = PhoneAuthOptions.newBuilder(firebaseAuth)
//                .setPhoneNumber(phone)
//                .setTimeout(60L, TimeUnit.SECONDS)
//                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                        firebaseAuth.signInWithCredential(credential)
//                            .addOnCompleteListener { task ->
//                                if (task.isSuccessful) {
//                                    continuation.resume(Unit)
//                                } else {
//                                    continuation.resumeWithException(ServerError)
//                                }
//                            }
//                    }
//
//                    override fun onVerificationFailed(e: FirebaseException) {
//                        continuation.resumeWithException(e)
//                    }
//
//                    override fun onCodeSent(
//                        verificationId: String,
//                        token: PhoneAuthProvider.ForceResendingToken
//                    ) {
//                        authSharedPrefs.verificationId = verificationId
//                        continuation.resume(Unit) {}
//                    }
//                })
//                .build()
//
//            PhoneAuthProvider.verifyPhoneNumber(options)
//        }
//
//        emit(ResultState.Success(Unit))
//    }.catch {
//        emit(ResultState.Error(ServerError))
//    }
//
//    override suspend fun verifyPhoneOtp(code: String, isUpdate: Boolean): Flow<ResultState<Unit>> = flow {
//        emit(ResultState.Loading())
//
//        if (!networkProvider.isInternetConnection()) {
//            emit(ResultState.Error(NoInternet))
//            return@flow
//        }
//
//        val verificationId = authSharedPrefs.verificationId
//        val credential = verificationId?.let { PhoneAuthProvider.getCredential(it, code) }
//
//        if (credential == null) {
//            emit(ResultState.Error(ServerError))
//            return@flow
//        }
//
//        suspendCancellableCoroutine { continuation ->
//            firebaseAuth.signInWithCredential(credential)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        continuation.resume(Unit)
//                    } else {
//                        continuation.resumeWithException(ServerError)
//                    }
//                }
//        }
//        if (isUpdate) emitAll(updateUser())
//        else emitAll(registerUser())
//    }.catch {
//        emit(ResultState.Error(ServerError))
//    }

    private fun registerUser(): Flow<ResultState<Unit>> {
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                val res = authApiService.registerUser(UserDto(phone = authSharedPrefs.phoneNumber!!))
                if (res.isSuccessful) {
                    authSharedPrefs.isAuthenticated = true
                    emit(ResultState.Success(Unit))
                } else {
                    emit(ResultState.Error(ServerError))
                }
            }
        }
    }

    private fun updateUser(): Flow<ResultState<Unit>> {
        return ApiErrorHandler.handleErrors {
            flow {
                emit(ResultState.Loading())
                val oldUser = authApiService.getUserByPhone(currentPhone!!)
                if (oldUser.isSuccessful) {
                    val newUser = oldUser.body()!!.copy(phone = authSharedPrefs.phoneNumber!!)
                    val updateResult = authApiService.updateUser(newUser.id!!, newUser)
                    if (updateResult.isSuccessful) {
                        authSharedPrefs.isAuthenticated = true
                        emit(ResultState.Success(Unit))
                    } else {
                        emit(ResultState.Error(ServerError))
                    }
                } else {
                    emit(ResultState.Error(ServerError))
                }
            }
        }
    }

    override fun getPhoneNumber(): String? {
        return authSharedPrefs.phoneNumber
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
        authSharedPrefs.clearAll()
        return Result.success(Unit)
    }
//
//    override fun getAuthToken(): String? {
//        return sharedPreferences.getString("auth_token", null)
//    }
}

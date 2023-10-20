package com.tsciences.dailycaller.android.data.utils

import androidx.annotation.Keep

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tsciences.dailycaller.android.R
import com.tsciences.dailycaller.android.ui.commonComponents.utils.UiText
import com.tsciences.dailycaller.android.utils.fromJson
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import retrofit2.Response

import java.io.IOException

sealed class NetworkResult<out T> {
    data class Success<out R>(val value: R) : NetworkResult<R>()
    object Loading : NetworkResult<Nothing>()
    data class Failure(
        val uiText: UiText?
    ) : NetworkResult<Nothing>()
}

inline fun <reified T> NetworkResult<T>.doIfFailure(callback: (error: UiText?) -> Unit) {
    if (this is NetworkResult.Failure) {
        callback(uiText)
    }
}

inline fun <reified T> NetworkResult<T>.doIfSuccess(callback: (value: T) -> Unit) {
    if (this is NetworkResult.Success) {
        callback(value)
    }
}

inline fun <reified T> NetworkResult<T>.doIfLoading(callback: () -> Unit) {
    if (this is NetworkResult.Loading) {
        callback()
    }
}

fun Response<*>?.getMessageFromInternalServerError(): String {
    val INTERNAL_SERVER_ERROR = "Internal Server Error"
    if (this == null) {
        return INTERNAL_SERVER_ERROR
    }
    return errorBody()?.string()?.fromJson(ErrorResponse::class.java)?.let {
        it.message ?: INTERNAL_SERVER_ERROR
    } ?: INTERNAL_SERVER_ERROR
}

@Keep
data class ErrorResponse(
    @Expose
    @SerializedName("message")
    val message: String?,

    @Expose
    @SerializedName("error_description")
    val errorDescription: String?
)

suspend fun <T> tryApiCall(block: suspend () -> NetworkResult<T>): NetworkResult<T> =
    try {
        block()
    } catch (e: Exception) {
        var message: UiText? = UiText.StringResource(R.string.error_something_went_wrong)
        when (e) {
            is HttpException -> {
                if (e.code() == 400) {
                    message =
                        UiText.StringResource(R.string.error_network_not_found)
                }
                if (e.code() == 503) {
                    message =
                        UiText.StringResource(R.string.error_service_unavailable)
                } else if (e.code() == 401) { // As the backend cant change the message in response , set message for session expiry
                    message = UiText.StringResource(R.string.error_session_expired)
                } else if (e.code() == 500) {
                    message = UiText.StringResource(R.string.error_something_went_wrong)
                } else if (e.code() !in 501..599) {
                    val errorBody = e.response()?.errorBody()
                    errorBody?.let {// handling multiple error body structures
                        try {
                            val error = it.string().fromJson(ErrorResponse::class.java)
                            if (!error.message.isNullOrEmpty())
                                message = UiText.DynamicText(error.message)
                            else if (!error.errorDescription.isNullOrEmpty())
                                message = UiText.DynamicText(error.errorDescription)
                        } catch (ex: Exception) {
                            message = null
                        }
                    }
                }
            }
            is NoInternetException -> {
                message = UiText.StringResource(R.string.error_internet_unavailable)
            }
            is CancellationException -> {
                message = null
            }
            else -> {
                message = e.localizedMessage?.let { UiText.DynamicText(it) }
            }
        }
        NetworkResult.Failure(message)
    }

class NoInternetException : IOException() {
    override val message: String
        get() = "You are offline. Please Check your Internet Connection."
}
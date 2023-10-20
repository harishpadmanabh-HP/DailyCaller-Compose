package com.tsciences.dailycaller.android.data.remote.interceptors

import com.tsciences.dailycaller.android.BuildConfig
import com.tsciences.dailycaller.android.appConstants.AppConstants
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class TokenInterceptor(
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request? = chain.request()
        var url: HttpUrl? = null
        if (request?.url?.host == "api.dailycaller.com") {
            url = request.url.newBuilder()
                .addQueryParameter(AppConstants.AUTHORIZATION_HEADER_KEY, BuildConfig.API_KEY)
                .build()
        } else if (request?.url?.host == "www.googleapis.com") {
            url = request.url.newBuilder().addQueryParameter(
                AppConstants.AUTHORIZATION_HEADER_KEY,
                BuildConfig.GOOGLE_API_KEY
            ).build()
        } else if (request?.url?.host == "streamdata.dailycaller.com") {
            url = request.url.newBuilder().build()
        } else if (request?.url?.host == "api.piano.io") {
            url = request.url.newBuilder().build()
        }
        request = url?.let { request?.newBuilder()?.url(it)?.build() }
        return request?.let { chain.proceed(it) }!!
    }
}
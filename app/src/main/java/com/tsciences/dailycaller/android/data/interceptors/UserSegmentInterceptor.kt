package com.tsciences.dailycaller.android.data.remote.interceptors

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.text.SimpleDateFormat
import java.util.*

class UserSegmentInterceptor(
    context: Context
) : Interceptor {
    private val appContext: Context = context.applicationContext

    private fun getCurrentTimeStamp(): String? {
        return try {
            val sdf = SimpleDateFormat("dd-MM-yyyy:HH:mm:ss", Locale.ENGLISH)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            sdf.format(Date())
        } catch (e: Exception) {
            ""
        }
    }

    private fun getAppVersionName(): String? {
        return try {
            val context: Context = appContext
            val manager = context.packageManager
            val info = manager?.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)
            info?.versionName
        } catch (e: java.lang.Exception) {
            ""
        }
    }

    private fun getPlatformName(): String? {
        return Build.MODEL
    }

    private fun getAndroidVersion(): String {
        return "Android " + Build.VERSION.SDK_INT
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val userAgent =
            "Daily Caller App (" + getAppVersionName() + ", " + getCurrentTimeStamp() + ", " + getAndroidVersion() + ", " + "Android " + getPlatformName() + ")"
        Log.e("UserAgent added", userAgent)
        val originalRequest = chain.request()
        val requestWithUserAgent =
            originalRequest.newBuilder().header("User-agent", userAgent).build()
        return chain.proceed(requestWithUserAgent)
    }
}
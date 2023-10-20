package com.tsciences.dailycaller.android.utils

import android.text.Html
import com.google.gson.Gson
import com.tsciences.dailycaller.android.data.utils.ShareWhileSubscribed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

fun <T : Any> String.fromJson(destination: Class<T>): T {
    return Gson().fromJson(this, destination)
}

fun Any.toJsonString(): String {
    return Gson().toJson(this)
}

fun <T> Flow<T>.toStateFlow(
    scope: CoroutineScope, initialValue: T, sharingStarted: SharingStarted = ShareWhileSubscribed
): StateFlow<T> = stateIn(scope, sharingStarted, initialValue)

fun stripHtml(html: String?): String {
    return if (null != html) {
        Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        ""
    }
}

fun isValidLink(url: String): Boolean {
    return (url.contains("http://dailycaller.com") || url.contains("https://dailycaller.com") || url.contains(
        "https://www.dailycaller.com"
    ) || url.contains("http://www.dailycaller.com"))
}

fun getLinkFromSearchItem(resp: String): String? {
    return if (resp.contains("https")) {
        if (resp.contains("www")) {
            resp.replace("https://www.dailycaller.com", "")
        } else {
            resp.replace("https://dailycaller.com", "")
        }
    } else {
        if (resp.contains("www")) {
            resp.replace("http://www.dailycaller.com", "")
        } else {
            resp.replace("http://dailycaller.com", "")
        }
    }
}
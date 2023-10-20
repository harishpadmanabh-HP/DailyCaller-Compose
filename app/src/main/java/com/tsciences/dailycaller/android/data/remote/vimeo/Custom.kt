package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class Custom(
    @Expose
    @SerializedName("active")
    val active: Boolean,
    @Expose
    @SerializedName("link")
    val link: Any,
    @Expose
    @SerializedName("sticky")
    val sticky: Boolean,
    @Expose
    @SerializedName("url")
    val url: Any,
    @Expose
    @SerializedName("use_link")
    val useLink: Boolean
)
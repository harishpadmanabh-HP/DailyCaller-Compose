package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class Buttons(
    @Expose
    @SerializedName("embed")
    val embed: Boolean,
    @Expose
    @SerializedName("fullscreen")
    val fullscreen: Boolean,
    @Expose
    @SerializedName("hd")
    val hd: Boolean,
    @Expose
    @SerializedName("like")
    val like: Boolean,
    @Expose
    @SerializedName("scaling")
    val scaling: Boolean,
    @Expose
    @SerializedName("share")
    val share: Boolean,
    @Expose
    @SerializedName("watchlater")
    val watchlater: Boolean
)
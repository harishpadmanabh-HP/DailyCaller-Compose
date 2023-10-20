package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class PrivacyXX(
    @Expose
    @SerializedName("add")
    val add: Boolean,
    @Expose
    @SerializedName("comments")
    val comments: String,
    @Expose
    @SerializedName("download")
    val download: Boolean,
    @Expose
    @SerializedName("embed")
    val embed: String,
    @Expose
    @SerializedName("view")
    val view: String
)
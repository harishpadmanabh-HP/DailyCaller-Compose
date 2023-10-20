package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class Upload(
    @Expose
    @SerializedName("approach")
    val approach: Any,
    @Expose
    @SerializedName("form")
    val form: Any,
    @Expose
    @SerializedName("link")
    val link: Any,
    @Expose
    @SerializedName("redirect_url")
    val redirectUrl: Any,
    @Expose
    @SerializedName("size")
    val size: Any,
    @Expose
    @SerializedName("status")
    val status: String,
    @Expose
    @SerializedName("upload_link")
    val uploadLink: Any
)
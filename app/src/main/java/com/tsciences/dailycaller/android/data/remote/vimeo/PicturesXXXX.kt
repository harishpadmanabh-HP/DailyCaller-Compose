package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class PicturesXXXX(
    @Expose
    @SerializedName("active")
    val active: Boolean,
    @Expose
    @SerializedName("base_link")
    val baseLink: String,
    @Expose
    @SerializedName("default_picture")
    val defaultPicture: Boolean,
    @Expose
    @SerializedName("resource_key")
    val resourceKey: String,

    @Expose
    @SerializedName("type")
    val type: String,
    @Expose
    @SerializedName("uri")
    val uri: Any
)
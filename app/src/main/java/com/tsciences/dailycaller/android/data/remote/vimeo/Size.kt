package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class Size(
    @Expose
    @SerializedName("height")
    val height: Int,
    @Expose
    @SerializedName("link")
    val link: String,
    @Expose
    @SerializedName("width")
    val width: Int
)
package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class Logos(
    @Expose
    @SerializedName("custom")
    val custom: Custom,
    @Expose
    @SerializedName("vimeo")
    val vimeo: Boolean
)
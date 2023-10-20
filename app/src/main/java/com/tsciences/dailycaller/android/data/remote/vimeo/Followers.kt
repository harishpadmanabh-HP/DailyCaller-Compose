package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class Followers(
    @Expose
    @SerializedName("options")
    val options: List<String>,
    @Expose
    @SerializedName("total")
    val total: Int,
    @Expose
    @SerializedName("uri")
    val uri: String
)
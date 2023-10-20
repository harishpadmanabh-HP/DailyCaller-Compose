package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class VideosXX(
    @Expose
    @SerializedName("privacy")
    val privacy: PrivacyX,
    @Expose
    @SerializedName("rating")
    val rating: List<String>
)
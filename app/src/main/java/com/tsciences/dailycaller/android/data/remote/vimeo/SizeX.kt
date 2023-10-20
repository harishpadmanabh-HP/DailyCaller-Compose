package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class SizeX(
    @Expose
    @SerializedName("height")
    val height: Int,
    @Expose
    @SerializedName("link")
    val link: String,
    @Expose
    @SerializedName("link_with_play_button")
    val linkWithPlayButton: String,
    @Expose
    @SerializedName("width")
    val width: Int
)
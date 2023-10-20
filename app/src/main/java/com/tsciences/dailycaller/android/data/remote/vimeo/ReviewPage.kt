package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class ReviewPage(
    @Expose
    @SerializedName("active")
    val active: Boolean,
    @Expose
    @SerializedName("is_shareable")
    val isShareable: Boolean,
    @Expose
    @SerializedName("link")
    val link: String
)
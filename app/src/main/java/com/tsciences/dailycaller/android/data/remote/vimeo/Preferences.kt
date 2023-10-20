package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class Preferences(
    @Expose
    @SerializedName("videos")
    val videos: VideosXX,
    @Expose
    @SerializedName("webinar_registrant_lower_watermark_banner_dismissed")
    val webinarRegistrantLowerWatermarkBannerDismissed: List<Any>
)
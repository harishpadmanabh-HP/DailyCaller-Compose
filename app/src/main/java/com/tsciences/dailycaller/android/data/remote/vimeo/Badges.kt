package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class Badges(
    @Expose
    @SerializedName("hdr")
    val hdr: Boolean,
    @Expose
    @SerializedName("live")
    val live: Live,
    @Expose
    @SerializedName("staff_pick")
    val staffPick: StaffPick,
    @Expose
    @SerializedName("vod")
    val vod: Boolean,
    @Expose
    @SerializedName("weekend_challenge")
    val weekendChallenge: Boolean
)
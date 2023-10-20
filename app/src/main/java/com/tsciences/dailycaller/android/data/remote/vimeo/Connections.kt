package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class Connections(
    @Expose
    @SerializedName("albums")
    val albums: Albums,
    @Expose
    @SerializedName("available_albums")
    val availableAlbums: AvailableAlbums,
    @Expose
    @SerializedName("comments")
    val comments: Comments,
    @Expose
    @SerializedName("credits")
    val credits: Any,
    @Expose
    @SerializedName("likes")
    val likes: Likes,
    @Expose
    @SerializedName("pictures")
    val pictures: Pictures,
    @Expose
    @SerializedName("recommendations")
    val recommendations: Any,
    @Expose
    @SerializedName("related")
    val related: Any,
    @Expose
    @SerializedName("texttracks")
    val texttracks: Texttracks,
    @Expose
    @SerializedName("versions")
    val versions: Versions
)
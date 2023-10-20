package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class ConnectionsX(
    @Expose
    @SerializedName("ancestor_path")
    val ancestorPath: List<Any>,
    @Expose
    @SerializedName("folders")
    val folders: Folders,
    @Expose
    @SerializedName("items")
    val items: Items,
    @Expose
    @SerializedName("videos")
    val videos: Videos
)
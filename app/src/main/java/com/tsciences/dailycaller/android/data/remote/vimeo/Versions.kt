package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class Versions(
    @Expose
    @SerializedName("current_uri")
    val currentUri: String,
    @Expose
    @SerializedName("latest_incomplete_version")
    val latestIncompleteVersion: Any,
    @Expose
    @SerializedName("options")
    val options: List<String>,
    @Expose
    @SerializedName("resource_key")
    val resourceKey: String,
    @Expose
    @SerializedName("total")
    val total: Int,
    @Expose
    @SerializedName("uri")
    val uri: String
)
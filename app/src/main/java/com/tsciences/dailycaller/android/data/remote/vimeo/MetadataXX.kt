package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class MetadataXX(
    @Expose
    @SerializedName("connections")
    val connections: ConnectionsXX
)
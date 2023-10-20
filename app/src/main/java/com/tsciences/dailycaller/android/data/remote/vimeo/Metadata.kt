package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class Metadata(
    @Expose
    @SerializedName("connections")
    val connections: Connections,
    @Expose
    @SerializedName("interactions")
    val interactions: Interactions,
    @Expose
    @SerializedName("is_screen_record")
    val isScreenRecord: Boolean,
    @Expose
    @SerializedName("is_vimeo_create")
    val isVimeoCreate: Boolean
)
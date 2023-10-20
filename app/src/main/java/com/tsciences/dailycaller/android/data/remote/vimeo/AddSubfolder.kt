package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class AddSubfolder(
    @Expose
    @SerializedName("can_add_subfolders")
    val canAddSubfolders: Boolean,
    @Expose
    @SerializedName("content_type")
    val contentType: String,
    @Expose
    @SerializedName("options")
    val options: List<String>,
    @Expose
    @SerializedName("properties")
    val properties: List<PropertyX>,
    @Expose
    @SerializedName("subfolder_depth_limit_reached")
    val subfolderDepthLimitReached: Boolean,
    @Expose
    @SerializedName("uri")
    val uri: String
)
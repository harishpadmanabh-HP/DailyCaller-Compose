package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class ParentFolder(
    @Expose
    @SerializedName("access_grant")
    val accessGrant: Any,
    @Expose
    @SerializedName("created_time")
    val createdTime: String,
    @Expose
    @SerializedName("is_pinned")
    val isPinned: Boolean,
    @Expose
    @SerializedName("is_private_to_user")
    val isPrivateToUser: Boolean,
    @Expose
    @SerializedName("last_user_action_event_date")
    val lastUserActionEventDate: String,
    @Expose
    @SerializedName("link")
    val link: Any,
    @Expose
    @SerializedName("metadata")
    val metadata: MetadataX,
    @Expose
    @SerializedName("modified_time")
    val modifiedTime: String,
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("pinned_on")
    val pinnedOn: Any,
    @Expose
    @SerializedName("privacy")
    val privacy: Privacy,
    @Expose
    @SerializedName("resource_key")
    val resourceKey: String,
    @Expose
    @SerializedName("uri")
    val uri: String,
    @Expose
    @SerializedName("user")
    val user: User
)
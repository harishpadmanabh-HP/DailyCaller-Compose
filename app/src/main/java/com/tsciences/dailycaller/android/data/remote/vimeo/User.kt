package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class User(
    @Expose
    @SerializedName("account")
    val account: String,
    @Expose
    @SerializedName("available_for_hire")
    val availableForHire: Boolean,
    @Expose
    @SerializedName("bio")
    val bio: Any,
    @Expose
    @SerializedName("can_work_remotely")
    val canWorkRemotely: Boolean,
    @Expose
    @SerializedName("capabilities")
    val capabilities: Capabilities,
    @Expose
    @SerializedName("content_filter")
    val contentFilter: List<String>,
    @Expose
    @SerializedName("created_time")
    val createdTime: String,
    @Expose
    @SerializedName("gender")
    val gender: String,
    @Expose
    @SerializedName("link")
    val link: String,
    @Expose
    @SerializedName("location")
    val location: String,
    @Expose
    @SerializedName("location_details")
    val locationDetails: LocationDetails,
    @Expose
    @SerializedName("metadata")
    val metadata: MetadataXX,
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("pictures")
    val pictures: PicturesXX,
    @Expose
    @SerializedName("preferences")
    val preferences: Preferences,
    @Expose
    @SerializedName("resource_key")
    val resourceKey: String,
    @Expose
    @SerializedName("short_bio")
    val shortBio: Any,
    @Expose
    @SerializedName("skills")
    val skills: List<Any>,
    @Expose
    @SerializedName("uri")
    val uri: String,
    @Expose
    @SerializedName("websites")
    val websites: List<Any>
)
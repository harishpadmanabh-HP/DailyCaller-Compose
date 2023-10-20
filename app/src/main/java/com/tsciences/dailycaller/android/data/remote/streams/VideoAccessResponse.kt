package com.tsciences.dailycaller.android.data.remote.streams

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class VideoAccessResponse(
    @Expose
    @SerializedName("accesses")
    val accesses: List<Accesse>,
    @Expose
    @SerializedName("code")
    val code: Int,
    @Expose
    @SerializedName("count")
    val count: Int,
    @Expose
    @SerializedName("limit")
    val limit: Int,
    @Expose
    @SerializedName("offset")
    val offset: Int,
    @Expose
    @SerializedName("total")
    val total: Int,
    @Expose
    @SerializedName("ts")
    val ts: Int
)

@Keep
data class Accesse(
    @Expose
    @SerializedName("access_id")
    val accessId: String,
    @Expose
    @SerializedName("can_revoke_access")
    val canRevokeAccess: Boolean,
    @Expose
    @SerializedName("custom_data")
    val customData: Any,
    @Expose
    @SerializedName("granted")
    val granted: Boolean,
    @Expose
    @SerializedName("parent_access_id")
    val parentAccessId: Any,
    @Expose
    @SerializedName("resource")
    val resource: Resource,
    @Expose
    @SerializedName("start_date")
    val startDate: Int,
    @Expose
    @SerializedName("user")
    val user: User1
)

@Keep

data class User1(
    @Expose
    @SerializedName("create_date")
    val createDate: Int,
    @Expose
    @SerializedName("email")
    val email: String,
    @Expose
    @SerializedName("first_name")
    val firstName: String,
    @Expose
    @SerializedName("image1")
    val image1: Any,
    @Expose
    @SerializedName("last_name")
    val lastName: String,
    @Expose
    @SerializedName("personal_name")
    val personalName: String,
    @Expose
    @SerializedName("uid")
    val uid: String
)

@Keep
data class Resource(
    @Expose
    @SerializedName("aid")
    val aid: String,
    @Expose
    @SerializedName("bundle_type")
    val bundleType: String,
    @Expose
    @SerializedName("bundle_type_label")
    val bundleTypeLabel: String,
    @Expose
    @SerializedName("create_date")
    val createDate: Int,
    @Expose
    @SerializedName("deleted")
    val deleted: Boolean,
    @Expose
    @SerializedName("description")
    val description: String,
    @Expose
    @SerializedName("disabled")
    val disabled: Boolean,
    @Expose
    @SerializedName("external_id")
    val externalId: Any,
    @Expose
    @SerializedName("image_url")
    val imageUrl: String,
    @Expose
    @SerializedName("is_fbia_resource")
    val isFbiaResource: Boolean,
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("publish_date")
    val publishDate: Int,
    @Expose
    @SerializedName("purchase_url")
    val purchaseUrl: Any,
    @Expose
    @SerializedName("resource_url")
    val resourceUrl: String,
    @Expose
    @SerializedName("rid")
    val rid: String,
    @Expose
    @SerializedName("type")
    val type: String,
    @Expose
    @SerializedName("type_label")
    val typeLabel: String,
    @Expose
    @SerializedName("update_date")
    val updateDate: Int
)
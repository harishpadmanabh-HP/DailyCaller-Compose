package com.tsciences.dailycaller.android.data.remote.streams

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class UserIdResponse(
    @Expose
    @SerializedName("code")
    val code: Int,
    @Expose
    @SerializedName("ts")
    val ts: Int,
    @Expose
    @SerializedName("user")
    val user: User
)

@Keep
data class User(
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
    @SerializedName("last_name")
    val lastName: String,
    @Expose
    @SerializedName("personal_name")
    val personalName: String,
    @Expose
    @SerializedName("uid")
    val uid: String
)
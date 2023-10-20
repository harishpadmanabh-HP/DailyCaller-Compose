package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class Interactions(
    @Expose
    @SerializedName("can_update_privacy_to_public")
    val canUpdatePrivacyToPublic: CanUpdatePrivacyToPublic,
    @Expose
    @SerializedName("delete")
    val delete: Delete,
    @Expose
    @SerializedName("edit")
    val edit: Edit,
    @Expose
    @SerializedName("edit_content_rating")
    val editContentRating: EditContentRating,
    @Expose
    @SerializedName("edit_privacy")
    val editPrivacy: EditPrivacy,
    @Expose
    @SerializedName("invite")
    val invite: Invite,
    @Expose
    @SerializedName("report")
    val report: Report,
    @Expose
    @SerializedName("trim")
    val trim: Trim,
    @Expose
    @SerializedName("validate")
    val validate: Validate,
    @Expose
    @SerializedName("view_team_members")
    val viewTeamMembers: ViewTeamMembers,
    @Expose
    @SerializedName("watchlater")
    val watchlater: Watchlater
)
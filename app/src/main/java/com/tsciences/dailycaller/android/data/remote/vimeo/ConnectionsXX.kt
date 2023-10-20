package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class ConnectionsXX(
    @Expose
    @SerializedName("albums")
    val albums: Albums,
    @Expose
    @SerializedName("appearances")
    val appearances: Appearances,
    @Expose
    @SerializedName("block")
    val block: Block,
    @Expose
    @SerializedName("categories")
    val categories: Categories,
    @Expose
    @SerializedName("channels")
    val channels: Channels,
    @Expose
    @SerializedName("feed")
    val feed: Feed,

    @Expose
    @SerializedName("folders_root")
    val foldersRoot: FoldersRoot,
    @Expose
    @SerializedName("followers")
    val followers: Followers,
    @Expose
    @SerializedName("following")
    val following: Following,
    @Expose
    @SerializedName("groups")
    val groups: Groups,

    @Expose
    @SerializedName("membership")
    val membership: Membership,
    @Expose
    @SerializedName("moderated_channels")
    val moderatedChannels: ModeratedChannels,
    @Expose
    @SerializedName("permission_policies")
    val permissionPolicies: PermissionPolicies,

    @Expose
    @SerializedName("portfolios")
    val portfolios: Portfolios,
    @Expose
    @SerializedName("shared")
    val shared: Shared,
    @Expose
    @SerializedName("teams")
    val teams: Teams,

    @Expose
    @SerializedName("watched_videos")
    val watchedVideos: WatchedVideos,
    @Expose
    @SerializedName("watchlater")
    val watchlater: WatchlaterX
)
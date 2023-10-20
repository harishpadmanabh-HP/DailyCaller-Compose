package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class VimeoUrlResponse(
    @Expose
    @SerializedName("app")
    val app: App,
    @Expose
    @SerializedName("categories")
    val categories: List<Any>,
    @Expose
    @SerializedName("content_rating")
    val contentRating: List<String>,
    @Expose
    @SerializedName("content_rating_class")
    val contentRatingClass: String,
    @Expose
    @SerializedName("created_time")
    val createdTime: String,
    @Expose
    @SerializedName("description")
    val description: Any,
    @Expose
    @SerializedName("duration")
    val duration: Int,
    @Expose
    @SerializedName("embed")
    val embed: Embed,
    @Expose
    @SerializedName("has_audio")
    val hasAudio: Boolean,
    @Expose
    @SerializedName("height")
    val height: Int,
    @Expose
    @SerializedName("is_playable")
    val isPlayable: Boolean,
    @Expose
    @SerializedName("language")
    val language: Any,
    @Expose
    @SerializedName("last_user_action_event_date")
    val lastUserActionEventDate: String,
    @Expose
    @SerializedName("license")
    val license: Any,
    @Expose
    @SerializedName("link")
    val link: String,
    @Expose
    @SerializedName("manage_link")
    val manageLink: String,
    @Expose
    @SerializedName("metadata")
    val metadata: Metadata,
    @Expose
    @SerializedName("modified_time")
    val modifiedTime: String,
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("parent_folder")
    val parentFolder: ParentFolder,
    @Expose
    @SerializedName("pictures")
    val pictures: PicturesXXX,
    @Expose
    @SerializedName("play")
    val play: Play,
    @Expose
    @SerializedName("player_embed_url")
    val playerEmbedUrl: String,
    @Expose
    @SerializedName("privacy")
    val privacy: PrivacyXX,
    @Expose
    @SerializedName("rating_mod_locked")
    val ratingModLocked: Boolean,
    @Expose
    @SerializedName("release_time")
    val releaseTime: String,
    @Expose
    @SerializedName("resource_key")
    val resourceKey: String,
    @Expose
    @SerializedName("review_page")
    val reviewPage: ReviewPage,
    @Expose
    @SerializedName("stats")
    val stats: Stats,
    @Expose
    @SerializedName("status")
    val status: String,
    @Expose
    @SerializedName("tags")
    val tags: List<Any>,
    @Expose
    @SerializedName("transcode")
    val transcode: Transcode,
    @Expose
    @SerializedName("type")
    val type: String,
    @Expose
    @SerializedName("upload")
    val upload: Upload,
    @Expose
    @SerializedName("uploader")
    val uploader: Uploader,
    @Expose
    @SerializedName("uri")
    val uri: String,
    @Expose
    @SerializedName("user")
    val user: UserX,
    @Expose
    @SerializedName("width")
    val width: Int
)
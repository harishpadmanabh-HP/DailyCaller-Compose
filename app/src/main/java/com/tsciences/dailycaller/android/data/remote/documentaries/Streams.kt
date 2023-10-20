package com.tsciences.dailycaller.android.data.remote.documentaries


import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class Streams(
    @Expose
    @SerializedName("stream")
    val stream: List<Stream>
)

@Keep
data class Stream(
    @Expose
    @SerializedName("creators")
    val creators: String,
    @Expose
    @SerializedName("mainImage")
    val mainImage: String,
    @Expose
    @SerializedName("minutes")
    val minutes: Int,
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("resImages")
    val resImages: List<Any>,
    @Expose
    @SerializedName("slug")
    val slug: String,
    @Expose
    @SerializedName("summary")
    val summary: String,
    @Expose
    @SerializedName("thumImage")
    val thumImage: String,
    @Expose
    @SerializedName("title")
    val title: String,
    @Expose
    @SerializedName("videoUrl")
    val videoUrl: String,
    @Expose
    @SerializedName("fullVideoUrl")
    val fullVideoUrl: String
)
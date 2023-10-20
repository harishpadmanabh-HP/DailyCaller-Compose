package com.tsciences.dailycaller.android.data.remote.search

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class SearchResponse(
    @SerializedName("items")
    @Expose
    var items: List<SearchItem> = emptyList()
)

@Keep
data class SearchItem(
    @SerializedName("title")
    @Expose
    val title: String? = null,

    @SerializedName("link")
    @Expose
    var link: String? = null,

    @SerializedName("snippet")
    @Expose
    var snippet: String? = null,

    @SerializedName("pagemap")
    @Expose
    val pagemap: Pagemap? = null
)

@Keep
data class Pagemap(
    @SerializedName("cse_thumbnail")
    @Expose
    var cseThumbnail: List<CseThumbnail> = emptyList(),

    @SerializedName("cse_image")
    @Expose
    val cse_image: List<Cse_image> = emptyList()
)

@Keep
data class Cse_image(
    @SerializedName("src")
    @Expose
    val src: String? = null
)

@Keep
data class CseThumbnail(
    @SerializedName("width")
    @Expose
    var width: String? = null,

    @SerializedName("height")
    @Expose
    val height: String? = null,

    @SerializedName("src")
    @Expose
    val src: String? = null
)
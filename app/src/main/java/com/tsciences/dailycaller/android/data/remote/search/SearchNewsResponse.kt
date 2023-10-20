package com.tsciences.dailycaller.android.data.remote.search

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tsciences.dailycaller.android.data.remote.home.Item

@Keep
data class SearchNewsResponse(
    @Expose
    @SerializedName("title")
    var title: String? = null,

    @Expose
    @SerializedName("description")
    val description: String? = null,

    @Expose
    @SerializedName("item")
    val newsItem: Item? = null
)



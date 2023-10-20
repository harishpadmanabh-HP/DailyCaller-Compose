package com.tsciences.dailycaller.android.data.remote.home

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class MenuResponse(
    @SerializedName("link") @Expose val link: List<MenuItem> = emptyList()
)

@Keep
data class MenuItem(
    @SerializedName("_href") @Expose var href: String? = null,

    @SerializedName("_title") @Expose val title: String? = null,

    @SerializedName("link") @Expose val link: List<MenuSubItem>? = null,

    var isSelected: Boolean = false
)

@Keep
data class MenuSubItem(
    @SerializedName("_href") @Expose var href: String? = null,

    @SerializedName("_title") @Expose val title: String? = null,

    var id: Int
)



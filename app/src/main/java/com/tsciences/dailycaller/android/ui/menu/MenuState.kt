package com.tsciences.dailycaller.android.ui.menu

import com.tsciences.dailycaller.android.data.remote.documentaries.Stream
import com.tsciences.dailycaller.android.data.remote.home.Item
import com.tsciences.dailycaller.android.database.NewsModel

data class MenuState(
    val newsFullItems: List<Item> = emptyList(),
    val savedNews: List<NewsModel?> = emptyList(),
    val listPage: Int = 0,
    val hasListEndReached: Boolean = false,
    val loading: Boolean = false,
    val streams: List<Stream> = emptyList(),
    val menuTitle: String = ""
)
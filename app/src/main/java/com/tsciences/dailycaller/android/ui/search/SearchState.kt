package com.tsciences.dailycaller.android.ui.search

import com.tsciences.dailycaller.android.data.remote.search.SearchItem

data class SearchState(
    val searchItemList: List<SearchItem> = emptyList(),
    val listPage: Int = 0,
    val hasListEndReached: Boolean = false,
    val loading: Boolean = false
)
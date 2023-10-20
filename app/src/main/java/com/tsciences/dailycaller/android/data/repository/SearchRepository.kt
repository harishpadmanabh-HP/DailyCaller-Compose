package com.tsciences.dailycaller.android.data.repository

import com.tsciences.dailycaller.android.data.remote.search.SearchApi
import com.tsciences.dailycaller.android.data.remote.search.SearchItem
import com.tsciences.dailycaller.android.data.utils.NetworkResult
import com.tsciences.dailycaller.android.data.utils.tryApiCall

class SearchRepository(
    private val api: SearchApi
) {
    suspend fun getGoogleSearch(
        query: String,
        apiKey: String,
        cx: String,
        start: Int
    ): NetworkResult<List<SearchItem>> = tryApiCall {
        val response = api.getGoogleSearchList(
            query,
            cx,
            start
        )
        NetworkResult.Success(response.items)
    }
}
package com.tsciences.dailycaller.android.data.repository

import com.tsciences.dailycaller.android.data.remote.home.HomeApi
import com.tsciences.dailycaller.android.data.remote.home.Item
import com.tsciences.dailycaller.android.data.remote.home.MenuResponse
import com.tsciences.dailycaller.android.data.utils.NetworkResult
import com.tsciences.dailycaller.android.data.utils.tryApiCall

class HomeRepository(
    private val api: HomeApi
) {
    suspend fun getFullNewsList(
        pageId: Int,
        maxPosts: Int
    ): NetworkResult<List<Item>> = tryApiCall {
        val response = api.getFullNewsList(
            pageId = pageId,
            maxPosts = maxPosts
        )
        NetworkResult.Success(response.newsItem)
    }

    suspend fun getSectionNewsList(
        type: String,
        pageId: Int,
        maxPosts: Int
    ): NetworkResult<List<Item>> = tryApiCall {
        val response = api.getSectionNewsList(
            type = type,
            pageId = pageId,
            maxPosts = maxPosts
        )
        NetworkResult.Success(response.newsItem)
    }

    suspend fun getSearchNewsDetails(
        searchLink: String
    ): NetworkResult<Item?> = tryApiCall {
        val response = api.getSearchNewsDetails(
            singlePostString = searchLink
        )
        NetworkResult.Success(response.newsItem)
    }

    suspend fun getMenuDetails(
    ): NetworkResult<MenuResponse> = tryApiCall {
        val response = api.getMenuItems(
        )
        NetworkResult.Success(response)
    }
}
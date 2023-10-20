package com.tsciences.dailycaller.android.data.repository

import com.tsciences.dailycaller.android.data.remote.home.Item
import com.tsciences.dailycaller.android.data.remote.menuSection.MenuSectionApi
import com.tsciences.dailycaller.android.data.utils.NetworkResult
import com.tsciences.dailycaller.android.data.utils.tryApiCall

class MenuSectionRepository(
    private val api: MenuSectionApi
) {
    suspend fun getMenuNewsList(
        menuTerm: String,
        pageId: Int,
        maxPosts: Int
    ): NetworkResult<List<Item>> = tryApiCall {
        val response = api.getMenuNewsList(
            menuTerm = menuTerm,
            pageId = pageId,
            maxPosts = maxPosts
        )
        NetworkResult.Success(response.newsItem)
    }

    suspend fun getMenuNewsPremiumTwosides(
        menuTerm: String,
        pageId: Int,
        maxPosts: Int
    ): NetworkResult<List<Item>> = tryApiCall {
        val response = api.getMenuNewsPremiumTwoSides(
            menuTerm = menuTerm,
            pageId = pageId,
            maxPosts = maxPosts
        )
        NetworkResult.Success(response.newsItem)
    }
}
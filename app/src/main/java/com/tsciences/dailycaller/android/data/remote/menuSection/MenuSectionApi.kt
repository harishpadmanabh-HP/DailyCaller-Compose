package com.tsciences.dailycaller.android.data.remote.menuSection

import com.tsciences.dailycaller.android.data.remote.home.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MenuSectionApi {
    @GET("section/{type}")
    suspend fun getMenuNewsList(
        @Path("type") menuTerm: String,
        @Query("feed") feed: String = "fullsection",
        @Query("json") json: String = "yes",
        @Query("paged") pageId: Int?,
        @Query("max_posts") maxPosts: Int?
    ): NewsResponse

    @GET("?feed=fulltaxonomy")
    suspend fun getMenuNewsPremiumTwoSides(
        @Query("term0") menuTerm: String?,
        @Query("taxonomy0") taxonomy0: String = "category",
        @Query("operator0") operator0: String = "IN",
        @Query("json") json: String = "yes",
        @Query("paged") pageId: Int?,
        @Query("max_posts") maxPosts: Int?
    ): NewsResponse
}
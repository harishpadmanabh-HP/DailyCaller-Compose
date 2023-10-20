package com.tsciences.dailycaller.android.data.remote.home

import com.tsciences.dailycaller.android.data.remote.search.SearchNewsResponse
import retrofit2.http.*

interface HomeApi {
    @GET("feed/full")
    suspend fun getFullNewsList(
        @Query("json") json: String = "yes",
        @Query("paged") pageId: Int,
        @Query("max_posts") maxPosts: Int
    ): NewsResponse

    @GET("section/{type}")
    suspend fun getSectionNewsList(
        @Path("type") type: String,
        @Query("feed") feed: String = "fullsection",
        @Query("json") json: String = "yes",
        @Query("paged") pageId: Int,
        @Query("max_posts") maxPosts: Int
    ): NewsResponse

    @GET("?feed=singlepost")
    suspend fun getSearchNewsDetails(
        @Query("json") json: String = "yes",
        @Query("singlepost") singlePostString: String,
    ): SearchNewsResponse

    @GET("?feed=menu")
    suspend fun getMenuItems(
        @Query("json") json: String? = "yes",
    ): MenuResponse
}
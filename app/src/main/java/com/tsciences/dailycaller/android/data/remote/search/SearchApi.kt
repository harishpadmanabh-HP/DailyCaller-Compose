package com.tsciences.dailycaller.android.data.remote.search

import retrofit2.http.*

interface SearchApi {
    @GET("customsearch/v1")
    suspend fun getGoogleSearchList(
        @Query("q") query: String,
        @Query("cx") cx: String,
        @Query("start") start: Int
    ): SearchResponse
}
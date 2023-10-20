package com.tsciences.dailycaller.android.data.remote.vimeo

import retrofit2.http.*

interface VimeoApi {
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("videos/{videoId}")
    suspend fun getVimeoUrl( @Path("videoId") videoId: String,@Header("Authorization") token: String
    ): VimeoUrlResponse
}
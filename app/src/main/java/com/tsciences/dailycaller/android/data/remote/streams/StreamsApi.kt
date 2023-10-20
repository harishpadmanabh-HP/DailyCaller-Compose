package com.tsciences.dailycaller.android.data.remote.streams

import retrofit2.http.*

interface StreamsApi {
    @POST("api/v3/anon/user/get")
    suspend fun getUserId(
        @Query("aid") aid: String, @Query("user_token") userToken: String
    ): UserIdResponse

    @POST("api/v3/publisher/user/access/list")
    suspend fun getUserAccess(
        @Query("aid") aid: String, @Query("uid") uid: String, @Query("api_token") apiToken: String
    ): VideoAccessResponse
}
package com.tsciences.dailycaller.android.data.repository

import com.tsciences.dailycaller.android.data.remote.streams.StreamsApi
import com.tsciences.dailycaller.android.data.remote.streams.UserIdResponse
import com.tsciences.dailycaller.android.data.remote.streams.VideoAccessResponse
import com.tsciences.dailycaller.android.data.utils.NetworkResult
import com.tsciences.dailycaller.android.data.utils.tryApiCall

class DocumentaryRepository(
    private val api: StreamsApi
) {
    suspend fun getUserId(
        aid: String, userToken: String
    ): NetworkResult<UserIdResponse> = tryApiCall {
        val response = api.getUserId(aid, userToken)
        NetworkResult.Success(response)
    }

    suspend fun getUserAccess(
        aid: String, uid: String, apiToken: String
    ): NetworkResult<VideoAccessResponse> = tryApiCall {
        val response = api.getUserAccess(aid, uid, apiToken)
        NetworkResult.Success(response)
    }
}
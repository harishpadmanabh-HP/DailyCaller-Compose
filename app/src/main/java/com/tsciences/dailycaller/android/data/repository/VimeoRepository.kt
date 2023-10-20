package com.tsciences.dailycaller.android.data.repository

import com.tsciences.dailycaller.android.data.remote.vimeo.VimeoApi
import com.tsciences.dailycaller.android.data.remote.vimeo.VimeoUrlResponse
import com.tsciences.dailycaller.android.data.utils.NetworkResult
import com.tsciences.dailycaller.android.data.utils.tryApiCall

class VimeoRepository(
    private val api: VimeoApi
) {
    suspend fun getVimeoVideoUrl(
        videoId: String
    ): NetworkResult<VimeoUrlResponse> = tryApiCall {
        val response = api.getVimeoUrl(videoId, "Bearer 661abbe302a8f6c7e6c08ff029b68e15")
        NetworkResult.Success(response)
    }
}
package com.tsciences.dailycaller.android.data.repository

import com.tsciences.dailycaller.android.data.remote.documentaries.DocumentariesApi
import com.tsciences.dailycaller.android.data.remote.documentaries.Streams
import com.tsciences.dailycaller.android.data.utils.NetworkResult
import com.tsciences.dailycaller.android.data.utils.tryApiCall

class StreamRepository(
    private val api: DocumentariesApi
) {
    suspend fun getStreamDocumentaries(
    ): NetworkResult<Streams> = tryApiCall {
        val response = api.getStreamDocumentaries()
        NetworkResult.Success(response)
    }
}
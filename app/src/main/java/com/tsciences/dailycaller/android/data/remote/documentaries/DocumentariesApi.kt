package com.tsciences.dailycaller.android.data.remote.documentaries

import retrofit2.http.*

interface DocumentariesApi {
    @GET("streams.json")
    suspend fun getStreamDocumentaries(
    ): Streams
}
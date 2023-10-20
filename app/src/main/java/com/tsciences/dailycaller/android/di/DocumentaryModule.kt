package com.tsciences.dailycaller.android.di

import com.tsciences.dailycaller.android.data.remote.streams.StreamsApi
import com.tsciences.dailycaller.android.data.repository.DocumentaryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object DocumentaryModule {

    @Provides
    fun provideDocumentaryApi(@PianoInterceptorOkHttpClient retrofit: Retrofit): StreamsApi =
        retrofit.create(StreamsApi::class.java)

    @Provides
    fun provideDocumentaryRepository(
        api: StreamsApi
    ): DocumentaryRepository = DocumentaryRepository(api)
}
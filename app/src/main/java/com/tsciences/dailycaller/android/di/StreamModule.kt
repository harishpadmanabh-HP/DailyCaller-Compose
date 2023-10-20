package com.tsciences.dailycaller.android.di

import com.tsciences.dailycaller.android.data.remote.documentaries.DocumentariesApi
import com.tsciences.dailycaller.android.data.repository.StreamRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object StreamModule {

    @Provides
    fun provideStreamApi(@StreamInterceptorOkHttpClient retrofit: Retrofit): DocumentariesApi =
        retrofit.create(DocumentariesApi::class.java)

    @Provides
    fun provideStreamRepository(
        api: DocumentariesApi
    ): StreamRepository = StreamRepository(api)
}
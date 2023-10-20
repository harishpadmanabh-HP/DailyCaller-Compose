package com.tsciences.dailycaller.android.di

import com.tsciences.dailycaller.android.data.remote.search.SearchApi
import com.tsciences.dailycaller.android.data.repository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object SearchModule {

    @Provides
    fun provideSearchApi(@GoogleInterceptorOkHttpClient retrofit: Retrofit): SearchApi =
        retrofit.create(SearchApi::class.java)

    @Provides
    fun provideSearchRepository(
        api: SearchApi
    ): SearchRepository = SearchRepository(api)
}
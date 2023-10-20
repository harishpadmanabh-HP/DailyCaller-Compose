package com.tsciences.dailycaller.android.di

import com.tsciences.dailycaller.android.data.remote.home.HomeApi
import com.tsciences.dailycaller.android.data.repository.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Provides
    fun provideHomeApi(@BaseUrlInterceptorOkHttpClient retrofit: Retrofit): HomeApi =
        retrofit.create(HomeApi::class.java)

    @Provides
    fun provideHomeRepository(
        api: HomeApi
    ): HomeRepository = HomeRepository(api)
}
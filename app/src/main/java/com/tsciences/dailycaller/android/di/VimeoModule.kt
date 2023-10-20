package com.tsciences.dailycaller.android.di

import com.tsciences.dailycaller.android.data.remote.vimeo.VimeoApi
import com.tsciences.dailycaller.android.data.repository.VimeoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object VimeoModule {

    @Provides
    fun provideVimeoApi(@VimeoInterceptorOkHttpClient retrofit: Retrofit): VimeoApi =
        retrofit.create(VimeoApi::class.java)

    @Provides
    fun provideVimeoRepository(
        api: VimeoApi
    ): VimeoRepository = VimeoRepository(api)
}
package com.tsciences.dailycaller.android.di

import com.tsciences.dailycaller.android.data.remote.menuSection.MenuSectionApi
import com.tsciences.dailycaller.android.data.repository.MenuSectionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object MenuSectionModule {

    @Provides
    fun provideMenuSectionApi(@BaseUrlInterceptorOkHttpClient retrofit: Retrofit): MenuSectionApi =
        retrofit.create(MenuSectionApi::class.java)

    @Provides
    fun provideMenuSectionRepository(
        api: MenuSectionApi
    ): MenuSectionRepository = MenuSectionRepository(api)
}
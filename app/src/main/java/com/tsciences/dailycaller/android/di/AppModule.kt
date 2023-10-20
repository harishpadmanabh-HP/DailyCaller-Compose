package com.tsciences.dailycaller.android.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import com.tsciences.dailycaller.android.BuildConfig
import com.tsciences.dailycaller.android.application.DailyCallerApplication
import com.tsciences.dailycaller.android.data.interceptors.NetworkConnectionInterceptor
import com.tsciences.dailycaller.android.data.preferences.SharedPrefService
import com.tsciences.dailycaller.android.data.remote.interceptors.TokenInterceptor
import com.tsciences.dailycaller.android.data.remote.interceptors.UserSegmentInterceptor
import com.tsciences.dailycaller.android.database.DailyCallerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        //SharedPreferences.
        return context.getSharedPreferences("dailyCaller", Context.MODE_PRIVATE)
    }

    @Provides
    fun provideSharedPrefService(sharedPreferences: SharedPreferences): SharedPrefService =
        SharedPrefService(sharedPreferences)

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideNetworkInterceptor(
        @ApplicationContext context: Context
    ): NetworkConnectionInterceptor = NetworkConnectionInterceptor(context)

    @Provides
    fun provideUserSegmentInterceptor(
        @ApplicationContext context: Context
    ): UserSegmentInterceptor = UserSegmentInterceptor(context)

    @Provides
    fun provideTokenInterceptor(
    ): TokenInterceptor = TokenInterceptor()

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.HEADERS
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        DailyCallerDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideNewsDao(db: DailyCallerDatabase) = db.newsDao()

    @Provides
    fun provideOkHttpClient(
        tokenInterceptor: TokenInterceptor,
        networkConnectionInterceptor: NetworkConnectionInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        userSegmentInterceptor: UserSegmentInterceptor
    ): OkHttpClient = OkHttpClient.Builder().connectTimeout(30L, TimeUnit.SECONDS)
        .readTimeout(30L, TimeUnit.SECONDS).writeTimeout(30L, TimeUnit.SECONDS)
        .addInterceptor(networkConnectionInterceptor).addInterceptor(tokenInterceptor)
        .addInterceptor(loggingInterceptor).addInterceptor(userSegmentInterceptor).also {
            if (BuildConfig.DEBUG) it.addInterceptor(OkHttpProfilerInterceptor())
        }.build()

    @Singleton
    @Provides
    @BaseUrlInterceptorOkHttpClient
    fun provideRetrofitInstance(
        client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(client).addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
            )
        ).build()

    @Singleton
    @Provides
    @GoogleInterceptorOkHttpClient
    fun provideGoogleRetrofitInstance(
        client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder().baseUrl(BuildConfig.GOOGLE_BASE_URL).client(client).addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
            )
        ).build()

    @Singleton
    @Provides
    @StreamInterceptorOkHttpClient
    fun provideStreamRetrofitInstance(
        client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder().baseUrl(BuildConfig.STREAM_BASE_URL).client(client).addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
            )
        ).build()

    @Singleton
    @Provides
    @VimeoInterceptorOkHttpClient
    fun provideVimeoRetrofitInstance(
        client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder().baseUrl(BuildConfig.VIMEO_BASE_URL).client(client).addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
            )
        ).build()

    @Singleton
    @Provides
    @PianoInterceptorOkHttpClient
    fun providePianoRetrofitInstance(
        client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder().baseUrl(BuildConfig.PIANO_BASE_URL).client(client).addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
            )
        ).build()

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): DailyCallerApplication {
        return app as DailyCallerApplication
    }
}

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION, AnnotationTarget.EXPRESSION)
@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class BaseUrlInterceptorOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GoogleInterceptorOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class StreamInterceptorOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class VimeoInterceptorOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PianoInterceptorOkHttpClient
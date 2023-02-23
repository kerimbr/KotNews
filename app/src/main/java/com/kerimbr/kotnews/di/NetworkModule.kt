package com.kerimbr.kotnews.di

import com.kerimbr.kotnews.BuildConfig
import com.kerimbr.kotnews.core.constants.BASE_URL
import com.kerimbr.kotnews.core.constants.CONNECT_TIMEOUT
import com.kerimbr.kotnews.core.constants.READ_TIMEOUT
import com.kerimbr.kotnews.core.constants.WRITE_TIMEOUT
import com.kerimbr.kotnews.data.remote.api.NewsAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        builder.addInterceptor {
            val request = it
                .request()
                .newBuilder()
                .build()
            it.proceed(request)
        }
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideNewsAPIService(retrofit: Retrofit): NewsAPIService {
        return retrofit.create(NewsAPIService::class.java)
    }

}
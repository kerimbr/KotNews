package com.kerimbr.kotnews.di

import com.kerimbr.kotnews.data.remote.api.NewsAPIService
import com.kerimbr.kotnews.data.remote.data_source.NewsRemoteDataSource
import com.kerimbr.kotnews.data.remote.data_source.NewsRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RemoteDataModule {

    @Singleton
    @Provides
    fun provideNewsRemoteDataSource(newsAPIService: NewsAPIService): NewsRemoteDataSource {
        return NewsRemoteDataSourceImpl(newsAPIService)
    }

}
package com.kerimbr.kotnews.di


import com.kerimbr.kotnews.data.local.cache.CacheManager
import com.kerimbr.kotnews.data.local.data_source.NewsLocalDataSource
import com.kerimbr.kotnews.data.remote.data_source.NewsRemoteDataSource
import com.kerimbr.kotnews.domain.reposityory.NewsRepository
import com.kerimbr.kotnews.domain.reposityory.NewsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideNewsRepository(
        newsRemoteDataSource: NewsRemoteDataSource,
        newsLocalDataSource: NewsLocalDataSource,
        cacheManager: CacheManager
    ): NewsRepository {
        return NewsRepositoryImpl(newsRemoteDataSource, newsLocalDataSource,cacheManager)
    }

}
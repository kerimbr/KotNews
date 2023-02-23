package com.kerimbr.kotnews.di

import android.app.Application
import com.kerimbr.kotnews.data.db.ArticleDAO
import com.kerimbr.kotnews.data.local.cache.CacheManager
import com.kerimbr.kotnews.data.local.cache.CacheManagerImpl
import com.kerimbr.kotnews.data.local.data_source.NewsLocalDataSource
import com.kerimbr.kotnews.data.local.data_source.NewsLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class LocaleDataModule {

    @Singleton
    @Provides
    fun provideLocaleDataSource(articleDao: ArticleDAO): NewsLocalDataSource {
        return NewsLocalDataSourceImpl(articleDao)
    }

    @Singleton
    @Provides
    fun provideCacheManager(application: Application) : CacheManager{
        return CacheManagerImpl(application)
    }

}
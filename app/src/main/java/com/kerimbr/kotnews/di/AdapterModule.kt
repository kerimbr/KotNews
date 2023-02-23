package com.kerimbr.kotnews.di

import com.kerimbr.kotnews.presentation.adapters.BookmarksRecyclerViewAdapter
import com.kerimbr.kotnews.presentation.adapters.LastSearchRecyclerViewAdapter
import com.kerimbr.kotnews.presentation.adapters.NewsPagingDataAdapter
import com.kerimbr.kotnews.presentation.adapters.NewsRecyclerViewAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AdapterModule {

    @Singleton
    @Provides
    fun provideNewsRecyclerViewAdapter(): NewsRecyclerViewAdapter = NewsRecyclerViewAdapter()

    @Singleton
    @Provides
    fun provideNewsPagingDataAdapter(): NewsPagingDataAdapter = NewsPagingDataAdapter()

    @Singleton
    @Provides
    fun provideLastSearchRecyclerViewAdapter(): LastSearchRecyclerViewAdapter = LastSearchRecyclerViewAdapter()

    @Singleton
    @Provides
    fun provideBookmarksRecyclerViewAdapter(): BookmarksRecyclerViewAdapter = BookmarksRecyclerViewAdapter()

}
package com.kerimbr.kotnews.di

import android.app.Application
import androidx.room.Room
import com.kerimbr.kotnews.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "kotnews"
        ).build()
    }


    @Provides
    @Singleton
    fun provideArticleDAO(appDatabase: AppDatabase) = appDatabase.articleDAO()




}
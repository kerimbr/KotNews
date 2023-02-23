package com.kerimbr.kotnews.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kerimbr.kotnews.data.models.news.Article

@Database(
    entities = [Article::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun articleDAO(): ArticleDAO
}
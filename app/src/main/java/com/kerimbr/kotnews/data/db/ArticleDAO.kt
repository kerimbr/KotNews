package com.kerimbr.kotnews.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kerimbr.kotnews.data.models.news.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article)

    @Query("SELECT * FROM articles")
    fun getAllSavedNews(): Flow<List<Article>>

    @Query("DELETE FROM articles WHERE url = :articleUrl")
    fun deleteWithUrl(articleUrl: String): Int

    @Query("DELETE FROM articles WHERE id = :articleId")
    fun deleteWithId(articleId: Int): Int

    @Query("SELECT EXISTS(SELECT * FROM articles WHERE url = :articleUrl)")
    fun isBookmarked(articleUrl: String): Boolean

}
package com.kerimbr.kotnews.data.local.data_source

import com.kerimbr.kotnews.data.models.news.Article
import kotlinx.coroutines.flow.Flow

interface NewsLocalDataSource{

    suspend fun saveNewFromBookmarks(article: Article)

    fun getSavedNews(): Flow<List<Article>>

    suspend fun deleteNewFromBookmarks(article: Article) : Boolean

    fun isBookmarked(articleUrl: String): Boolean

}
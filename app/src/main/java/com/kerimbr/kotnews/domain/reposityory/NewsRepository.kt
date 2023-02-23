package com.kerimbr.kotnews.domain.reposityory


import com.kerimbr.kotnews.core.constants.DEFAULT_COUNTRY
import com.kerimbr.kotnews.core.constants.DEFAULT_PAGE_NUMBER
import com.kerimbr.kotnews.data.models.news.Article
import com.kerimbr.kotnews.data.models.news.NewsAPIResponse
import com.kerimbr.kotnews.data.remote.data_source.paging_source.NewsRemotePagingSource
import com.kerimbr.kotnews.data.utils.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getNewsHeadlines(
        country: String = DEFAULT_COUNTRY,
        page: Int = DEFAULT_PAGE_NUMBER
    ): Resource<NewsAPIResponse>

    fun getNewsPagingSource(): NewsRemotePagingSource

    suspend fun searchNews(searchQuery: String): Resource<NewsAPIResponse>

    suspend fun saveNewFromBookmarks(article: Article)

    suspend fun deleteNewFromBookmarks(article: Article) : Boolean

    fun getSavedNews(): Flow<List<Article>>

    fun getLastSearchQueries(): List<String>

    fun setLastSearchQuery(query: String) : Boolean

    fun deleteLastSearchQuery(query: String) : Boolean

    fun isBookmarked(articleUrl: String): Boolean

}
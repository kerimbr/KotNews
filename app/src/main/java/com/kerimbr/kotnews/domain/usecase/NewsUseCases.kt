package com.kerimbr.kotnews.domain.usecase

import com.kerimbr.kotnews.core.constants.DEFAULT_COUNTRY
import com.kerimbr.kotnews.core.constants.DEFAULT_PAGE_NUMBER
import com.kerimbr.kotnews.data.models.news.Article
import com.kerimbr.kotnews.data.models.news.NewsAPIResponse
import com.kerimbr.kotnews.data.remote.data_source.paging_source.NewsRemotePagingSource
import com.kerimbr.kotnews.data.utils.Resource
import com.kerimbr.kotnews.domain.reposityory.NewsRepository
import kotlinx.coroutines.flow.Flow

class NewsUseCases(
    private val newsRepository: NewsRepository
) {


    fun getNewsPagingSource(): NewsRemotePagingSource = newsRepository.getNewsPagingSource()

    suspend fun getNewsHeadlines(
        country: String = DEFAULT_COUNTRY,
        page: Int = DEFAULT_PAGE_NUMBER
    ): Resource<NewsAPIResponse> = newsRepository.getNewsHeadlines(
        country = country,
        page = page
    )

    suspend fun searchNews(searchQuery: String): Resource<NewsAPIResponse> =
        newsRepository.searchNews(searchQuery)

    suspend fun saveNewFromBookmarks(article: Article): Unit =
        newsRepository.saveNewFromBookmarks(article)

    suspend fun deleteNewsFromBookmarks(article: Article): Boolean =
        newsRepository.deleteNewFromBookmarks(article)

    fun getSavedNews(): Flow<List<Article>> = newsRepository.getSavedNews()

    fun isBookmarked(articleUrl: String): Boolean {
        return newsRepository.isBookmarked(articleUrl)
    }

}
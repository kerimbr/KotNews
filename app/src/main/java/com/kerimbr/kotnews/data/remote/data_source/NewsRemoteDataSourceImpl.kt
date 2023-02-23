package com.kerimbr.kotnews.data.remote.data_source

import com.kerimbr.kotnews.data.models.news.NewsAPIResponse
import com.kerimbr.kotnews.data.remote.api.NewsAPIService
import retrofit2.Response

class NewsRemoteDataSourceImpl(
    private val newsAPIService: NewsAPIService
) : NewsRemoteDataSource {

    override suspend fun getNewsHeadlines(
        country: String,
        pageNumber: Int
    ): Response<NewsAPIResponse> {
        return newsAPIService.getNewsHeadlines(country, pageNumber)
    }

    override suspend fun searchNews(searchQuery: String): Response<NewsAPIResponse> {
        return newsAPIService.getSearchNews(searchQuery)
    }


}
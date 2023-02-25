package com.kerimbr.kotnews.data.remote.api


import com.kerimbr.kotnews.core.constants.API_KEY
import com.kerimbr.kotnews.core.constants.DEFAULT_COUNTRY
import com.kerimbr.kotnews.core.constants.DEFAULT_PAGE_NUMBER
import com.kerimbr.kotnews.core.enums.SearchNewsQueries
import com.kerimbr.kotnews.data.models.news.NewsAPIResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsAPIService {

    @GET("v2/top-headlines")
    suspend fun getNewsHeadlines(
        @Query("country")
        country: String = DEFAULT_COUNTRY,

        @Query("page")
        pageNumber: Int = DEFAULT_PAGE_NUMBER,

        @Query("apiKey")
        apikey: String = API_KEY

    ): Response<NewsAPIResponse>

    @GET("v2/everything")
    suspend fun getSearchNews(

        @Query("q")
        searchQuery: String,

        @Query("language")
        language: String? = DEFAULT_COUNTRY,

        @Query("searchIn")
        searchIn: String = SearchNewsQueries.SearchIn.TITLE.value,

        @Query("sortBy")
        sortBy: String = SearchNewsQueries.SortBy.POPULARITY.value,

        @Query("apiKey")
        apikey: String = API_KEY

    ): Response<NewsAPIResponse>

}
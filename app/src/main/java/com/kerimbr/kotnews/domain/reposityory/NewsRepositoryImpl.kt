package com.kerimbr.kotnews.domain.reposityory



import com.kerimbr.kotnews.data.local.cache.CacheManager
import com.kerimbr.kotnews.data.local.data_source.NewsLocalDataSource
import com.kerimbr.kotnews.data.models.news.Article
import com.kerimbr.kotnews.data.models.news.NewsAPIResponse
import com.kerimbr.kotnews.data.remote.data_source.NewsRemoteDataSource
import com.kerimbr.kotnews.data.remote.data_source.paging_source.NewsRemotePagingSource
import com.kerimbr.kotnews.data.utils.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


class NewsRepositoryImpl(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val newsLocalDataSource: NewsLocalDataSource,
    private val cacheManager: CacheManager
): NewsRepository {

    private fun responseToResource(response: Response<NewsAPIResponse>) : Resource<NewsAPIResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (resultResponse.articles?.isNotEmpty() == true) {
                    return Resource.Success(resultResponse)
                }
            }
        }
        return Resource.Error(response.message())
    }

    override fun getNewsPagingSource() = NewsRemotePagingSource(
        newsRemoteDataSource = newsRemoteDataSource
    )

    override suspend fun getNewsHeadlines(
        country: String,
        page: Int
    ): Resource<NewsAPIResponse> {
        return responseToResource(newsRemoteDataSource.getNewsHeadlines(country, page))
    }

    override fun getLastSearchQueries(): List<String> {
        return cacheManager.getLastSearchQueries()
    }

    override fun setLastSearchQuery(query: String): Boolean {
        return cacheManager.setLastSearchQuery(query)
    }

    override fun deleteLastSearchQuery(query: String): Boolean {
        return cacheManager.deleteSearchQueryFromHistory(query)
    }

    override suspend fun searchNews(searchQuery: String): Resource<NewsAPIResponse> {
        return responseToResource(newsRemoteDataSource.searchNews(searchQuery))
    }

    override suspend fun saveNewFromBookmarks(article: Article) {
        newsLocalDataSource.saveNewFromBookmarks(article)
    }

    override suspend fun deleteNewFromBookmarks(article: Article) : Boolean{
        return newsLocalDataSource.deleteNewFromBookmarks(article)
    }

    override fun getSavedNews(): Flow<List<Article>> {
        return newsLocalDataSource.getSavedNews()
    }

    override fun isBookmarked(articleUrl: String): Boolean {
        return newsLocalDataSource.isBookmarked(articleUrl)
    }

}
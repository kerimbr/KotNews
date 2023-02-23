package com.kerimbr.kotnews.presentation.viewmodels

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kerimbr.kotnews.core.constants.GITHUB_REPO_URL
import com.kerimbr.kotnews.core.constants.PAGING_PAGE_SIZE
import com.kerimbr.kotnews.data.models.news.Article
import com.kerimbr.kotnews.domain.usecase.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(
    app: Application,
    private val newsUseCases: NewsUseCases,
) : AndroidViewModel(app) {



    private lateinit var _pagingNews: Flow<PagingData<Article>>
    val pagingNews: Flow<PagingData<Article>> get() = _pagingNews


    init {
        getPagingData()
    }

    private fun getPagingData() {
        _pagingNews = Pager(
            config = PagingConfig(pageSize = PAGING_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { newsUseCases.getNewsPagingSource() }
        ).flow.cachedIn(viewModelScope)
    }

    fun openGithubRepo(context: Context) {
        val url = GITHUB_REPO_URL
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context.startActivity(intent)
    }

    fun checkBookmark(article: Article): Boolean {
        if (article.url == null) return false
        var result = false
        // runBlocking is used to wait for the result of the coroutine
        runBlocking {
            // withContext is used to switch the context of the coroutine
            // [Main Thread --> IO Thread]
            withContext(Dispatchers.IO) {
                result = newsUseCases.isBookmarked(article.url)
            }
        }
        return result
    }

    fun addBookmark(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            newsUseCases.saveNewFromBookmarks(article)
        }
    }

    fun deleteBookmark(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            newsUseCases.deleteNewsFromBookmarks(article)
        }
    }

    // Migration from Flat RecyclerView to Paging 3
    /*fun getNewsHeadlines(country: String = DEFAULT_COUNTRY, page: Int = DEFAULT_PAGE_NUMBER) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (isInternetAvailable(app)) {
                    newsHeadlines.postValue(Resource.Loading())
                    val apiResult = getNewsHeadlinesUseCase
                        .execute(country, page)
                    newsHeadlines.postValue(apiResult)
                } else {
                    newsHeadlines.postValue(Resource.Error("No Internet Connection"))
                }
            } catch (e: Exception) {
                newsHeadlines.postValue(Resource.Error(e.message.toString()))
            }

        }
    }*/

}
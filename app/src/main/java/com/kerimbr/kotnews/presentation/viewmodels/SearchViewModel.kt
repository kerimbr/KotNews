package com.kerimbr.kotnews.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kerimbr.kotnews.core.utils.isInternetAvailable
import com.kerimbr.kotnews.data.models.news.Article
import com.kerimbr.kotnews.data.models.news.NewsAPIResponse
import com.kerimbr.kotnews.data.utils.Resource
import com.kerimbr.kotnews.domain.usecase.NewsUseCases
import com.kerimbr.kotnews.domain.usecase.SearchHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchHistoryUseCase: SearchHistoryUseCase,
    private val newsUseCases: NewsUseCases,
    private val app: Application
) : AndroidViewModel(app) {

    private val _searchHistory = MutableLiveData<List<String>>()
    val searchHistory: MutableLiveData<List<String>> get() = _searchHistory

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: MutableLiveData<String> get() = _searchQuery


    private val _news = MutableLiveData<Resource<NewsAPIResponse>>()
    val news: MutableLiveData<Resource<NewsAPIResponse>> get() = _news

    init {
        getSearchHistory()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getNewsByQuery(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (isInternetAvailable(app)) {
                    _news.postValue(Resource.Loading())
                    val apiResult = newsUseCases.searchNews(query)
                    _news.postValue(apiResult)
                } else {
                    _news.postValue(Resource.Error("No Internet Connection"))
                }
            } catch (e: Exception) {
                _news.postValue(Resource.Error(e.message.toString()))
            }

        }
    }


    fun getSearchHistory() {
        val list = searchHistoryUseCase.getSearchHistory()
        _searchHistory.value = list
    }


    fun addSearchQueryToHistory(query: String): Boolean {
        val result = searchHistoryUseCase.addSearchQuery(query)
        if (result) {
            _searchHistory.value?.let {
                val list = it.toMutableList()
                list.add(query)
                _searchHistory.value = list.distinct()

            }
        }
        return result
    }

    fun deleteSearchQueryFromHistory(query: String) {
        searchHistoryUseCase.clearSearchHistory(query)
        _searchHistory.value?.let {
            val list = it.toMutableList()
            list.remove(query)
            _searchHistory.value = list
        }
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

}
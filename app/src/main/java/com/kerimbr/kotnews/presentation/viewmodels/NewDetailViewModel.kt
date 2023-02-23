package com.kerimbr.kotnews.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerimbr.kotnews.data.models.news.Article
import com.kerimbr.kotnews.domain.usecase.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewDetailViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases
) : ViewModel() {

    private val _article = MutableLiveData<Article?>(null)
    val article: LiveData<Article?> get() = _article

    private val _isBookmarked = MutableLiveData<Boolean>(false)
    val isBookmarked: LiveData<Boolean> get() = _isBookmarked


    fun setArticle(article: Article) {
        _article.value = article
        checkBookmark(article)
    }

    fun addBookmark() {
        if (_article.value == null) return
        viewModelScope.launch(Dispatchers.IO) {
            newsUseCases.saveNewFromBookmarks(_article.value!!)
        }
    }

    fun removeBookmark() {
        if (_article.value == null) return
        viewModelScope.launch(Dispatchers.IO) {
            newsUseCases.deleteNewsFromBookmarks(_article.value!!)
        }
    }

    private fun checkBookmark(article: Article) {
        if (article.url == null) _isBookmarked.value = false
        viewModelScope.launch(Dispatchers.IO) {
            if (article.url == null) return@launch
            val deffered = async {
                newsUseCases.isBookmarked(article.url)
            }
            _isBookmarked.postValue(deffered.await())
        }
    }


}
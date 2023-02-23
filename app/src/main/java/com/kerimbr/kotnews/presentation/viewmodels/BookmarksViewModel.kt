package com.kerimbr.kotnews.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.kerimbr.kotnews.data.models.news.Article
import com.kerimbr.kotnews.domain.usecase.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases,
) : ViewModel() {

    fun getAllBookmarks() = liveData<List<Article>> {
        newsUseCases.getSavedNews().collect {
            emit(it)
        }
    }

    suspend fun deleteSavedNews(article: Article) : Boolean{
        val getResult = viewModelScope.async(Dispatchers.IO) {
            return@async newsUseCases.deleteNewsFromBookmarks(article)
        }
        return getResult.await()
    }


}
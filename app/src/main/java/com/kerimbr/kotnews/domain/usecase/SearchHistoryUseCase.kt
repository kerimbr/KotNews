package com.kerimbr.kotnews.domain.usecase

import com.kerimbr.kotnews.domain.reposityory.NewsRepository

class SearchHistoryUseCase(private val newsRepository: NewsRepository) {

    fun getSearchHistory(): List<String> = newsRepository.getLastSearchQueries()

    fun addSearchQuery(query: String): Boolean = newsRepository.setLastSearchQuery(query)

    fun clearSearchHistory(query: String): Boolean = newsRepository.deleteLastSearchQuery(query)

}
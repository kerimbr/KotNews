package com.kerimbr.kotnews.data.local.cache

interface CacheManager {

    fun setLastSearchQuery(query: String) : Boolean

    fun getLastSearchQueries(): List<String>

    fun deleteSearchQueryFromHistory(query: String) : Boolean

    fun clearSearchQueries(): Boolean

}
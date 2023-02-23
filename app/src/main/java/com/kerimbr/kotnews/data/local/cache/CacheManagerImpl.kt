package com.kerimbr.kotnews.data.local.cache

import android.content.Context
import com.kerimbr.kotnews.core.enums.CacheManagerKeys

class CacheManagerImpl(context: Context) : CacheManager {

    private val sharedPreferences = context.getSharedPreferences(
        "KotNews",
        Context.MODE_PRIVATE
    )
    private val editor = sharedPreferences.edit()

    override fun setLastSearchQuery(query: String): Boolean{
        return try {
            val oldQueries: List<String> = getLastSearchQueries()
            val queries: MutableList<String> = oldQueries.toMutableList()
            if (queries.size >= 10) {
                queries.removeFirst()
            }
            queries.add(query)
            editor.putStringSet(CacheManagerKeys.SEARCH_QUERIES.name, queries.toSet())
            editor.apply()
            true
        }catch (e: Exception){
            println("Error: ${e.message}")
            false
        }
    }

    override fun getLastSearchQueries(): List<String> {
        return sharedPreferences.getStringSet(CacheManagerKeys.SEARCH_QUERIES.name, setOf())
            ?.toList()?.reversed() ?: listOf()
    }

    override fun deleteSearchQueryFromHistory(query: String): Boolean {
        return try {
            val oldQueries: List<String> = getLastSearchQueries()
            val queries: MutableList<String> = oldQueries.toMutableList()
            queries.remove(query)
            editor.putStringSet(CacheManagerKeys.SEARCH_QUERIES.name, queries.toSet())
            editor.apply()
            true
        }catch (e: Exception){
            false
        }
    }

    override fun clearSearchQueries(): Boolean {
        return try {
            editor.remove(CacheManagerKeys.SEARCH_QUERIES.name)
            editor.apply()
            true
        }catch (e: Exception){
            false
        }
    }

}
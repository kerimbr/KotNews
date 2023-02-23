package com.kerimbr.kotnews.core.enums

enum class SearchNewsQueries{

    ;

    enum class SearchIn(val value: String) {
        TITLE("title"),
        DESCRIPTION("description"),
        CONTENT("content")
    }

    enum class SortBy(val value: String) {
        RELEVANCY("relevancy"),
        POPULARITY("popularity"),
        PUBLISHED_AT("publishedAt")
    }



}
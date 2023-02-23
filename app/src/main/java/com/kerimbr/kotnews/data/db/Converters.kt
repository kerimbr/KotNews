package com.kerimbr.kotnews.data.db

import androidx.room.TypeConverter
import com.kerimbr.kotnews.data.models.news.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String? {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(null, name)
    }

}
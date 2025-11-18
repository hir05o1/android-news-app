package dev.hir05o1.news_app.data.local

import androidx.room.TypeConverter
import dev.hir05o1.news_app.data.news_api.Source
import kotlinx.serialization.json.Json

class SourceConverters {
    // Convert a Source object to a JSON string
    @TypeConverter
    fun fromSource(source: Source): String {
        return Json.encodeToString(source)
    }

    // Convert a JSON string to a Source object
    @TypeConverter
    fun toSource(sourceString: String): Source {
        return Json.decodeFromString(sourceString)
    }
}

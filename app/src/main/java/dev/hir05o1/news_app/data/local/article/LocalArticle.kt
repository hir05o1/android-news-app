package dev.hir05o1.news_app.data.local.article

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "articles")
data class LocalArticle(
    @PrimaryKey val url: String,
    val sourceId: String?,
    val sourceName: String,
    val author: String?,
    val title: String,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)

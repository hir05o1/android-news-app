package dev.hir05o1.news_app.data.news_api

import dev.hir05o1.news_app.data.local.article.LocalArticle
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("status")
sealed interface NewsApiResult

@Serializable
@SerialName("ok")
data class NewsApiResponse(
    val totalResults: Int, val articles: List<Article>
) : NewsApiResult

@Serializable
@SerialName("error")
data class NewsApiErrorResponse(
    val code: String? = null,
    val message: String? = null,
) : NewsApiResult

@Serializable
data class Article(
    val source: Source,
    val author: String? = null,
    val title: String,
    val description: String? = null,
    val url: String,
    val urlToImage: String? = null,
    val publishedAt: String,
    val content: String? = null
)

@Serializable
data class Source(
    val id: String? = null, val name: String
)

data class EverythingParams(
    val q: String? = null,
    val sources: String? = null,
    val domains: String? = null,
    val excludeDomains: String? = null,
    val from: String? = null,
    val to: String? = null,
    val language: String? = null,
    val sortBy: String? = null,
    val pageSize: Int? = null,
    val page: Int? = null,
) {
    fun toQueryMap(): Map<String, String> = buildMap {
        q?.let { put("q", it) }
        sources?.let { put("sources", it) }
        domains?.let { put("domains", it) }
        excludeDomains?.let { put("excludeDomains", it) }
        from?.let { put("from", it) }
        to?.let { put("to", it) }
        language?.let { put("language", it) }
        sortBy?.let { put("sortBy", it) }
        pageSize?.let { put("pageSize", it.toString()) }
        page?.let { put("page", it.toString()) }
    }
}

data class TopHeadlinesParams(
    val q: String? = null,
    val sources: String? = null,
    val category: String? = null,
    val country: String? = null,
    val pageSize: Int? = null,
    val page: Int? = null,
) {
    fun toQueryMap(): Map<String, String> = buildMap {
        q?.let { put("q", it) }
        sources?.let { put("sources", it) }
        category?.let { put("category", it) }
        country?.let { put("country", it) }
        pageSize?.let { put("pageSize", it.toString()) }
        page?.let { put("page", it.toString()) }
    }
}

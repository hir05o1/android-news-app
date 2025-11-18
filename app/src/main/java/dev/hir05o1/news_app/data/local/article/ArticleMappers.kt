package dev.hir05o1.news_app.data.local.article

import dev.hir05o1.news_app.data.news_api.Article
import dev.hir05o1.news_app.data.news_api.Source

/**
 * Map API Model [Article] to Local DB Model [LocalArticle] .
 */
fun Article.toLocalArticle(): LocalArticle = LocalArticle(
    url = url,
    sourceId = source.id,
    sourceName = source.name,
    author = author,
    title = title,
    description = description,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = content
)

/**
 * Map Local DB Model [LocalArticle] to API Model [Article] .
 */
fun LocalArticle.toApiArticle(): Article = Article(
    url = url,
    source = Source(id = sourceId, name = sourceName),
    author = author,
    title = title,
    description = description,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = content
)

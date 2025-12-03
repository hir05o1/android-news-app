package dev.hir05o1.news_app.data.local.article

import dev.hir05o1.news_app.data.news_api.Article
import dev.hir05o1.news_app.data.news_api.Source
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ArticleMappersTest {

    @Test
    fun `toLocalArticle should map all fields from Article to LocalArticle`() {
        // Given
        val article = Article(
            source = Source(id = "source-123", name = "Test Source"),
            author = "John Doe",
            title = "Test Article Title",
            description = "Test article description",
            url = "https://example.com/article",
            urlToImage = "https://example.com/image.jpg",
            publishedAt = "2024-01-15T10:30:00Z",
            content = "Full article content here"
        )

        // When
        val localArticle = article.toLocalArticle()

        // Then
        assertEquals(article.url, localArticle.url)
        assertEquals(article.source.id, localArticle.sourceId)
        assertEquals(article.source.name, localArticle.sourceName)
        assertEquals(article.author, localArticle.author)
        assertEquals(article.title, localArticle.title)
        assertEquals(article.description, localArticle.description)
        assertEquals(article.urlToImage, localArticle.urlToImage)
        assertEquals(article.publishedAt, localArticle.publishedAt)
        assertEquals(article.content, localArticle.content)
    }

    @Test
    fun `toLocalArticle should handle null optional fields`() {
        // Given
        val article = Article(
            source = Source(id = null, name = "Test Source"),
            author = null,
            title = "Test Title",
            description = null,
            url = "https://example.com/article",
            urlToImage = null,
            publishedAt = "2024-01-15T10:30:00Z",
            content = null
        )

        // When
        val localArticle = article.toLocalArticle()

        // Then
        assertNull(localArticle.sourceId)
        assertNull(localArticle.author)
        assertNull(localArticle.description)
        assertNull(localArticle.urlToImage)
        assertNull(localArticle.content)
        // 必須フィールドは存在する
        assertEquals("Test Source", localArticle.sourceName)
        assertEquals("Test Title", localArticle.title)
        assertEquals("https://example.com/article", localArticle.url)
    }

    @Test
    fun `toApiArticle should map all fields from LocalArticle to Article`() {
        // Given
        val localArticle = LocalArticle(
            url = "https://example.com/article",
            sourceId = "source-456",
            sourceName = "Local Test Source",
            author = "Jane Smith",
            title = "Local Article Title",
            description = "Local article description",
            urlToImage = "https://example.com/local-image.jpg",
            publishedAt = "2024-02-20T14:45:00Z",
            content = "Local full content"
        )

        // When
        val article = localArticle.toApiArticle()

        // Then
        assertEquals(localArticle.url, article.url)
        assertEquals(localArticle.sourceId, article.source.id)
        assertEquals(localArticle.sourceName, article.source.name)
        assertEquals(localArticle.author, article.author)
        assertEquals(localArticle.title, article.title)
        assertEquals(localArticle.description, article.description)
        assertEquals(localArticle.urlToImage, article.urlToImage)
        assertEquals(localArticle.publishedAt, article.publishedAt)
        assertEquals(localArticle.content, article.content)

    }

    @Test
    fun `toApiArticle should handle null optional fields`() {
        // Given
        val localArticle = LocalArticle(
            url = "https://example.com/article",
            sourceId = null,
            sourceName = "Source Name",
            author = null,
            title = "Title",
            description = null,
            urlToImage = null,
            publishedAt = "2024-01-01T00:00:00Z",
            content = null
        )

        // When
        val article = localArticle.toApiArticle()

        // Then
        assertNull(article.source.id)
        assertNull(article.author)
        assertNull(article.description)
        assertNull(article.urlToImage)
        assertNull(article.content)
    }
}

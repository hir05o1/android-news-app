package dev.hir05o1.news_app.data.local.article

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArticleDatabaseTest {

    private lateinit var database: ArticleDatabase
    private lateinit var articleDao: ArticleDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // インメモリデータベースを作成(テスト終了時に破棄される)
        database = Room.inMemoryDatabaseBuilder(
            context, ArticleDatabase::class.java
        ).build()
        articleDao = database.articleDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `insertArticles should store articles in database`() = runTest {
        // Given
        val articles = listOf(
            LocalArticle(
                url = "https://example.com/1",
                sourceId = "source1",
                sourceName = "Test Source",
                author = "Author 1",
                title = "Test Article 1",
                description = "Description 1",
                urlToImage = "https://example.com/image1.jpg",
                publishedAt = "2024-01-01T00:00:00Z",
                content = "Content 1"
            ), LocalArticle(
                url = "https://example.com/2",
                sourceId = "source2",
                sourceName = "Test Source 2",
                author = "Author 2",
                title = "Test Article 2",
                description = "Description 2",
                urlToImage = "https://example.com/image2.jpg",
                publishedAt = "2024-01-02T00:00:00Z",
                content = "Content 2"
            )
        )

        // When
        articleDao.insertArticles(articles)

        // Then
        val retrievedArticles = articleDao.getArticles().first()
        assertEquals(2, retrievedArticles.size)
    }

    @Test
    fun `getArticles should return articles ordered by publishedAt descending`() = runTest {
        // Given
        val articles = listOf(
            LocalArticle(
                url = "https://example.com/1",
                sourceId = null,
                sourceName = "Source",
                author = null,
                title = "Old Article",
                description = null,
                urlToImage = null,
                publishedAt = "2024-01-01T00:00:00Z",
                content = null
            ), LocalArticle(
                url = "https://example.com/2",
                sourceId = null,
                sourceName = "Source",
                author = null,
                title = "New Article",
                description = null,
                urlToImage = null,
                publishedAt = "2024-01-03T00:00:00Z",
                content = null
            ), LocalArticle(
                url = "https://example.com/3",
                sourceId = null,
                sourceName = "Source",
                author = null,
                title = "Middle Article",
                description = null,
                urlToImage = null,
                publishedAt = "2024-01-02T00:00:00Z",
                content = null
            )
        )

        // When
        articleDao.insertArticles(articles)

        // Then
        val retrievedArticles = articleDao.getArticles().first()
        assertEquals(3, retrievedArticles.size)

        // 最初の記事は "New Article" (最新の日付)
        assertEquals(articles[1], retrievedArticles[0])
        // 2番目の記事は "Middle Article"
        assertEquals(articles[2], retrievedArticles[1])
        // 3番目の記事は "Old Article" (最も古い日付)
        assertEquals(articles[0], retrievedArticles[2])
    }

    @Test
    fun `observeArticleByUrl should return article with matching URL`() = runTest {
        // Given
        val targetUrl = "https://example.com/target"
        val articles = listOf(
            LocalArticle(
                url = targetUrl,
                sourceId = null,
                sourceName = "Source",
                author = null,
                title = "Target Article",
                description = null,
                urlToImage = null,
                publishedAt = "2024-01-01T00:00:00Z",
                content = null
            ), LocalArticle(
                url = "https://example.com/other",
                sourceId = null,
                sourceName = "Source",
                author = null,
                title = "Other Article",
                description = null,
                urlToImage = null,
                publishedAt = "2024-01-02T00:00:00Z",
                content = null
            )
        )
        articleDao.insertArticles(articles)

        // When
        val result = articleDao.observeArticleByUrl(targetUrl).first()

        // Then
        // foundArticleはnullではない
        assertNotNull(result)

        // foundArticleのtitleは "Target Article"
        assertEquals(articles[0], result)
    }

    @Test
    fun `observeArticleByUrl should return null when URL does not exist`() = runTest {
        // Given
        val articles = listOf(
            LocalArticle(
                url = "https://example.com/1",
                sourceId = null,
                sourceName = "Source",
                author = null,
                title = "Article 1",
                description = null,
                urlToImage = null,
                publishedAt = "2024-01-01T00:00:00Z",
                content = null
            )
        )
        articleDao.insertArticles(articles)

        // When
        val foundArticle = articleDao.observeArticleByUrl("https://example.com/nonexistent").first()

        // Then
        assertNull(foundArticle)
    }

    @Test
    fun `deleteAllArticles should remove all articles from database`() = runTest {
        // Given
        val articles = listOf(
            LocalArticle(
                url = "https://example.com/1",
                sourceId = null,
                sourceName = "Source",
                author = null,
                title = "Article 1",
                description = null,
                urlToImage = null,
                publishedAt = "2024-01-01T00:00:00Z",
                content = null
            ), LocalArticle(
                url = "https://example.com/2",
                sourceId = null,
                sourceName = "Source",
                author = null,
                title = "Article 2",
                description = null,
                urlToImage = null,
                publishedAt = "2024-01-02T00:00:00Z",
                content = null
            )
        )
        articleDao.insertArticles(articles)

        // When
        articleDao.deleteAllArticles()

        // Then
        val remainingArticles = articleDao.getArticles().first()
        assertEquals(0, remainingArticles.size)
    }

    @Test
    fun `insertArticles with duplicate URL should replace existing article`() = runTest {
        // Given
        val originalArticle = LocalArticle(
            url = "https://example.com/article",
            sourceId = null,
            sourceName = "Original Source",
            author = "Original Author",
            title = "Original Title",
            description = null,
            urlToImage = null,
            publishedAt = "2024-01-01T00:00:00Z",
            content = null
        )
        articleDao.insertArticles(listOf(originalArticle))

        // When
        val updatedArticle = LocalArticle(
            url = "https://example.com/article", // 同じURL
            sourceId = null,
            sourceName = "Updated Source",
            author = "Updated Author",
            title = "Updated Title",
            description = null,
            urlToImage = null,
            publishedAt = "2024-01-02T00:00:00Z",
            content = null
        )
        articleDao.insertArticles(listOf(updatedArticle))

        // Then
        val retrievedArticles = articleDao.getArticles().first()
        // ヒント: getArticles()で取得した記事リストのサイズは1
        assertEquals(1, retrievedArticles.size)

        // ヒント: 取得した記事のtitleは "Updated Title"
        // ヒント: 取得した記事のauthorは "Updated Author"
        assertEquals(updatedArticle, retrievedArticles[0])
    }
}

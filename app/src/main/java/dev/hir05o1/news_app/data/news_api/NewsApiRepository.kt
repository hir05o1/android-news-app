package dev.hir05o1.news_app.data.news_api

import android.util.Log
import dev.hir05o1.news_app.data.local.article.ArticleDao
import dev.hir05o1.news_app.data.local.article.toApiArticle
import dev.hir05o1.news_app.data.local.article.toLocalArticle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NewsApiRepository(
    private val newsApiClient: NewsApiClient, private val articleDao: ArticleDao
) {
    private val tag = "NewsApiRepository"

    /**
     * Returns a Flow that monitors articles in the local DB and emits an Article list
     * for the UI whenever the data changes.
     */
    fun getArticlesStream(): Flow<List<Article>> {
        return articleDao.getArticles().map { localArticles ->
            localArticles.map { it.toApiArticle() }
        }
    }

    fun getArticleStream(url: String): Flow<Article?> {
        return articleDao.observeArticleByUrl(url).map { localArticle ->
            localArticle?.toApiArticle()
        }
    }

    /**
     * Refresh articles from the network and save them to the local DB.
     */
    suspend fun refreshArticles(
        // TODO: QueryをUIから受け取る
        everythingParams: EverythingParams = EverythingParams(
            domains = "asahi.com", pageSize = 3
        )
    ) {
        try {
            val networkResult = newsApiClient.getEverything(
                everythingParams.toQueryMap()
            )

            if (networkResult is NewsApiResponse) {
                // ローカルDBに保存
                val localArticles = networkResult.articles.map { it.toLocalArticle() }
                articleDao.insertArticles(localArticles)
                Log.d(tag, "Successfully fetched and saved ${localArticles.size} articles to DB.")

            } else if (networkResult is NewsApiErrorResponse) {
                Log.e(tag, "API Error: ${networkResult.code} - ${networkResult.message}")
                throw ApiException(networkResult.message ?: "Unknown API error")
            }
        } catch (e: Exception) {
            Log.e(tag, "Failed to refresh articles: ${e.message}")
            // TODO: UIにエラーを通知する
            throw e
        }
    }
}

/**
 * APIエラーをラップするためのカスタム例外クラス
 */
class ApiException(message: String) : Exception(message)

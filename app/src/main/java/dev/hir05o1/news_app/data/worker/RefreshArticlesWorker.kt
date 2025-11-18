package dev.hir05o1.news_app.data.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.hir05o1.news_app.data.news_api.EverythingParams
import dev.hir05o1.news_app.data.news_api.NewsApiRepository

class RefreshArticlesWorker(
    appContext: Context, params: WorkerParameters, private val newsApiRepository: NewsApiRepository
) : CoroutineWorker(appContext, params) {
    private val tag = "RefreshArticlesWorker"

    override suspend fun doWork(): Result {
        return try {
            Log.d(tag, "doWork: Refreshing articles...")
            newsApiRepository.refreshArticles(everythingParams = EverythingParams(q = "Android"))
            Log.d(tag, "doWork: Successfully refreshed articles.")
            Result.success()
        } catch (e: Exception) {
            Log.e(tag, "doWork: Failed to refresh articles.", e)
            Result.failure()
        }
    }
}

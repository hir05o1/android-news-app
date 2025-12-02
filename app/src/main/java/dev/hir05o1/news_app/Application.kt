package dev.hir05o1.news_app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import dev.hir05o1.news_app.data.worker.RefreshArticlesWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class Application : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()

    private fun setupRecurringWork() {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshArticlesWorker>(
            15, TimeUnit.MINUTES
        ).setConstraints(constraints).build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "refresh-articles-work", ExistingPeriodicWorkPolicy.KEEP, repeatingRequest
        )
    }
    override fun onCreate() {
        super.onCreate()
        setupRecurringWork()
    }
}



package dev.hir05o1.news_app

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dev.hir05o1.news_app.data.worker.RefreshArticlesWorker
import dev.hir05o1.news_app.di.databaseModule
import dev.hir05o1.news_app.di.networkModule
import dev.hir05o1.news_app.di.repositoryModule
import dev.hir05o1.news_app.di.viewModelModule
import dev.hir05o1.news_app.di.workerModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Application)
            workManagerFactory()
            modules(networkModule, repositoryModule, viewModelModule, databaseModule, workerModule)
        }
        setupRecurringWork()
    }

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
}



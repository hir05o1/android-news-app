package dev.hir05o1.news_app.di

import dev.hir05o1.news_app.data.worker.RefreshArticlesWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

val workerModule = module {
    workerOf(::RefreshArticlesWorker)
}

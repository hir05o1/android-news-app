package dev.hir05o1.news_app.di

import dev.hir05o1.news_app.data.news_api.NewsApiRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { NewsApiRepository(get(), get()) }
}

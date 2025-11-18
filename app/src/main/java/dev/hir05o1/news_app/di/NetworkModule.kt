package dev.hir05o1.news_app.di

import dev.hir05o1.news_app.data.news_api.NewsApiClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val networkModule = module {
    singleOf(::NewsApiClient)
}

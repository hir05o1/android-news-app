package dev.hir05o1.news_app.di

import dev.hir05o1.news_app.ui.news_list.NewsListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::NewsListViewModel)
}

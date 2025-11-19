package dev.hir05o1.news_app.di

import dev.hir05o1.news_app.ui.article.ArticleViewModel
import dev.hir05o1.news_app.ui.article_list.ArticleListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::ArticleListViewModel)
    viewModelOf(::ArticleViewModel)
}

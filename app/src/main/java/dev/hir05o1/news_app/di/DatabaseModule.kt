package dev.hir05o1.news_app.di

import androidx.room.Room
import dev.hir05o1.news_app.data.local.article.ArticleDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(), ArticleDatabase::class.java, "article.db"
        ).build()
    }
    single { get<ArticleDatabase>().articleDao() }
}

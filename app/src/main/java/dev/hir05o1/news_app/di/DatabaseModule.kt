package dev.hir05o1.news_app.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.hir05o1.news_app.data.local.article.ArticleDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideArticleDatabase(
        @ApplicationContext context: Context
    ): ArticleDatabase {
        return Room.databaseBuilder(
            context, ArticleDatabase::class.java, "article.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideArticleDao(db: ArticleDatabase) = db.articleDao()
}

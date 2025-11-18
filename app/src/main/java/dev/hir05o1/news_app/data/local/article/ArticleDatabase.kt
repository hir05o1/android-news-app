package dev.hir05o1.news_app.data.local.article

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.hir05o1.news_app.data.local.SourceConverters

@Database(entities = [LocalArticle::class], version = 1)
@TypeConverters(SourceConverters::class)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}

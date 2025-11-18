package dev.hir05o1.news_app.data.local.article

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<LocalArticle>)

    @Query("SELECT * FROM articles ORDER BY publishedAt DESC")
    fun getArticles(): Flow<List<LocalArticle>>

    /**
     * URLをキーに単一の記事を取得し、Flowとして監視します。
     * @param url 取得したい記事のURL
     * @return 見つかった記事のFlow。見つからない場合はnullを放出します。
     */
    @Query("SELECT * FROM articles WHERE url = :url")
    fun observeArticleByUrl(url: String): Flow<LocalArticle?>

    @Query("DELETE FROM articles")
    suspend fun deleteAllArticles()
}

package dev.hir05o1.news_app.ui.article

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.hir05o1.news_app.data.news_api.Article


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleView(
    onNavigateBack: () -> Unit, viewModel: ArticleViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                // 記事のタイトルをトップバーに表示（長い場合は省略）
                val title =
                    uiState.article?.title ?: if (uiState.isLoading) "Loading..." else "Article"
                Text(text = title, maxLines = 1)
            }, navigationIcon = {
                // 戻るボタン
                IconButton(onClick = onNavigateBack) {
                    Text(text = "back")
                }
            })
        }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> {
                    // ローディング表示
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.error != null -> {
                    // エラー表示
                    Text(
                        text = "Error: ${uiState.error}",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                uiState.article != null -> {
                    // 記事詳細表示
                    ArticleDetail(article = uiState.article!!)
                }

                else -> {
                    // 記事が見つからなかった場合（nullの場合）
                    Text(
                        text = "Article not found.", modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun ArticleDetail(article: Article) {
    // 縦スクロール可能にする
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 記事の画像
        AsyncImage(
            model = article.urlToImage,
            contentDescription = article.title,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 記事のタイトル
        Text(
            text = article.title, style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))

        // 著者と提供元
        Text(
            text = "by ${article.author ?: article.source.name}",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 記事の本文
        Text(
            text = article.content ?: article.description ?: "No content available.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


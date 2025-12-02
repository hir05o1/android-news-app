package dev.hir05o1.news_app.ui.article

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import dev.hir05o1.news_app.R
import dev.hir05o1.news_app.data.news_api.Article
import dev.hir05o1.news_app.data.news_api.Source
import dev.hir05o1.news_app.ui.theme.News_appTheme
import dev.hir05o1.news_app.utils.formatPublishedAt
import dev.hir05o1.news_app.utils.shareUrl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleView(
    onNavigateBack: () -> Unit, viewModel: ArticleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    ArticleViewContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        modifier = Modifier.fillMaxSize(),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArticleViewContent(
    uiState: ArticleUiState,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
) {
    val context = LocalContext.current
    Scaffold(
        modifier = modifier, topBar = {
            TopAppBar(title = {
                val title = uiState.article?.title ?: if (uiState.isLoading) "" else "Article"
                Text(text = title, maxLines = 1, overflow = Ellipsis)
            }, navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Back",
                    )
                }
            }, actions = {
                if (uiState.article != null) {
                    IconButton(onClick = {
                        shareUrl(context, uiState.article.url)
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_share),
                            contentDescription = "Share"
                        )
                    }
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
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.error != null -> {
                    Text(
                        text = "Error: ${uiState.error}",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                uiState.article != null -> {
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        AsyncImage(
            model = article.urlToImage,
            contentDescription = article.title,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = article.title, style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "by ${article.author ?: article.source.name}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = formatPublishedAt(article.publishedAt),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = article.content ?: article.description ?: "No content available.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
private fun ArticleViewPreview() {
    News_appTheme {
        ArticleViewContent(
            uiState = ArticleUiState(
                article = Article(
                    source = Source(id = "the-verge", name = "The Verge"),
                    author = "Sarah Johnson",
                    title = "New AI breakthrough enables real-time language translation",
                    description = "Researchers announce major advancement in neural machine translation technology.",
                    url = "https://example.com/ai-translation-breakthrough",
                    urlToImage = "https://placehold.jp/150x150.png",
                    publishedAt = "2024-11-18T10:30:00Z",
                    content = "A team of researchers has developed a new AI model that can translate spoken language in real-time with unprecedented accuracy..."
                )
            ), onNavigateBack = {})
    }
}

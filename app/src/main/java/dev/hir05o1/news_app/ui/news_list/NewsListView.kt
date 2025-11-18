package dev.hir05o1.news_app.ui.news_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.hir05o1.news_app.data.news_api.Article
import dev.hir05o1.news_app.data.news_api.Source
import dev.hir05o1.news_app.ui.theme.News_appTheme
import dev.hir05o1.news_app.utils.formatPublishedAt
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListView(
    modifier: Modifier = Modifier, viewModel: NewsListViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    NewsListContent(
        uiState = uiState, onRefresh = viewModel::refresh, modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewsListContent(
    uiState: NewsListUiState, onRefresh: () -> Unit, modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("News") })
        },
        contentWindowInsets = WindowInsets().exclude(WindowInsets.systemBars),
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = onRefresh,
            modifier = modifier.padding(innerPadding)
        ) {
            // エラーのとき（記事が空のとき）
            if (uiState.error != null && uiState.articles.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${uiState.error}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            } else {
                // 記事リストの表示
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(items = uiState.articles, key = { it.url }) { article ->
                        ArticleItem(article = article)
                    }
                }
            }
        }
    }
}

@Composable
private fun ArticleItem(article: Article) {
    ElevatedCard(
        modifier = Modifier
            .clip(shape = CardDefaults.elevatedShape)
            .clickable(
                onClick = {})
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = article.source.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = formatPublishedAt(article.publishedAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (!article.urlToImage.isNullOrEmpty()) {
                AsyncImage(
                    model = article.urlToImage,
                    contentDescription = article.title,
                    modifier = Modifier
                        .size(88.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NewsListContentPreview() {
    val sampleUiState = NewsListUiState(
        articles = listOf(
            Article(
                source = Source(id = "bbc-news", name = "BBC News"),
                author = "John Doe",
                title = "This is a sample title for a news article in a preview",
                description = "This is a longer description to see how it wraps and displays in the UI.",
                url = "https://example.com/1",
                urlToImage = "",
                publishedAt = "2023-10-27T10:00:00Z",
                content = ""
            ), Article(
                source = Source(id = "techcrunch", name = "TechCrunch"),
                author = "Jane Smith",
                title = "Another exciting news article about technology and gadgets",
                description = "More details about the exciting technology.",
                url = "https://example.com/2",
                urlToImage = "",
                publishedAt = "2023-10-27T09:00:00Z",
                content = ""
            )
        ), isLoading = false, error = null
    )
    News_appTheme {
        NewsListContent(uiState = sampleUiState, onRefresh = {})
    }
}

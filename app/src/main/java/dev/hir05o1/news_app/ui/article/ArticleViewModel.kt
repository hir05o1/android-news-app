package dev.hir05o1.news_app.ui.article


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.hir05o1.news_app.data.news_api.Article
import dev.hir05o1.news_app.data.news_api.NewsApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

data class ArticleUiState(
    val article: Article? = null, val isLoading: Boolean = false, val error: String? = null
)

class ArticleViewModel(
    newsApiRepository: NewsApiRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArticleUiState())
    val uiState: StateFlow<ArticleUiState> = _uiState.asStateFlow()

    // Navigationから渡される記事のURL
    private val articleUrl: String = checkNotNull(savedStateHandle["articleUrl"])

    init {
        _uiState.update { it.copy(isLoading = true) }

        newsApiRepository.getArticleStream(articleUrl).onEach { article ->
            _uiState.update {
                it.copy(
                    article = article, isLoading = false
                )
            }
        }.catch { e ->
            _uiState.update {
                it.copy(
                    error = e.message, isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }
}

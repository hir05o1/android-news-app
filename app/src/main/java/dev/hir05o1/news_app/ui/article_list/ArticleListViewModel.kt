package dev.hir05o1.news_app.ui.article_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.hir05o1.news_app.data.news_api.Article
import dev.hir05o1.news_app.data.news_api.NewsApiRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ArticleListUiState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val repository: NewsApiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArticleListUiState())
    val uiState: StateFlow<ArticleListUiState> = _uiState.asStateFlow()

    init {
        repository.getArticlesStream().onEach { articles ->
            _uiState.update { it.copy(articles = articles) }
        }.catch { e ->
            _uiState.update { it.copy(error = e.message) }
        }.launchIn(viewModelScope) // viewModelScopeCollectを開始
    }

    fun refresh() {
        // 多重実行を防ぐ
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.refreshArticles()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}

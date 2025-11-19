package dev.hir05o1.news_app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.hir05o1.news_app.ui.article.ArticleView
import dev.hir05o1.news_app.ui.news_list.NewsListView

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    navActions: NavActions = remember(navController) {
        NavActions(navController)
    }
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.ARTICLE_LIST_ROUTE,
        modifier = modifier,
    ) {
        composable(
            Destinations.ARTICLE_LIST_ROUTE
        ) {
            NewsListView(
                onArticleClick = navActions::navigateToArticle
            )
        }

        composable(
            Destinations.ARTICLE_ROUTE,
        ) {
            ArticleView(
                onNavigateBack = navActions::navigateToBack,
            )
        }
    }
}

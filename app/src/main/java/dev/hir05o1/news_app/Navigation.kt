package dev.hir05o1.news_app

import androidx.navigation.NavController
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

private object Views {
    const val ARTICLE_LIST_VIEW = "article_list_view"
    const val ARTICLE_VIEW = "article_view"
}

object DestinationArgs {
    const val ARTICLE_URL_ARG = "article_url"
}

object Destinations {
    const val ARTICLE_LIST_ROUTE = Views.ARTICLE_LIST_VIEW
    const val ARTICLE_ROUTE = "${Views.ARTICLE_VIEW}/{${DestinationArgs.ARTICLE_URL_ARG}}"
}

class NavActions(private val navController: NavController) {
    fun navigateToArticleList() {
        navController.navigate(Destinations.ARTICLE_LIST_ROUTE)
    }

    fun navigateToArticle(articleUrl: String) {
        val encodedUrl = URLEncoder.encode(articleUrl, StandardCharsets.UTF_8.toString())
        navController.navigate("${Views.ARTICLE_VIEW}/$encodedUrl")
    }

    fun navigateToBack() {
        navController.popBackStack()
    }
}

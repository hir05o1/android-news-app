package dev.hir05o1.news_app.data.news_api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.hir05o1.news_app.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class NewsApiClient {
    private val NEWS_API_API_KEY = BuildConfig.NEWS_API_API_KEY
    private val baseUrl = "https://newsapi.org/"

    private val json = Json {
        ignoreUnknownKeys = false
    }

    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(
            json.asConverterFactory(
                contentType = "application/json".toMediaType()
            )
        ).build()
    }

    val newsApiService: NewsApiService by lazy { retrofit.create(NewsApiService::class.java) }

    suspend fun getTopHeadlines(params: Map<String, String>): NewsApiResult {
        val response = newsApiService.getTopHeadlines(params, NEWS_API_API_KEY)
        return response
    }

    suspend fun getEverything(params: Map<String, String>): NewsApiResult {
        return newsApiService.getEverything(params, NEWS_API_API_KEY)
    }
}

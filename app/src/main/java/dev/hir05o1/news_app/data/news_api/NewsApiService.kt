package dev.hir05o1.news_app.data.news_api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.QueryMap

interface NewsApiService {
    @GET("/v2/top-headlines")
    suspend fun getTopHeadlines(
        @QueryMap params: Map<String, String>,
        @Header("X-Api-Key") apiKey: String,
    ): NewsApiResult

    @GET("/v2/everything")
    suspend fun getEverything(
        @QueryMap params: Map<String, String>,
        @Header("X-Api-Key") apiKey: String,
    ): NewsApiResult
}

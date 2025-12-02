package dev.hir05o1.news_app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.hir05o1.news_app.data.news_api.NewsApiClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideNewsApiClient(): NewsApiClient = NewsApiClient()
}

package dev.hir05o1.news_app.utils

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun formatPublishedAt(publishedAt: String): String {
    return try {
        val odt = OffsetDateTime.parse(publishedAt)
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
        odt.format(formatter)
    } catch (e: Exception) {
        publishedAt
    }
}

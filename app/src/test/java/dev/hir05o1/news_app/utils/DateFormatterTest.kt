package dev.hir05o1.news_app.utils

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * DateFormatter のユニットテスト
 *
 * formatPublishedAt 関数が正しく日付をフォーマットできることを確認します。
 */
class DateFormatterTest {

    @Test
    fun `formatPublishedAt should format valid ISO 8601 date correctly`() {
        // Given: 有効なISO 8601形式の日付文字列
        val input = "2024-12-02T10:30:45Z"

        // When: フォーマット関数を実行
        val result = formatPublishedAt(input)

        // Then: 期待される形式 "yyyy/MM/dd HH:mm" にフォーマットされている
        assertEquals("2024/12/02 10:30", result)
    }

    @Test
    fun `formatPublishedAt should format date with timezone offset correctly`() {
        // Given: タイムゾーンオフセット付きの日付文字列
        val input = "2024-12-02T15:45:30+09:00"

        // When: フォーマット関数を実行
        val result = formatPublishedAt(input)

        // Then: 正しくフォーマットされている（タイムゾーンを考慮）
        assertEquals("2024/12/02 15:45", result)
    }

    // TODO(human): ここに無効な日付形式のテストケースを追加してください
    // ヒント: 無効な文字列が渡された時、元の文字列がそのまま返されるべきです

    @Test
    fun `formatPublishedAt should handle date with milliseconds`() {
        // Given: ミリ秒を含む日付文字列
        val input = "2024-12-02T12:34:56.789Z"

        // When: フォーマット関数を実行
        val result = formatPublishedAt(input)

        // Then: ミリ秒は無視され、分まで正しくフォーマットされている
        assertEquals("2024/12/02 12:34", result)
    }

    @Test
    fun `formatPublishedAt should handle midnight time`() {
        // Given: 深夜0時の日付文字列
        val input = "2024-12-02T00:00:00Z"

        // When: フォーマット関数を実行
        val result = formatPublishedAt(input)

        // Then: 00:00として正しくフォーマットされている
        assertEquals("2024/12/02 00:00", result)
    }

    @Test
    fun `formatPublishedAt should handle end of day time`() {
        // Given: 1日の終わり（23:59）の日付文字列
        val input = "2024-12-31T23:59:59Z"

        // When: フォーマット関数を実行
        val result = formatPublishedAt(input)

        // Then: 23:59として正しくフォーマットされている
        assertEquals("2024/12/31 23:59", result)
    }
}
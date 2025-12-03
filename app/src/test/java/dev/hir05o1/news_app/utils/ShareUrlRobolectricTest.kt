package dev.hir05o1.news_app.utils

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class) // Androidシミュレーション環境を提供
@Config(sdk = [33])
class ShareUrlRobolectricTest {

    @Test
    fun `shareUrl should create and start chooser intent with correct URL`() {
        // Given
        // テスト用のContextとURLを準備
        val context = ApplicationProvider.getApplicationContext<Context>()
        val testUrl = "https://example.com/article"

        // When
        // ShareUrl関数を実行
        shareUrl(context, testUrl)

        // Then
        // startActivityが呼ばれたことを確認
        val shadowActivity = shadowOf(context as android.app.Application)
        val startedIntent = shadowActivity.nextStartedActivity

        // Intentが開始されたこと
        assertNotNull("Intent should be started", startedIntent)

        // Intent.ACTION_CHOOSERアクションが使われていること
        assertEquals(
            "Intent.ACTION_CHOOSER should be used", Intent.ACTION_CHOOSER, startedIntent.action
        )

        // 元のIntentが存在すること

        // 元のIntentを取得
        val originalIntent =
            startedIntent.getParcelableExtra(Intent.EXTRA_INTENT, Intent::class.java)
        assertNotNull("Original intent should exist", originalIntent)

        // 元のIntentのアクションは Intent.ACTION_SEND であるべき
        assertEquals(
            "Original intent action should be ACTION_SEND",
            Intent.ACTION_SEND,
            originalIntent?.action
        )

        // 元のIntentの EXTRA_TEXT には testUrl が含まれているべき
        assertEquals(
            "Original intent should contain the URL",
            testUrl,
            originalIntent?.getStringExtra(Intent.EXTRA_TEXT)
        )

        // 元のIntentの type は "text/plain" であるべき
        assertEquals(
            "Original intent type should be text/plain", "text/plain", originalIntent?.type
        )
    }

    @Test
    fun `shareUrl should handle empty URL string`() {
        // Given: 空のURL
        val context = ApplicationProvider.getApplicationContext<Context>()
        val emptyUrl = ""

        // When: shareUrl関数を実行
        shareUrl(context, emptyUrl)

        // Then: Intentは作成されるが、URLは空
        val shadowActivity = shadowOf(context as android.app.Application)
        val startedIntent = shadowActivity.nextStartedActivity

        assertNotNull("Intent should be started even with empty URL", startedIntent)

        // 元のIntentを取得
        @Suppress("DEPRECATION") val originalIntent =
            startedIntent.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)
        assertNotNull("Original intent should exist", originalIntent)

        // 空のURLが設定されていることを確認
        val sharedText = originalIntent?.getStringExtra(Intent.EXTRA_TEXT)
        assertEquals("Empty URL should be passed", "", sharedText)
    }

    @Test
    fun `shareUrl should handle URL with special characters`() {
        // Given: 特殊文字を含むURL
        val context = ApplicationProvider.getApplicationContext<Context>()
        val urlWithSpecialChars = "https://example.com/article?id=123&lang=ja#section1"

        // When: shareUrl関数を実行
        shareUrl(context, urlWithSpecialChars)

        // Then: URLがそのまま渡される
        val shadowActivity = shadowOf(context as android.app.Application)
        val startedIntent = shadowActivity.nextStartedActivity

        @Suppress("DEPRECATION") val originalIntent =
            startedIntent.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)
        val sharedText = originalIntent?.getStringExtra(Intent.EXTRA_TEXT)

        assertEquals(
            "URL with special characters should be preserved", urlWithSpecialChars, sharedText
        )
    }
}

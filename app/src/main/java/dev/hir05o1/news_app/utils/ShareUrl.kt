package dev.hir05o1.news_app.utils

import android.content.Context
import android.content.Intent

fun shareUrl(context: Context, url: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, url)
        type = "text/plain"
    }

    // 常にアプリ選択ダイアログを表示させる
    val chooser = Intent.createChooser(sendIntent, null)
    context.startActivity(chooser)
}

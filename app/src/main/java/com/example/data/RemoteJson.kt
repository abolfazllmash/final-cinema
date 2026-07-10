package com.example.data

import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

/**
 * خواندن فایل‌های داده ساده (films.json / collections.json / top_lists.json) از سرور.
 * روی هر خطا null برمی‌گرداند تا فراخواننده از assets (نسخه آفلاین) استفاده کند.
 * توجه: blocking است؛ حتما روی Dispatchers.IO صدایش بزن.
 */
object RemoteJson {
    // پایه داده روی سرور. باید با / تمام شود. خالی بگذاری = فقط assets.
    const val DATA_BASE = ""

    private val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(6, TimeUnit.SECONDS)
        .build()

    fun fetch(fileName: String): String? {
        if (DATA_BASE.isEmpty()) return null
        return try {
            val req = Request.Builder().url(DATA_BASE + fileName).build()
            client.newCall(req).execute().use { resp ->
                if (resp.isSuccessful) resp.body?.string() else null
            }
        } catch (e: Exception) {
            null
        }
    }
}

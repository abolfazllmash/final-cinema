package com.example.ui

internal val LANG_FA = mapOf(
    "fa" to "فارسی",
    "en" to "انگلیسی",
    "fr" to "فرانسوی",
    "it" to "ایتالیایی",
    "ja" to "ژاپنی",
    "ko" to "کره‌ای",
    "es" to "اسپانیایی",
    "de" to "آلمانی"
)

internal val COUNTRY_FA = mapOf(
    "IR" to "ایران",
    "US" to "ایالات متحده آمریکا",
    "GB" to "انگلستان",
    "FR" to "فرانسه",
    "IT" to "ایتالیا",
    "JP" to "ژاپن",
    "KR" to "کره جنوبی",
    "DE" to "آلمان",
    "ES" to "اسپانیا"
)

fun getLanguageFa(code: String): String {
    return LANG_FA[code.lowercase()] ?: code
}

fun getCountryFa(code: String): String {
    return COUNTRY_FA[code.uppercase()] ?: code
}

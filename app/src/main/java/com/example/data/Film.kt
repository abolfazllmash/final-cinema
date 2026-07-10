package com.example.data

data class Cast(
    val n: String,        // Name of the actor
    val c: String? = null // Name of the character (optional)
)

data class Film(
    val id: String,
    val marquee: String,
    val fa: String,
    val native: String = "",
    val year: Int,
    val runtime: Int = 0,
    val genres: List<String> = emptyList(),
    val director: List<String> = emptyList(),
    val cast: List<Cast> = emptyList(),
    val lang: String,
    val country: String,
    val rating: Double,
    val posterUrl: String? = null,
    val overview: String? = null
)

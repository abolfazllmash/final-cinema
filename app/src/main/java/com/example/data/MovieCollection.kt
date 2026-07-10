package com.example.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SeasonPeriod(
    val from: String,
    val to: String
)

@JsonClass(generateAdapter = true)
data class MovieCollection(
    val id: String,
    val title: String,
    val subtitle: String = "",
    val emoji: String? = null,
    val filmIds: List<String> = emptyList(),
    val gradient: List<String> = emptyList(),
    val season: SeasonPeriod? = null
)

package com.example.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FilmApi {
    @GET("films")
    suspend fun films(
        @Query("q") q: String? = null,
        @Query("lang") lang: String? = null,
        @Query("country") country: String? = null,
        @Query("page") page: Int = 1
    ): List<Film>

    @GET("films/{id}")
    suspend fun film(
        @Path("id") id: String
    ): Film
}

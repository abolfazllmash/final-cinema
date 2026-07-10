package com.example.data

import android.content.Context
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

class FilmRepository(private val context: Context) {

    companion object {
        // Empty by default = offline assets. Fill with server URL for Phase 2-3 (e.g., "http://film.example.ir/api/")
        const val BASE_URL = "" 
        private const val TAG = "FilmRepository"
    }

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val listType = Types.newParameterizedType(List::class.java, Film::class.java)
    private val listAdapter = moshi.adapter<List<Film>>(listType)
    private val filmAdapter = moshi.adapter(Film::class.java)

    private val database = FilmDatabase.getDatabase(context)
    private val filmDao = database.filmDao()

    private val api: FilmApi? by lazy {
        if (BASE_URL.isNotEmpty()) {
            try {
                val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build()

                retrofit.create(FilmApi::class.java)
            } catch (e: Exception) {
                Log.e(TAG, "Error initializing Retrofit API client", e)
                null
            }
        } else {
            null
        }
    }

    suspend fun getFilms(): List<Film> = withContext(Dispatchers.IO) {
        // 1. Try server file first: DATA_BASE/films.json (offline-first: on any failure, fall back below)
        try {
            val remoteJson = RemoteJson.fetch("films.json")
            if (remoteJson != null) {
                val remoteFilms = listAdapter.fromJson(remoteJson) ?: emptyList()
                if (remoteFilms.size >= 50) {
                    val entities = remoteFilms.map { film -> FilmEntity(film.id, filmAdapter.toJson(film)) }
                    filmDao.clearAll()
                    filmDao.insertAll(entities)
                    Log.d(TAG, "Fetched & cached ${remoteFilms.size} films from server file.")
                    return@withContext remoteFilms
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Server films.json failed, falling back to cache/assets.", e)
        }

        // Check if assets/films.json has been updated in development/deployment
        val prefs = context.getSharedPreferences("film_prefs", Context.MODE_PRIVATE)
        val lastAssetSize = prefs.getLong("last_films_asset_size", 0L)
        var currentAssetSize = 0L
        try {
            context.assets.open("films.json").use { currentAssetSize = it.available().toLong() }
        } catch (e: Exception) {
            Log.e(TAG, "Error checking asset size", e)
        }

        if (currentAssetSize > 0 && currentAssetSize != lastAssetSize) {
            Log.d(TAG, "Asset films.json size changed from $lastAssetSize to $currentAssetSize. Clearing Room to force reload.")
            try {
                filmDao.clearAll()
            } catch (e: Exception) {
                Log.e(TAG, "Error clearing local database on asset update", e)
            }
        }

        // 2. Try Local Room database
        try {
            val cachedJson = filmDao.getAllJson()
            if (cachedJson.isNotEmpty()) {
                val cachedFilms = cachedJson.mapNotNull { json ->
                    filmAdapter.fromJson(json)
                }
                if (cachedFilms.size >= 200) {
                    Log.d(TAG, "Loaded ${cachedFilms.size} films from local Room database cache.")
                    return@withContext cachedFilms
                } else {
                    Log.d(TAG, "Local cache is outdated or too small (${cachedFilms.size}). Clearing to upgrade database.")
                    filmDao.clearAll()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error querying local Room database", e)
        }

        // 3. Fallback to assets/films.json
        try {
            Log.d(TAG, "Room is empty/failed. Loading default offline dataset from assets/films.json...")
            context.assets.open("films.json").use { inputStream ->
                InputStreamReader(inputStream).use { reader ->
                    val jsonString = reader.readText()
                    val assetFilms = listAdapter.fromJson(jsonString) ?: emptyList()
                    if (assetFilms.isNotEmpty()) {
                        // Populate Room for future quick offline launches
                        val entities = assetFilms.map { film ->
                            FilmEntity(film.id, filmAdapter.toJson(film))
                        }
                        try {
                            filmDao.insertAll(entities)
                            if (currentAssetSize > 0) {
                                prefs.edit().putLong("last_films_asset_size", currentAssetSize).apply()
                            }
                        } catch (dbEx: Exception) {
                            Log.e(TAG, "Failed to prepopulate Room database with asset films", dbEx)
                        }
                    }
                    return@withContext assetFilms
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Fatal error reading assets/films.json", e)
            return@withContext emptyList()
        }
    }
}
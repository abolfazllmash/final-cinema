package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Film
import com.example.data.FilmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UiState(
    val loading: Boolean = true,
    val all: List<Film> = emptyList(),
    val query: String = "",
    val lang: String? = null,
    val country: String? = null,
    val selected: Film? = null
) {
    val visible: List<Film> get() {
        return all.filter { film ->
            // Language Filter (Toggleable)
            val langMatch = lang == null || film.lang.equals(lang, ignoreCase = true)

            // Country Filter (Toggleable)
            val countryMatch = country == null || film.country.equals(country, ignoreCase = true)

            // Text Query Search (Case-Insensitive across title, directors, actors)
            val queryMatch = if (query.isBlank()) {
                true
            } else {
                val q = query.trim().lowercase()
                film.marquee.lowercase().contains(q) ||
                film.fa.lowercase().contains(q) ||
                film.native.lowercase().contains(q) ||
                film.director.any { it.lowercase().contains(q) } ||
                film.cast.any { 
                    it.n.lowercase().contains(q) || 
                    (it.c?.lowercase()?.contains(q) == true) 
                }
            }

            langMatch && countryMatch && queryMatch
        }
    }
}

data class TopListsJson(
    val imdb: List<ImdbMovie> = emptyList(),
    val rotten: List<ImdbMovie> = emptyList(),
    val metacritic: List<ImdbMovie> = emptyList()
)

class FilmViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = FilmRepository(app.applicationContext)
    private val prefs = app.getSharedPreferences("user_lists", Context.MODE_PRIVATE)

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val _imdbList = MutableStateFlow<List<ImdbMovie>>(emptyList())
    val imdbList: StateFlow<List<ImdbMovie>> = _imdbList.asStateFlow()

    private val _rottenList = MutableStateFlow<List<ImdbMovie>>(emptyList())
    val rottenList: StateFlow<List<ImdbMovie>> = _rottenList.asStateFlow()

    private val _metacriticList = MutableStateFlow<List<ImdbMovie>>(emptyList())
    val metacriticList: StateFlow<List<ImdbMovie>> = _metacriticList.asStateFlow()

    private val _watchedIds = MutableStateFlow<Set<String>>(emptySet())
    val watchedIds: StateFlow<Set<String>> = _watchedIds.asStateFlow()

    private val _watchlistIds = MutableStateFlow<Set<String>>(emptySet())
    val watchlistIds: StateFlow<Set<String>> = _watchlistIds.asStateFlow()

    private val _tasteSeeds = MutableStateFlow<Set<String>>(emptySet())
    val tasteSeeds: StateFlow<Set<String>> = _tasteSeeds.asStateFlow()

    private val _forYou = MutableStateFlow<List<Film>>(emptyList())
    val forYou: StateFlow<List<Film>> = _forYou.asStateFlow()

    private val _themeStyle = MutableStateFlow("classic")
    val themeStyle: StateFlow<String> = _themeStyle.asStateFlow()

    private val _displayName = MutableStateFlow("کاربر مهمان")
    val displayName: StateFlow<String> = _displayName.asStateFlow()

    private val _selectedAvatar = MutableStateFlow("default")
    val selectedAvatar: StateFlow<String> = _selectedAvatar.asStateFlow()

    init {
        _watchedIds.value = prefs.getStringSet("watched_ids", emptySet()) ?: emptySet()
        _watchlistIds.value = prefs.getStringSet("watchlist_ids", emptySet()) ?: emptySet()
        _tasteSeeds.value = prefs.getStringSet("taste_seeds", emptySet()) ?: emptySet()
        _themeStyle.value = prefs.getString("theme_style", "classic") ?: "classic"
        _displayName.value = prefs.getString("display_name", "کاربر مهمان") ?: "کاربر مهمان"
        _selectedAvatar.value = prefs.getString("selected_avatar", "default") ?: "default"
        loadFilms()
        loadTopLists()
    }

    fun setTasteSeeds(seeds: Set<String>) {
        _tasteSeeds.value = seeds
        prefs.edit().putStringSet("taste_seeds", seeds).apply()
        updateRecommendations()
    }

    fun updateRecommendations() {
        val allFilms = _state.value.all
        if (allFilms.isEmpty()) return

        val watched = _watchedIds.value
        val watchlist = _watchlistIds.value
        val seeds = _tasteSeeds.value

        // If there are no user signals, fallback to highest rated movies
        if (watched.isEmpty() && watchlist.isEmpty() && seeds.isEmpty()) {
            _forYou.value = allFilms
                .sortedByDescending { it.rating }
                .take(15)
            return
        }

        // Exclude already watched films from the recommendations
        val candidates = allFilms.filter { !watched.contains(it.id) }

        // Build profile models
        val genreCounts = mutableMapOf<String, Int>()
        val directorCounts = mutableMapOf<String, Int>()

        // Helper to add signals
        fun addSignals(ids: Set<String>, weight: Int) {
            allFilms.filter { ids.contains(it.id) }.forEach { film ->
                film.genres.forEach { g ->
                    genreCounts[g] = (genreCounts[g] ?: 0) + weight
                }
                film.director.forEach { d ->
                    directorCounts[d] = (directorCounts[d] ?: 0) + weight
                }
            }
        }

        addSignals(seeds, 4)
        addSignals(watchlist, 3)
        addSignals(watched, 2)

        // Score candidates
        val scored = candidates.map { film ->
            var score = 0.0

            film.genres.forEach { g ->
                score += (genreCounts[g] ?: 0) * 1.5
            }

            film.director.forEach { d ->
                score += (directorCounts[d] ?: 0) * 3.0
            }

            score += film.rating * 1.2
            score += (film.year - 1900) * 0.02

            if (watchlist.contains(film.id)) {
                score += 5.0
            }

            Pair(film, score)
        }

        _forYou.value = scored
            .sortedByDescending { it.second }
            .map { it.first }
            .take(15)
    }

    fun setThemeStyle(style: String) {
        _themeStyle.value = style
        prefs.edit().putString("theme_style", style).apply()
    }

    fun setAvatarId(id: String) {
        _selectedAvatar.value = id
        prefs.edit().putString("selected_avatar", id).apply()
    }

    fun setDisplayName(name: String): Boolean {
        val clean = name.trim()
        if (clean.isBlank() || clean.length > 30 || !isNameAllowed(clean)) return false
        _displayName.value = clean
        prefs.edit().putString("display_name", clean).apply()
        return true
    }

    // بخش‌هایی که در نام مجاز نیستند (نرمال‌شده: بدون فاصله/نیم‌فاصله). قابل گسترش.
    private val bannedNameFragments = listOf(
        // اسامی/عناوین سیاسی و رهبران (جلوگیری از جعل هویت)
        "خامنه", "خمینی", "ولیفقیه", "رهبرانقلاب", "رهبرمعظم", "بیترهبری",
        "امامخمینی", "سیدعلیخامنه", "احمدینژاد", "رییسجمهور",
        // ناسزا / توهین به مقدسات (نمونه‌های پایه)
        "کصکش", "کسکش", "کیری", "کونی", "جنده", "کوسکش", "مادرجنده",
        "پفیوز", "حرومزاده", "حرامزاده", "بیناموس", "کسخل", "گاییدم", "کیرم"
    )

    private fun isNameAllowed(name: String): Boolean {
        val n = name.lowercase()
            .replace("ي", "ی").replace("ك", "ک").replace("ۀ", "ه").replace("ة", "ه")
            .replace(Regex("[\\s\\u200c._\\-*+]+"), "")
        return bannedNameFragments.none { n.contains(it) }
    }

    fun toggleWatched(filmId: String) {
        val current = _watchedIds.value.toMutableSet()
        if (current.contains(filmId)) {
            current.remove(filmId)
        } else {
            current.add(filmId)
            // اگر فیلم در لیست تماشا بود، با «دیده‌ام»‌شدن خودکار از آن خارج شود
            val wl = _watchlistIds.value.toMutableSet()
            if (wl.remove(filmId)) {
                _watchlistIds.value = wl
                prefs.edit().putStringSet("watchlist_ids", wl).apply()
            }
        }
        _watchedIds.value = current
        prefs.edit().putStringSet("watched_ids", current).apply()
        updateRecommendations()
    }

    fun toggleWatchlist(filmId: String) {
        val current = _watchlistIds.value.toMutableSet()
        if (current.contains(filmId)) {
            current.remove(filmId)
        } else {
            current.add(filmId)
            // اگر فیلم در لیست دیده‌شده‌ها بود، با «می‌بینم»‌شدن خودکار از آن خارج شود
            val wd = _watchedIds.value.toMutableSet()
            if (wd.remove(filmId)) {
                _watchedIds.value = wd
                prefs.edit().putStringSet("watched_ids", wd).apply()
            }
        }
        _watchlistIds.value = current
        prefs.edit().putStringSet("watchlist_ids", current).apply()
        updateRecommendations()
    }

    private fun loadTopLists() {
        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                // اول از سرور (DATA_BASE/top_lists.json)، اگر نشد از assets
                val jsonString = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                    com.example.data.RemoteJson.fetch("top_lists.json")
                        ?: context.assets.open("top_lists.json").use { it.bufferedReader().readText() }
                }
                val moshi = com.squareup.moshi.Moshi.Builder()
                    .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
                    .build()
                val adapter = moshi.adapter(TopListsJson::class.java)
                val wrapper = adapter.fromJson(jsonString)
                if (wrapper != null) {
                    _imdbList.value = wrapper.imdb
                    _rottenList.value = wrapper.rotten
                    _metacriticList.value = wrapper.metacritic
                }
            } catch (e: Exception) {
                android.util.Log.e("FilmViewModel", "Failed to load top_lists.json", e)
            }
        }
    }

    fun loadFilms() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            val films = repository.getFilms()
            _state.value = _state.value.copy(loading = false, all = films)
            updateRecommendations()
        }
    }

    fun onQuery(q: String) {
        _state.value = _state.value.copy(query = q)
    }

    fun onLang(c: String) {
        val current = _state.value.lang
        val next = if (current.equals(c, ignoreCase = true)) null else c
        _state.value = _state.value.copy(lang = next)
    }

    fun onCountry(c: String) {
        val current = _state.value.country
        val next = if (current.equals(c, ignoreCase = true)) null else c
        _state.value = _state.value.copy(country = next)
    }

    fun onSelect(f: Film) {
        _state.value = _state.value.copy(selected = f)
    }

    fun onBack() {
        _state.value = _state.value.copy(selected = null)
    }
}

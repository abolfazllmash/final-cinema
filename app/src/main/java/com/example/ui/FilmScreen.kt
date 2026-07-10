package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Close
import android.content.Intent
import android.widget.Toast
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import android.content.Context
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.ui.theme.Bar
import com.example.ui.theme.Bg
import com.example.ui.theme.Blue
import com.example.ui.theme.Dim
import com.example.ui.theme.Faint
import com.example.ui.theme.Green
import com.example.ui.theme.GreenInk
import com.example.ui.theme.Line
import com.example.ui.theme.Line2
import com.example.ui.theme.Orange
import com.example.ui.theme.PTitle
import com.example.ui.theme.Surface
import com.example.ui.theme.Tile
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.grid.GridItemSpan

// Extension function to convert English digits to Persian digits
fun String.toFaDigits(): String {
    var result = this
    val persianDigits = arrayOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")
    for (i in 0..9) {
        result = result.replace(i.toString(), persianDigits[i])
    }
    return result
}

fun Int.toFaDigits(): String = this.toString().toFaDigits()

@Composable
fun FilmScreen(
    vm: FilmViewModel = viewModel()
) {
    val context = LocalContext.current
    val sharedPrefs = remember(context) { context.getSharedPreferences("filmbaz_prefs", Context.MODE_PRIVATE) }

    val state by vm.state.collectAsState()
    val watchedIds by vm.watchedIds.collectAsState()
    val watchlistIds by vm.watchlistIds.collectAsState()

    // Flag to manage our elegant Landing onboarding screen flow, persisting once started
    var showLanding by rememberSaveable {
        mutableStateOf(sharedPrefs.getBoolean("show_landing", true))
    }
    var showTastePicker by rememberSaveable {
        mutableStateOf(!sharedPrefs.getBoolean("taste_picker_done", false))
    }
    var isSearchActive by rememberSaveable { mutableStateOf(false) }
    var isFilterSheetOpen by rememberSaveable { mutableStateOf(false) }
    var activeTab by rememberSaveable { mutableStateOf("browse") }
    var selectedCollection by remember { mutableStateOf<com.example.data.MovieCollection?>(null) }

    // Persistent scroll & tab states to prevent resets when switching between Detail and List/Tabs
    val browseGridState = androidx.compose.foundation.lazy.grid.rememberLazyGridState()
    val collectionGridState = remember(selectedCollection?.id) {
        androidx.compose.foundation.lazy.grid.LazyGridState(0, 0)
    }
    val genresListState = androidx.compose.foundation.lazy.rememberLazyListState()
    val bestImdbListState = androidx.compose.foundation.lazy.rememberLazyListState()
    val bestRottenListState = androidx.compose.foundation.lazy.rememberLazyListState()
    val bestMetacriticListState = androidx.compose.foundation.lazy.rememberLazyListState()
    val userProfileListState = androidx.compose.foundation.lazy.rememberLazyListState()
    var bestSubTab by rememberSaveable { mutableStateOf("imdb") }

    if (selectedCollection != null && state.selected == null) {
        BackHandler {
            selectedCollection = null
        }
    }

    if (showLanding) {
        LandingScreen(onStartClick = {
            sharedPrefs.edit().putBoolean("show_landing", false).apply()
            showLanding = false
        })
    } else if (showTastePicker) {
        TastePickerScreen(
            films = state.all,
            onFinish = { selectedIds ->
                sharedPrefs.edit().putBoolean("taste_picker_done", true).apply()
                showTastePicker = false
                if (selectedIds.isNotEmpty()) {
                    vm.setTasteSeeds(selectedIds)
                }
            }
        )
    } else {
        AnimatedContent(
            targetState = state.selected,
            transitionSpec = {
                if (targetState != null) {
                    // Detail screen slides up and fades in
                    (slideInVertically(animationSpec = tween(260)) { it / 8 } + fadeIn(animationSpec = tween(260)))
                        .togetherWith(fadeOut(animationSpec = tween(200)))
                } else {
                    // Backing out from Detail screen
                    (slideInVertically(animationSpec = tween(200)) { -it / 8 } + fadeIn(animationSpec = tween(200)))
                        .togetherWith(slideOutVertically(animationSpec = tween(260)) { it / 8 } + fadeOut(animationSpec = tween(260)))
                }
            },
            label = "home_detail_navigation_transition"
        ) { selectedFilm ->
            if (selectedFilm != null) {
                FilmDetail(
                    film = selectedFilm,
                    watchedIds = watchedIds,
                    watchlistIds = watchlistIds,
                    onToggleWatched = { vm.toggleWatched(selectedFilm.id) },
                    onToggleWatchlist = { vm.toggleWatchlist(selectedFilm.id) },
                    onBack = { vm.onBack() }
                )
            } else {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Bg,
                    topBar = {
                        Column {
                            TopBar(
                                onSearchClick = { isSearchActive = !isSearchActive },
                                onFilterClick = { isFilterSheetOpen = true },
                                showSearchIcon = activeTab == "browse" && selectedCollection == null,
                                showFilterIcon = activeTab == "browse" && selectedCollection == null
                            )
                            if (isSearchActive && activeTab == "browse" && selectedCollection == null) {
                                SearchBar(
                                    query = state.query,
                                    onQueryChange = { vm.onQuery(it) }
                                )
                            }
                            if (activeTab == "browse" && selectedCollection == null && (state.lang != null || state.country != null)) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Bar)
                                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("فیلترهای فعال:", color = Dim, fontSize = 11.sp)
                                    state.lang?.let { lang ->
                                        ActiveFilterChip(
                                            label = getLanguageFa(lang),
                                            onClear = { vm.onLang(lang) }
                                        )
                                    }
                                    state.country?.let { country ->
                                        ActiveFilterChip(
                                            label = getCountryFa(country),
                                            onClear = { vm.onCountry(country) }
                                        )
                                    }
                                }
                            }
                            HorizontalDivider(color = Line, thickness = 1.dp)
                        }
                    },
                    bottomBar = {
                        BottomNavBar(
                            selectedTab = activeTab,
                            onTabSelected = { activeTab = it }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (activeTab) {
                            "browse" -> {
                                if (selectedCollection != null) {
                                    CollectionDetailView(
                                        collection = selectedCollection!!,
                                        allFilms = state.all,
                                        gridState = collectionGridState,
                                        onFilmClick = { vm.onSelect(it) },
                                        onBack = { selectedCollection = null }
                                    )
                                } else {
                                    BrowseContent(
                                        state = state,
                                        vm = vm,
                                        gridState = browseGridState,
                                        isSearchActive = isSearchActive,
                                        onCollectionClick = { selectedCollection = it }
                                    )
                                }
                            }
                            "lists" -> {
                                GenreListsView(
                                    films = state.all,
                                    listState = genresListState,
                                    onFilmClick = { vm.onSelect(it) }
                                )
                            }
                            "best" -> {
                                BestWorldFilmsView(
                                    vm = vm,
                                    selectedListTab = bestSubTab,
                                    onTabChange = { bestSubTab = it },
                                    imdbListState = bestImdbListState,
                                    rottenListState = bestRottenListState,
                                    metacriticListState = bestMetacriticListState
                                )
                            }
                            "profile" -> {
                                UserProfileView(
                                    films = state.all,
                                    watchedIds = watchedIds,
                                    watchlistIds = watchlistIds,
                                    vm = vm,
                                    listState = userProfileListState,
                                    onFilmClick = { vm.onSelect(it) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (isFilterSheetOpen) {
        @OptIn(ExperimentalMaterial3Api::class)
        ModalBottomSheet(
            onDismissRequest = { isFilterSheetOpen = false },
            containerColor = Bg,
            contentColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
                    .navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "فیلترها",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "بستن",
                        fontSize = 13.sp,
                        color = Dim,
                        modifier = Modifier
                            .clickable { isFilterSheetOpen = false }
                            .padding(4.dp)
                    )
                }

                // 1. Language filter chips
                val uniqueLangs = remember(state.all) { state.all.map { it.lang }.distinct() }
                if (uniqueLangs.isNotEmpty()) {
                    LanguageFilterRow(
                        languages = uniqueLangs,
                        selectedLang = state.lang,
                        onLangClick = { vm.onLang(it) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Country filter chips
                val uniqueCountries = remember(state.all) { state.all.map { it.country }.distinct() }
                if (uniqueCountries.isNotEmpty()) {
                    CountryFilterRow(
                        countries = uniqueCountries,
                        selectedCountry = state.country,
                        onCountryClick = { vm.onCountry(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun TopBar(
    onSearchClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    showSearchIcon: Boolean = true,
    showFilterIcon: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(Bar)
            .height(56.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Right side: Name grouped with the new elegant app logo
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.app_logo_main_1783193355369),
                contentDescription = "Film App Logo",
                modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "فیلم‌بازی",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PTitle
            )
        }

        // Left side: Action buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showFilterIcon) {
                IconButton(
                    onClick = onFilterClick,
                    modifier = Modifier.size(36.dp).testTag("top_bar_filter_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "فیلترها",
                        tint = Dim,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            if (showSearchIcon) {
                IconButton(
                    onClick = onSearchClick,
                    modifier = Modifier.size(36.dp).testTag("top_bar_search_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "جست‌وجو",
                        tint = Dim,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("search_text_field"),
            placeholder = {
                Text(
                    text = "جست‌وجوی فیلم، کارگردان، بازیگر...",
                    fontSize = 14.sp,
                    color = Faint
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Faint
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "پاک کردن",
                            tint = Faint
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Surface,
                unfocusedContainerColor = Surface,
                focusedBorderColor = Green,
                unfocusedBorderColor = Line,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
    }
}

@Composable
fun LanguageFilterRow(
    languages: List<String>,
    selectedLang: String?,
    onLangClick: (String) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 6.dp)) {
        Text(
            text = "زبان فیلم:",
            fontSize = 12.sp,
            color = Faint,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 4.dp)
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(languages) { langCode ->
                val faLabel = getLanguageFa(langCode)
                val isSelected = selectedLang?.equals(langCode, ignoreCase = true) == true

                FilterChip(
                    label = faLabel,
                    isSelected = isSelected,
                    onClick = { onLangClick(langCode) },
                    testTag = "lang_chip_$langCode"
                )
            }
        }
    }
}

@Composable
fun CountryFilterRow(
    countries: List<String>,
    selectedCountry: String?,
    onCountryClick: (String) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(
            text = "کشور سازنده:",
            fontSize = 12.sp,
            color = Faint,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 4.dp)
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(countries) { countryCode ->
                val faLabel = getCountryFa(countryCode)
                val isSelected = selectedCountry?.equals(countryCode, ignoreCase = true) == true

                FilterChip(
                    label = faLabel,
                    isSelected = isSelected,
                    onClick = { onCountryClick(countryCode) },
                    testTag = "country_chip_$countryCode"
                )
            }
        }
    }
}

@Composable
fun ActiveFilterChip(
    label: String,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Green.copy(alpha = 0.15f))
            .border(1.dp, Green.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
            .clickable { onClear() }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            color = Green,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "حذف",
            tint = Green,
            modifier = Modifier.size(12.dp)
        )
    }
}

@Composable
fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val bgColor = if (isSelected) Green else Surface
    val textColor = if (isSelected) GreenInk else Dim
    val borderColor = if (isSelected) Color.Transparent else Line
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal

    Box(
        modifier = Modifier
            .testTag(testTag)
            .clip(RoundedCornerShape(100.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(100.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 13.sp,
            fontWeight = fontWeight
        )
    }
}

@Composable
fun EmptyState(
    hasActiveFilters: Boolean,
    onResetClick: () -> Unit
) {
    val strokeColor = Line2
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawRoundRect(
                        color = strokeColor,
                        cornerRadius = CornerRadius(8.dp.toPx()),
                        style = Stroke(
                            width = 1.5.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        )
                    )
                }
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "فیلمی پیدا نشد!",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (hasActiveFilters) {
                    "هیچ فیلمی با فیلترها یا عبارت جست‌وجوی فعلی شما مطابقت ندارد."
                } else {
                    "لیست فیلم‌ها در حال حاضر خالی است."
                },
                color = Dim,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            if (hasActiveFilters) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "پاک کردن فیلترها",
                    color = Green,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable(onClick = onResetClick)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun BottomNavBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .background(Bar)
            .navigationBarsPadding()
    ) {
        HorizontalDivider(color = Line, thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = Icons.Default.Home,
                label = "خانه",
                isSelected = selectedTab == "browse",
                onClick = { onTabSelected("browse") }
            )
            BottomNavItem(
                icon = Icons.Default.Star,
                label = "برترین‌ها",
                isSelected = selectedTab == "best",
                onClick = { onTabSelected("best") }
            )
            BottomNavItem(
                icon = Icons.Default.Person,
                label = "پروفایل",
                isSelected = selectedTab == "profile",
                onClick = { onTabSelected("profile") }
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = if (isSelected) Green else Dim
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = label,
            color = color,
            fontSize = 10.sp,
            fontWeight = fontWeight
        )
    }
}

@Composable
fun BrowseContent(
    state: UiState,
    vm: FilmViewModel,
    gridState: androidx.compose.foundation.lazy.grid.LazyGridState,
    isSearchActive: Boolean,
    onCollectionClick: (com.example.data.MovieCollection) -> Unit
) {
    val displayName by vm.displayName.collectAsState()
    val watchedIds by vm.watchedIds.collectAsState()
    val watchlistIds by vm.watchlistIds.collectAsState()
    val forYou by vm.forYou.collectAsState()

    var hideWatched by rememberSaveable { mutableStateOf(false) }

    val filteredFilms = remember(state.visible, watchedIds, hideWatched) {
        if (hideWatched) {
            state.visible.filter { !watchedIds.contains(it.id) }
        } else {
            state.visible
        }
    }

    val watchlistFilms = remember(state.all, watchlistIds) {
        state.all.filter { watchlistIds.contains(it.id) }
    }

    val genreGroups = remember(filteredFilms) {
        val genres = filteredFilms.flatMap { it.genres }.distinct().filter { it.isNotBlank() }
        genres.map { genre ->
            genre to filteredFilms.filter { it.genres.contains(genre) }
        }.filter { it.second.isNotEmpty() }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (state.loading) {
                CircularProgressIndicator(
                    color = Green,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .testTag("films_loading")
                )
            } else if (filteredFilms.isEmpty()) {
                EmptyState(
                    hasActiveFilters = state.query.isNotEmpty() || state.lang != null || state.country != null || hideWatched,
                    onResetClick = {
                        vm.onQuery("")
                        state.lang?.let { vm.onLang(it) }
                        state.country?.let { vm.onCountry(it) }
                        hideWatched = false
                    }
                )
            } else {
                BoxWithConstraints(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val columnsCount = when {
                        maxWidth < 600.dp -> 3
                        maxWidth < 900.dp -> 4
                        else -> 5
                    }
                    LazyVerticalGrid(
                        state = gridState,
                        columns = GridCells.Fixed(columnsCount),
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("films_grid"),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        val showSearchResults = isSearchActive || 
                                                state.query.isNotBlank() || 
                                                state.lang != null || 
                                                state.country != null
                        if (showSearchResults) {
                            // ۱. هدر نتایج جست‌وجو
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(width = 3.5.dp, height = 13.dp)
                                            .clip(RoundedCornerShape(30))
                                            .background(Green)
                                    )
                                    Text(
                                        text = when {
                                            state.query.isNotBlank() -> "نتایج جست‌وجو برای «${state.query}»"
                                            state.lang != null || state.country != null -> "فیلم‌های فیلتر شده"
                                            else -> "جست‌وجوی فیلم‌ها"
                                        },
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = "${filteredFilms.size.toFaDigits()} فیلم",
                                        fontSize = 12.sp,
                                        color = Dim
                                    )
                                }
                            }

                            // ۲. فیلم‌های فیلتر شده به عنوان کارت‌های مستقل
                            itemsIndexed(filteredFilms) { index, film ->
                                FilmCard(
                                    film = film,
                                    index = index,
                                    onClick = { vm.onSelect(film) }
                                )
                            }
                        } else {
                            // ۱. سلام و خوش‌آمدگویی (Greeting)
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(Surface, CircleShape)
                                            .border(1.dp, Green.copy(alpha = 0.5f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (displayName.isNotEmpty()) displayName.take(1).uppercase() else "U",
                                            color = Green,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "سلام، $displayName!",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "امروز آماده تماشای چه فیلمی هستی؟",
                                            fontSize = 11.sp,
                                            color = Dim
                                        )
                                    }
                                }
                            }

                            // ۲. بخش «ادامه بده» (Watchlist) در صورت غیرخالی بودن لیست تماشا
                            if (watchlistFilms.isNotEmpty()) {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    WatchlistRow(watchlistFilms = watchlistFilms, onFilmClick = { vm.onSelect(it) })
                                }
                            }

                            // ۳. بخش «برای تو» (Recommendations) در صورت غیرخالی بودن
                            if (forYou.isNotEmpty()) {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    RecommendationsRow(recommendedFilms = forYou, onFilmClick = { vm.onSelect(it) })
                                }
                            }

                            // ۴. بخش حس‌و‌حال و لیست‌های مناسبتی/تابستانه (Mood Shelf)
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Column(modifier = Modifier.padding(bottom = 8.dp)) {
                                    MoodSection(all = state.all, vm = vm, onOpen = onCollectionClick)
                                }
                            }

                            // ۵. هدر فیلم‌های پیشنهادی به همراه کلید پنهان‌سازی فیلم‌های دیده‌شده
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(width = 3.5.dp, height = 13.dp)
                                                .clip(RoundedCornerShape(30))
                                                .background(Green)
                                        )
                                        Text(
                                            text = "فیلم‌های پیشنهادی",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = PTitle
                                        )
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    
                                    // کلید پنهان‌سازی فیلم‌های دیده‌شده (Hide Watched)
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (hideWatched) Green.copy(alpha = 0.15f) else Surface)
                                            .border(1.dp, if (hideWatched) Green else Line, RoundedCornerShape(6.dp))
                                            .clickable { hideWatched = !hideWatched }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "پنهان کردن دیده‌شده‌ها",
                                            fontSize = 11.sp,
                                            color = if (hideWatched) Green else Dim
                                        )
                                    }
                                    
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "${filteredFilms.size.toFaDigits()} مورد",
                                        fontSize = 11.sp,
                                        color = Faint
                                    )
                                }
                            }

                            // ۶. ردیف‌های ژانر به صورت افقی
                            genreGroups.forEach { (genre, genreFilms) ->
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    GenreRow(
                                        genreName = genre,
                                        films = genreFilms,
                                        onFilmClick = { vm.onSelect(it) },
                                        onSeeMoreClick = {
                                            val collection = com.example.data.MovieCollection(
                                                id = "genre_$genre",
                                                title = genre,
                                                subtitle = "فیلم‌های منتخب ژانر $genre",
                                                emoji = "🎬",
                                                filmIds = genreFilms.map { it.id },
                                                gradient = listOf("#11998e", "#38ef7d")
                                            )
                                            onCollectionClick(collection)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WatchlistRow(
    watchlistFilms: List<com.example.data.Film>,
    onFilmClick: (com.example.data.Film) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(width = 3.5.dp, height = 13.dp)
                    .clip(RoundedCornerShape(30))
                    .background(Green)
            )
            Text(
                text = "ادامه بده (لیست تماشا)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PTitle
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(watchlistFilms) { film ->
                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onFilmClick(film) }
                ) {
                    if (film.posterUrl != null && film.posterUrl.isNotEmpty()) {
                        androidx.compose.foundation.Image(
                            painter = coil.compose.rememberAsyncImagePainter(film.posterUrl),
                            contentDescription = film.fa,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .background(Line),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = film.fa,
                                fontSize = 10.sp,
                                color = Dim,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecommendationsRow(
    recommendedFilms: List<com.example.data.Film>,
    onFilmClick: (com.example.data.Film) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(width = 3.5.dp, height = 13.dp)
                    .clip(RoundedCornerShape(30))
                    .background(Green)
            )
            Text(
                text = "برای تو (پیشنهادی)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PTitle
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(recommendedFilms) { film ->
                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onFilmClick(film) }
                ) {
                    if (film.posterUrl != null && film.posterUrl.isNotEmpty()) {
                        androidx.compose.foundation.Image(
                            painter = coil.compose.rememberAsyncImagePainter(film.posterUrl),
                            contentDescription = film.fa,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .background(Line),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = film.fa,
                                fontSize = 10.sp,
                                color = Dim,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GenreRow(
    genreName: String,
    films: List<com.example.data.Film>,
    onFilmClick: (com.example.data.Film) -> Unit,
    onSeeMoreClick: () -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 3.5.dp, height = 13.dp)
                        .clip(RoundedCornerShape(30))
                        .background(Green)
                )
                Text(
                    text = genreName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PTitle
                )
            }
            Text(
                text = "مشاهده همه",
                fontSize = 11.sp,
                color = Green,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clickable { onSeeMoreClick() }
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }
        
        val displayFilms = remember(films) { films.take(6) }
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            itemsIndexed(displayFilms) { index, film ->
                FilmCard(
                    film = film,
                    index = index,
                    onClick = { onFilmClick(film) },
                    modifier = Modifier.width(104.dp)
                )
            }
            
            item {
                Box(
                    modifier = Modifier
                        .width(104.dp)
                        .aspectRatio(2f / 3f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Surface)
                        .border(1.dp, Green.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                        .clickable { onSeeMoreClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "مشاهده بیشتر",
                            tint = Green,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "مشاهده بیشتر",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Green
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GenreListsView(
    films: List<com.example.data.Film>,
    listState: androidx.compose.foundation.lazy.LazyListState = androidx.compose.foundation.lazy.rememberLazyListState(),
    onFilmClick: (com.example.data.Film) -> Unit
) {
    val genres = films.flatMap { it.genres }.distinct().filter { it.isNotBlank() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(width = 3.5.dp, height = 13.dp)
                    .clip(RoundedCornerShape(30))
                    .background(Orange)
            )
            Text(
                text = "لیست‌های موضوعی (ژانر)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = PTitle
            )
        }

        if (films.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Green)
            }
        } else {
            androidx.compose.foundation.lazy.LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(genres) { genre ->
                    val genreFilms = films.filter { it.genres.contains(genre) }
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = genre,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "${genreFilms.size.toFaDigits()} فیلم",
                                fontSize = 11.sp,
                                color = Faint
                            )
                        }

                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            itemsIndexed(genreFilms) { index, film ->
                                FilmCard(
                                    film = film,
                                    index = index,
                                    onClick = { onFilmClick(film) },
                                    modifier = Modifier.width(104.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

data class ImdbMovie(
    val id: String,
    val rank: Int,
    val titleFa: String,
    val titleEn: String,
    val rating: String,
    val year: String,
    val director: String,
    val badge: String,
    val desc: String,
    val cast: List<String> = emptyList(),
    val oscarAward: String? = null
)

val imdbList = listOf(
    ImdbMovie(
        id = "tt0111161",
        rank = 1,
        titleFa = "رستگاری در شاوشنگ",
        titleEn = "The Shawshank Redemption",
        rating = "9.3",
        year = "1994",
        director = "فرانک دارابونت",
        badge = "رتبه ۱ تاریخ",
        desc = "دو مرد زندانی در طول چندین سال با هم صمیمی می‌شوند و از طریق رفتارهای تسکین‌دهنده به رستگاری مشترک می‌رسند."
    ),
    ImdbMovie(
        id = "tt0068646",
        rank = 2,
        titleFa = "پدرخوانده",
        titleEn = "The Godfather",
        rating = "9.2",
        year = "1972",
        director = "فرانسیس فورد کاپولا",
        badge = "شاهکار مافیایی تاریخ",
        desc = "رییس سالخورده یک سازمان جرایم سازمان‌یافته سلسله کنترل را به پسر کوچکش واگذار می‌کند.",
        oscarAward = "بهترین فیلم"
    ),
    ImdbMovie(
        id = "tt0468569",
        rank = 3,
        titleFa = "شوالیه تاریکی",
        titleEn = "The Dark Knight",
        rating = "9.0",
        year = "2008",
        director = "کریستوفر نولان",
        badge = "برترین فیلم ابرقهرمانی",
        desc = "وقتی تهدیدی به نام جوکر خرابی و هرج‌ومرج را در گاتهام ایجاد می‌کند، بتمن باید عدالت را برقرار کند."
    ),
    ImdbMovie(
        id = "tt0050083",
        rank = 4,
        titleFa = "۱۲ مرد خشمگین",
        titleEn = "12 Angry Men",
        rating = "9.0",
        year = "1957",
        director = "سیدنی لومت",
        badge = "برترین درام دادگاهی",
        desc = "یک هییت منصفه دوازده نفره در حال بررسی پرونده قتل یک نوجوان هستند که نظر یکی از اعضا همه چیز را تغییر می‌دهد."
    ),
    ImdbMovie(
        id = "tt0110912",
        rank = 5,
        titleFa = "داستان عامه‌پسند",
        titleEn = "Pulp Fiction",
        rating = "8.9",
        year = "1994",
        director = "کوینتین تارانتینو",
        badge = "شاهکار پست‌مدرن",
        desc = "داستان‌های متقاطع دو آدم‌کش حرفه‌ای، یک بوکسور و همسر یک گانگستر در لس‌آنجلس."
    ),
    ImdbMovie(
        id = "tt0108052",
        rank = 6,
        titleFa = "فهرست شیندلر",
        titleEn = "Schindler's List",
        rating = "9.0",
        year = "1993",
        director = "استیون اسپیلبرگ",
        badge = "شاهکار حماسی جنگی",
        desc = "داستان واقعی اسکار شیندلر که توانست جان بیش از هزار یهودی را در جنگ جهانی دوم نجات دهد.",
        oscarAward = "بهترین فیلم و کارگردانی"
    )
)

val rottenList = listOf(
    ImdbMovie(
        id = "tt0033467",
        rank = 1,
        titleFa = "همشهری کین",
        titleEn = "Citizen Kane",
        rating = "99%",
        year = "1941",
        director = "اورسن ولز",
        badge = "شاهکار کلاسیک سینما",
        desc = "در پی درگذشت یک روزنامه‌نگار بزرگ، گروهی از خبرنگاران در تلاشند تا معنای آخرین کلمه او یعنی رزباد را کشف کنند."
    ),
    ImdbMovie(
        id = "tt0034583",
        rank = 2,
        titleFa = "کازابلانکا",
        titleEn = "Casablanca",
        rating = "99%",
        year = "1942",
        director = "مایکل کورتیز",
        badge = "رمانتیک کلاسیک ماندگار",
        desc = "در طول جنگ جهانی دوم، یک کافه‌دار آمریکایی در کازابلانکا با عشق سابق خود روبرو می‌شود.",
        oscarAward = "بهترین فیلم و کارگردانی"
    ),
    ImdbMovie(
        id = "tt6751668",
        rank = 3,
        titleFa = "انگل",
        titleEn = "Parasite",
        rating = "99%",
        year = "2019",
        director = "بونگ جون-هو",
        badge = "شاهکار تعلیق اجتماعی",
        desc = "یک خانواده فقیر با ترفندهای جالب وارد زندگی و خانه یک خانواده بسیار ثروتمند می‌شوند.",
        oscarAward = "بهترین فیلم و کارگردانی"
    ),
    ImdbMovie(
        id = "tt0071562",
        rank = 4,
        titleFa = "پدرخوانده: قسمت دوم",
        titleEn = "The Godfather Part II",
        rating = "96%",
        year = "1974",
        director = "فرانسیس فورد کاپولا",
        badge = "برترین دنباله تاریخ سینما",
        desc = "بررسی زندگی مایکل کورلیونه در نقش دون جدید و فلاش‌بک به دوران جوانی پدرش ویتو کورلیونه.",
        oscarAward = "بهترین فیلم و کارگردانی"
    ),
    ImdbMovie(
        id = "tt0044040",
        rank = 5,
        titleFa = "آواز در باران",
        titleEn = "Singin' in the Rain",
        rating = "100%",
        year = "1952",
        director = "جین کلی",
        badge = "برترین موزیکال تاریخ",
        desc = "تحول سینمای صامت به ناطق در هالیوود و داستان چند هنرمند که تلاش می‌کنند خود را با آن وفق دهند."
    ),
    ImdbMovie(
        id = "tt0047396",
        rank = 6,
        titleFa = "پنجره پشتی",
        titleEn = "Rear Window",
        rating = "99%",
        year = "1954",
        director = "آلفرد هیچکاک",
        badge = "شاهکار تعلیق هیچکاک",
        desc = "یک عکاس خانه‌نشین با نگاه کردن به همسایگانش مشکوک به وقوع یک قتل در ساختمان روبرو می‌شود."
    )
)

val metacriticList = listOf(
    ImdbMovie(
        id = "tt0056172",
        rank = 1,
        titleFa = "لورنس عربستان",
        titleEn = "Lawrence of Arabia",
        rating = "100",
        year = "1962",
        director = "دیوید لین",
        badge = "ماجراجویی حماسی تاریخ",
        desc = "داستان یک افسر انگلیسی پرشور که قبایل پراکنده عرب را برای جنگ با ترک‌های عثمانی متحد می‌کند.",
        oscarAward = "بهترین فیلم و کارگردانی"
    ),
    ImdbMovie(
        id = "tt0052357",
        rank = 2,
        titleFa = "سرگیجه",
        titleEn = "Vertigo",
        rating = "100",
        year = "1958",
        director = "آلفرد هیچکاک",
        badge = "شاهکار روانشناختی تاریخ",
        desc = "یک کارآگاه سابق پلیس که از ترس ارتفاع رنج می‌برد، استخدام می‌شود تا رفتار عجیب همسر یکی از دوستانش را بررسی کند."
    ),
    ImdbMovie(
        id = "tt15398776",
        rank = 3,
        titleFa = "اوپنهایمر",
        titleEn = "Oppenheimer",
        rating = "90",
        year = "2023",
        director = "کریستوفر نولان",
        badge = "درام بیوگرافی حماسی",
        desc = "داستان فیزیکدان نظری جی. رابرت اوپنهایمر و رهبری او در پروژه منهتن برای ساخت اولین بمب اتم.",
        oscarAward = "بهترین فیلم و کارگردانی"
    ),
    ImdbMovie(
        id = "tt1065073",
        rank = 4,
        titleFa = "پسرانگی",
        titleEn = "Boyhood",
        rating = "100",
        year = "2014",
        director = "ریچارد لینکلیتر",
        badge = "تجربه ۱۲ ساله فیلم‌سازی",
        desc = "رشد و بزرگ شدن پسری به نام میسون از دوران ابتدایی تا ورود به کالج که در طول ۱۲ سال واقعی فیلم‌برداری شده است."
    ),
    ImdbMovie(
        id = "tt0033467",
        rank = 5,
        titleFa = "همشهری کین",
        titleEn = "Citizen Kane",
        rating = "100",
        year = "1941",
        director = "اورسن ولز",
        badge = "بهترین فیلم تاریخ هنر هفتم",
        desc = "بررسی زندگی چارلز فاستر کین، غول مطبوعاتی که با کلماتی مبهم و پر از رمز و راز از دنیا می‌رود."
    ),
    ImdbMovie(
        id = "tt0111495",
        rank = 6,
        titleFa = "سه رنگ: قرمز",
        titleEn = "Three Colors: Red",
        rating = "100",
        year = "1994",
        director = "کریشتوف کیشلوفسکی",
        badge = "شاهکار درام فلسفی",
        desc = "روایت دوستی غیرمعمول بین یک مدل جوان و یک قاضی بازنشسته بدبین را به تصویر می‌کشد که زندگی اطرافیان را شنود می‌کند."
    )
)

@Composable
fun BestWorldFilmsView(
    vm: FilmViewModel = viewModel(),
    selectedListTab: String,
    onTabChange: (String) -> Unit,
    imdbListState: androidx.compose.foundation.lazy.LazyListState,
    rottenListState: androidx.compose.foundation.lazy.LazyListState,
    metacriticListState: androidx.compose.foundation.lazy.LazyListState
) {
    val state by vm.state.collectAsState()
    val imdbFlowList by vm.imdbList.collectAsState()
    val rottenFlowList by vm.rottenList.collectAsState()
    val metacriticFlowList by vm.metacriticList.collectAsState()

    var selectedMovieDetail by remember { mutableStateOf<ImdbMovie?>(null) }

    val imdbColor = Color(0xFFFFC107)      // IMDb Yellow
    val rottenColor = Color(0xFFFF3333)    // Rotten Tomatoes Red
    val metacriticColor = Color(0xFF3F51B5) // Metacritic Navy Blue

    val movies = when (selectedListTab) {
        "imdb" -> imdbFlowList
        "rotten" -> rottenFlowList
        else -> metacriticFlowList
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(width = 3.5.dp, height = 13.dp)
                    .clip(RoundedCornerShape(30))
                    .background(Green)
            )
            Text(
                text = "برترین‌های سینمای جهان",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = PTitle
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val tabs = listOf(
                Triple("imdb", "برترین‌های IMDb", imdbColor),
                Triple("rotten", "راتن تومیتوز (RT)", rottenColor),
                Triple("metacritic", "متاکریتیک (MC)", metacriticColor)
            )

            tabs.forEach { (id, label, color) ->
                val isSelected = selectedListTab == id
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) color.copy(alpha = 0.2f) else Tile)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) color else Line2,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { onTabChange(id) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) color else Dim,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        val listState = when (selectedListTab) {
            "imdb" -> imdbListState
            "rotten" -> rottenListState
            else -> metacriticListState
        }

        androidx.compose.foundation.lazy.LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(movies) { movie ->
                val fullFilm = state.all.find { it.id == movie.id } ?: state.all.find { it.marquee == movie.titleEn && it.year.toString() == movie.year }
                ImdbMovieRow(
                    movie = movie,
                    fullFilm = fullFilm,
                    accentColor = when (selectedListTab) {
                        "imdb" -> imdbColor
                        "rotten" -> rottenColor
                        else -> metacriticColor
                    },
                    onClick = {
                        if (fullFilm != null) {
                            vm.onSelect(fullFilm)
                        } else {
                            selectedMovieDetail = movie
                        }
                    }
                )
            }
        }
    }

    selectedMovieDetail?.let { movie ->
        val fullFilm = state.all.find { it.id == movie.id } ?: state.all.find { it.marquee == movie.titleEn && it.year.toString() == movie.year }
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { selectedMovieDetail = null },
            containerColor = Surface,
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = movie.titleFa,
                        color = PTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = movie.titleEn,
                        color = Faint,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HorizontalDivider(color = Line, thickness = 1.dp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = when (selectedListTab) {
                                    "imdb" -> imdbColor
                                    "rotten" -> rottenColor
                                    else -> metacriticColor
                                },
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = movie.rating.toFaDigits(),
                                color = when (selectedListTab) {
                                    "imdb" -> imdbColor
                                    "rotten" -> rottenColor
                                    else -> metacriticColor
                                },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (selectedListTab == "rotten") "" else "/۱۰",
                                color = Faint,
                                fontSize = 11.sp
                            )
                        }

                        Text(
                            text = "رتبه #${movie.rank.toFaDigits()}",
                            color = Green,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "کارگردان: ${movie.director}",
                        color = Color.White,
                        fontSize = 13.sp
                    )
                    (if (movie.cast.isNotEmpty()) movie.cast else (fullFilm?.cast?.map { it.n } ?: emptyList())).let { actors ->
                        if (actors.isNotEmpty()) {
                            Text(
                                text = "بازیگران: " + actors.joinToString("، "),
                                color = Color.White,
                                fontSize = 13.sp
                            )
                        }
                    }
                    Text(
                        text = "سال ساخت: ${movie.year.toFaDigits()}",
                        color = Color.White,
                        fontSize = 13.sp
                    )

                    Text(
                        text = movie.desc,
                        color = Dim,
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Justify
                    )

                    if (movie.oscarAward != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFFB300).copy(alpha = 0.12f))
                                .border(1.dp, Color(0xFFFFB300).copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "🏆",
                                    fontSize = 18.sp
                                )
                                Column {
                                    Text(
                                        text = "برنده جایزه اسکار",
                                        color = Color(0xFFFFB300),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "اسکار بهترین ${movie.oscarAward}",
                                        color = Dim,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.End)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Green.copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = movie.badge,
                            color = Green,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = { selectedMovieDetail = null }
                ) {
                    Text("بستن", color = Green, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
fun ImdbMovieRow(
    movie: ImdbMovie,
    fullFilm: com.example.data.Film? = null,
    accentColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Tile)
            .border(1.dp, Line2, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(accentColor.copy(alpha = 0.15f))
                .border(1.dp, accentColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = movie.rank.toFaDigits(),
                color = accentColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = movie.titleFa,
                    color = PTitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                if (movie.oscarAward != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFFFB300).copy(alpha = 0.15f))
                            .border(1.dp, Color(0xFFFFB300).copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "🏆 اسکار",
                            color = Color(0xFFFFB300),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                if (movie.badge.contains("کن") || movie.badge.contains("نخل")) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF4CAF50).copy(alpha = 0.15f))
                            .border(1.dp, Color(0xFF4CAF50).copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "🌿 کن",
                            color = Color(0xFF4CAF50),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Text(
                text = "${movie.titleEn}  ·  ${movie.year.toFaDigits()}",
                color = Faint,
                fontSize = 11.sp
            )
            (if (movie.cast.isNotEmpty()) movie.cast else (fullFilm?.cast?.map { it.n } ?: emptyList())).let { actors ->
                if (actors.isNotEmpty()) {
                    Text(
                        text = "بازیگران: " + actors.take(3).joinToString("، "),
                        color = Dim,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Text(
                text = movie.desc,
                color = Dim,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = movie.rating.toFaDigits(),
                    color = accentColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(accentColor.copy(alpha = 0.1f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = movie.badge.split(" ").firstOrNull() ?: "",
                    color = accentColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

data class DirectorAvatar(
    val id: String,
    val name: String,
    val shortName: String,
    val initials: String,
    val emoji: String,
    val gradientColors: List<Color>
)

val directorAvatars = listOf(
    DirectorAvatar(
        id = "default",
        name = "کاربر فیلم‌بازی",
        shortName = "کاربر",
        initials = "🎬",
        emoji = "🎬",
        gradientColors = listOf(Color(0xFF2196F3), Color(0xFF00BCD4))
    ),
    DirectorAvatar(
        id = "nolan",
        name = "کریستوفر نولان",
        shortName = "نولان",
        initials = "CN",
        emoji = "⏳",
        gradientColors = listOf(Color(0xFF673AB7), Color(0xFF3F51B5))
    ),
    DirectorAvatar(
        id = "tarantino",
        name = "کوینتین تارانتینو",
        shortName = "تارانتینو",
        initials = "QT",
        emoji = "🔫",
        gradientColors = listOf(Color(0xFFE91E63), Color(0xFF9C27B0))
    ),
    DirectorAvatar(
        id = "hitchcock",
        name = "الفرد هیچکاک",
        shortName = "هیچکاک",
        initials = "AH",
        emoji = "🕵️",
        gradientColors = listOf(Color(0xFF607D8B), Color(0xFF455A64))
    ),
    DirectorAvatar(
        id = "scorsese",
        name = "مارتین اسکورسیزی",
        shortName = "اسکورسیزی",
        initials = "MS",
        emoji = "🕶️",
        gradientColors = listOf(Color(0xFFFF9800), Color(0xFFFF5722))
    ),
    DirectorAvatar(
        id = "spielberg",
        name = "استیون اسپیلبرگ",
        shortName = "اسپیلبرگ",
        initials = "SS",
        emoji = "🦖",
        gradientColors = listOf(Color(0xFF4CAF50), Color(0xFF8BC34A))
    ),
    DirectorAvatar(
        id = "kubrick",
        name = "استنلی کوبریک",
        shortName = "کوبریک",
        initials = "SK",
        emoji = "🚀",
        gradientColors = listOf(Color(0xFF9C27B0), Color(0xFF673AB7))
    ),
    DirectorAvatar(
        id = "lynch",
        name = "دیوید لینچ",
        shortName = "لینچ",
        initials = "DL",
        emoji = "☕",
        gradientColors = listOf(Color(0xFF3F51B5), Color(0xFFE91E63))
    )
)

@Composable
fun UserProfileView(
    films: List<com.example.data.Film>,
    watchedIds: Set<String>,
    watchlistIds: Set<String>,
    vm: com.example.ui.FilmViewModel,
    listState: androidx.compose.foundation.lazy.LazyListState = androidx.compose.foundation.lazy.rememberLazyListState(),
    onFilmClick: (com.example.data.Film) -> Unit
) {
    val watchedFilms = films.filter { watchedIds.contains(it.id) }
    val watchlistFilms = films.filter { watchlistIds.contains(it.id) }
    
    val displayName by vm.displayName.collectAsState()
    val themeStyle by vm.themeStyle.collectAsState()
    val selectedAvatarId by vm.selectedAvatar.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current
    
    val currentAvatar = remember(selectedAvatarId) {
        directorAvatars.find { it.id == selectedAvatarId } ?: directorAvatars[0]
    }
    
    var showAccountSettings by rememberSaveable { mutableStateOf(false) }
    var showShareSheet by rememberSaveable { mutableStateOf(false) }
    var showAboutDialog by rememberSaveable { mutableStateOf(false) }
    
    androidx.compose.foundation.lazy.LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(Bg),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Tile)
                    .border(1.dp, Line2, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(currentAvatar.gradientColors))
                        .border(2.dp, Green, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (currentAvatar.id == "default") {
                        Text(
                            text = if (displayName.isNotEmpty()) displayName.take(1).uppercase() else "A",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = currentAvatar.emoji,
                            fontSize = 36.sp
                        )
                    }
                }

                Text(
                    text = displayName,
                    color = PTitle,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                if (currentAvatar.id != "default") {
                    Text(
                        text = "کارگردان محبوب: ${currentAvatar.name}",
                        color = Green,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                } else if (displayName == "کاربر مهمان") {
                    Text(
                        text = "حساب مهمان",
                        color = Faint,
                        fontSize = 13.sp
                    )
                }

                Text(
                    text = "عضویت از تیر ۱۴۰۵".toFaDigits(),
                    color = Dim,
                    fontSize = 11.sp
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf(
                    Triple(watchedFilms.size.toString(), "فیلم دیده‌شده", Green),
                    Triple(watchlistFilms.size.toString(), "لیست تماشا", Orange),
                    Triple((watchedFilms.size + watchlistFilms.size).toString(), "مجموع کل", Blue)
                ).forEach { (count, label, color) ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Tile)
                            .border(1.dp, Line2, RoundedCornerShape(10.dp))
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = count.toFaDigits(),
                            color = color,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = label,
                            color = Dim,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Green, CircleShape)
                    )
                    Text(
                        text = "فیلم‌های دیده‌شده",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = PTitle
                    )
                }

                if (watchedFilms.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .border(1.dp, Line2, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("هنوز فیلمی به لیست دیده‌شده‌ها اضافه نکرده‌اید", color = Faint, fontSize = 12.sp)
                    }
                } else {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        itemsIndexed(watchedFilms) { index, film ->
                            FilmCard(
                                film = film,
                                index = index,
                                onClick = { onFilmClick(film) },
                                modifier = Modifier.width(104.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Orange, CircleShape)
                    )
                    Text(
                        text = "لیست تماشا (بعدا می‌بینم)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = PTitle
                    )
                }

                if (watchlistFilms.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .border(1.dp, Line2, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("هنوز فیلمی به لیست تماشای خود اضافه نکرده‌اید", color = Faint, fontSize = 12.sp)
                    }
                } else {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        itemsIndexed(watchlistFilms) { index, film ->
                            FilmCard(
                                film = film,
                                index = index,
                                onClick = { onFilmClick(film) },
                                modifier = Modifier.width(104.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Tile)
                    .border(1.dp, Line2, RoundedCornerShape(10.dp))
            ) {
                // Section 1: Account Settings
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showAccountSettings = !showAccountSettings }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Dim,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "تنظیمات حساب کاربری",
                            color = Color.White,
                            fontSize = 13.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = if (showAccountSettings) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Faint,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    
                    AnimatedVisibility(visible = showAccountSettings) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Bar)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // 1. Edit Name
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("نام نمایشی", color = Dim, fontSize = 11.sp)
                                var nameError by androidx.compose.runtime.remember { mutableStateOf<String?>(null) }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    var nameInput by androidx.compose.runtime.remember(displayName) { mutableStateOf(displayName) }
                                    val isChanged = remember(nameInput, displayName) {
                                        nameInput.isNotBlank() && nameInput.trim() != displayName.trim()
                                    }
                                    OutlinedTextField(
                                        value = nameInput,
                                        onValueChange = { nameInput = it },
                                        modifier = Modifier.weight(1f),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Green,
                                            unfocusedBorderColor = Line,
                                            focusedTextColor = Color.White,
                                            unfocusedTextColor = Color.White
                                        ),
                                        shape = RoundedCornerShape(8.dp),
                                        singleLine = true
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isChanged) Green else Line.copy(alpha = 0.4f))
                                            .then(
                                                if (isChanged) {
                                                    Modifier.clickable {
                                                        if (vm.setDisplayName(nameInput.trim())) {
                                                            nameError = null
                                                            android.widget.Toast.makeText(context, "نام کاربری با موفقیت تغییر یافت", android.widget.Toast.LENGTH_SHORT).show()
                                                        } else {
                                                            nameError = "این نام مجاز نیست؛ نام دیگری انتخاب کن"
                                                        }
                                                    }
                                                } else Modifier
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "ثبت",
                                            tint = if (isChanged) Bg else Dim,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                if (nameError != null) {
                                    Text(nameError!!, color = Color(0xFFFF6B6B), fontSize = 11.sp)
                                }
                            }
                            
                            HorizontalDivider(color = Line)
                            
                            // 1.5. Avatar selection
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("انتخاب اواتار (کارگردانهای محبوب)", color = Dim, fontSize = 11.sp)
                                androidx.compose.foundation.lazy.LazyRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    contentPadding = PaddingValues(vertical = 4.dp)
                                ) {
                                    items(directorAvatars) { avatar ->
                                        val isSelected = selectedAvatarId == avatar.id
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .clickable {
                                                    if (selectedAvatarId != avatar.id) {
                                                        vm.setAvatarId(avatar.id)
                                                        android.widget.Toast.makeText(context, "اواتار با موفقیت تغییر یافت", android.widget.Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                                .padding(horizontal = 4.dp),
                                            verticalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(56.dp)
                                                    .clip(CircleShape)
                                                    .background(Brush.linearGradient(avatar.gradientColors))
                                                    .border(
                                                        width = if (isSelected) 3.dp else 1.dp,
                                                        color = if (isSelected) Green else Line,
                                                        shape = CircleShape
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                if (avatar.id == "default") {
                                                    Text(
                                                        text = if (displayName.isNotEmpty()) displayName.take(1).uppercase() else "A",
                                                        color = Color.White,
                                                        fontSize = 20.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                } else {
                                                    Text(
                                                        text = avatar.emoji,
                                                        fontSize = 24.sp
                                                    )
                                                }
                                                if (isSelected) {
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .background(Color.Black.copy(alpha = 0.2f))
                                                    )
                                                }
                                            }
                                            Text(
                                                text = avatar.shortName,
                                                color = if (isSelected) Green else Dim,
                                                fontSize = 11.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        }
                                    }
                                }
                            }
                            
                            HorizontalDivider(color = Line)
                            
                            // 2. Theme selection
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("پوسته تاریک", color = Dim, fontSize = 11.sp)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf(
                                        "classic" to "تیره کلاسیک",
                                        "oled" to "مشکی مطلق (OLED)"
                                    ).forEach { (style, label) ->
                                        val isSelected = themeStyle == style
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isSelected) Blue.copy(alpha = 0.15f) else Tile)
                                                .border(
                                                    width = 1.dp,
                                                    color = if (isSelected) Blue else Line,
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .clickable { 
                                                    if (themeStyle != style) {
                                                        vm.setThemeStyle(style)
                                                        android.widget.Toast.makeText(context, "پوسته برنامه با موفقیت تغییر یافت", android.widget.Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                                .padding(vertical = 10.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = label,
                                                color = if (isSelected) Blue else Dim,
                                                fontSize = 12.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                HorizontalDivider(color = Line, thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
                
                // Section 2: Share Watched Films
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showShareSheet = !showShareSheet }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = Dim,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "اشتراک‌گذاری فیلم‌های دیده‌شده",
                            color = Color.White,
                            fontSize = 13.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = if (showShareSheet) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Faint,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    
                    AnimatedVisibility(visible = showShareSheet) {
                                        val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
                        
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Bar)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "خلاصه‌ای از فیلم‌های تماشا شده شما جهت به اشتراک گذاشتن با دیگران:",
                                color = Dim,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                            
                            val shareText = buildString {
                                appendLine("🎥 کارنامه فیلم‌بازی من:")
                                appendLine("من تا کنون در اپلیکیشن فیلم‌بازی ${watchedFilms.size.toString().toFaDigits()} فیلم را تماشا کرده‌ام:")
                                if (watchedFilms.isEmpty()) {
                                    appendLine("هنوز فیلمی ثبت نکرده‌ام. فیلم‌بازی بهترین جا برای آرشیو فیلم‌های موردعلاقه شماست!")
                                } else {
                                    watchedFilms.take(10).forEachIndexed { i, film ->
                                        appendLine("${(i + 1).toString().toFaDigits()}. ${film.fa} (${film.year.toString().toFaDigits()})")
                                    }
                                    if (watchedFilms.size > 10) {
                                        appendLine("و ${((watchedFilms.size - 10)).toString().toFaDigits()} فیلم دیگر...")
                                    }
                                }
                                appendLine("\nشما هم فیلم‌بازی را نصب کنید و لیست تماشای خودتان را بسازید!")
                            }
                            
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Tile)
                                    .border(1.dp, Line, RoundedCornerShape(8.dp))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = shareText,
                                    color = Faint,
                                    fontSize = 11.sp,
                                    lineHeight = 18.sp,
                                    maxLines = 8,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Green)
                                        .clickable {
                                            clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(shareText))
                                            Toast.makeText(context, "متن کپی شد!", Toast.LENGTH_SHORT).show()
                                        }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "کپی متن در حافظه",
                                        color = Bg,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Blue)
                                        .clickable {
                                            val intent = Intent(Intent.ACTION_SEND).apply {
                                                type = "text/plain"
                                                putExtra(Intent.EXTRA_TEXT, shareText)
                                            }
                                            context.startActivity(Intent.createChooser(intent, "اشتراک‌گذاری از طریق"))
                                        }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "اشتراک‌گذاری مستقیم",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
                
                HorizontalDivider(color = Line, thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
                
                // Section 3: About Filmbazi
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showAboutDialog = !showAboutDialog }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = Dim,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "درباره فیلم‌بازی",
                            color = Color.White,
                            fontSize = 13.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = if (showAboutDialog) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Faint,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    
                    AnimatedVisibility(visible = showAboutDialog) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Bar)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            androidx.compose.foundation.Image(
                                painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.app_logo_main_1783193355369),
                                contentDescription = "App Logo",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(1.5.dp, Green.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            )
                            
                            Text(
                                text = "فیلم‌بازی",
                                color = PTitle,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Text(
                                text = "نسخه ۲.۴.۰ (آفلاین و سریع)".toFaDigits(),
                                color = Green,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Text(
                                text = "اپلیکیشن تخصصی آرشیو و رده‌بندی فیلم‌های برتر سینمای جهان بر اساس معتبرترین مراجع بین‌المللی نظیر IMDb، Rotten Tomatoes و Metacritic.",
                                color = Faint,
                                fontSize = 11.sp,
                                lineHeight = 18.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.data.Film
import com.example.ui.theme.Bg
import com.example.ui.theme.Dim
import com.example.ui.theme.Faint
import com.example.ui.theme.Green
import com.example.ui.theme.Orange
import com.example.ui.theme.Line
import com.example.ui.theme.Line2
import com.example.ui.theme.Surface
import com.example.ui.theme.Tile

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilmDetail(
    film: Film,
    watchedIds: Set<String>,
    watchlistIds: Set<String>,
    onToggleWatched: () -> Unit,
    onToggleWatchlist: () -> Unit,
    onBack: () -> Unit
) {
    // Standard back handler to handle hardware back clicks
    BackHandler(onBack = onBack)

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .testTag("film_detail_screen")
    ) {
        // 1. Backdrop Gradient Layer (200dp: line2 -> #222B35 -> bg)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Line2,
                            Color(0xFF222B35),
                            Bg
                        )
                    )
                )
        )

        // 2. Scrollable Detail Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(scrollState)
                .padding(bottom = 32.dp)
        ) {
            // Spacer to let the backdrop shine, taking into account status bar padding
            Spacer(
                modifier = Modifier
                    .statusBarsPadding()
                    .height(110.dp)
            )

            // Header Section: Poster + Movie Details
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                // Movie Poster (104dp width x 156dp height -> exact 2:3 ratio)
                Box(
                    modifier = Modifier
                        .size(width = 104.dp, height = 156.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Tile)
                        .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                ) {
                    if (film.posterUrl != null && film.posterUrl.isNotEmpty()) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            SubcomposeAsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(film.posterUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = film.fa,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                                loading = { PosterPlaceholder(film, modifier = Modifier.fillMaxSize()) },
                                error = { PosterPlaceholder(film, modifier = Modifier.fillMaxSize()) }
                            )
                            // Elegant bottom gradient overlay for the detail poster
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f))
                                        )
                                    )
                            )
                        }
                    } else {
                        PosterPlaceholder(film, modifier = Modifier.fillMaxSize())
                    }
                }

                // Details Column
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(156.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        // Persian Title (22sp bold)
                        Text(
                            text = film.fa,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 28.sp
                        )

                        // Native Title · Year
                        val subtitleText = buildString {
                            if (film.native.isNotEmpty()) {
                                append(film.native)
                                append("  ·  ")
                            }
                            append(film.year.toFaDigits())
                        }
                        Text(
                            text = subtitleText,
                            fontSize = 12.sp,
                            color = Faint,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        // Director: "کارگردانی: ..."
                        if (film.director.isNotEmpty()) {
                            Text(
                                text = "کارگردانی: ${film.director.joinToString("، ")}",
                                fontSize = 13.sp,
                                color = Dim,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Rating Row: Stars + Rating/2 (with one decimal place)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Stars(rating10 = film.rating)
                        val halfRating = film.rating / 2.0
                        Text(
                            text = String.format("(%.1f)", halfRating).toFaDigits(),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Green
                        )
                    }
                }
            }

            // Interactive Buttons (Watched / Watchlist)
            val isWatched = watchedIds.contains(film.id)
            val isWatchlist = watchlistIds.contains(film.id)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Watched button ("دیدمش")
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isWatched) Green.copy(alpha = 0.15f) else Tile)
                        .border(
                            width = 1.dp,
                            color = if (isWatched) Green else Line,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onToggleWatched() }
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = if (isWatched) Green else Dim,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isWatched) "دیده‌ام" else "دیدمش",
                        color = if (isWatched) Green else Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Watchlist button ("جالبه، می‌بینمش")
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isWatchlist) Orange.copy(alpha = 0.15f) else Tile)
                        .border(
                            width = 1.dp,
                            color = if (isWatchlist) Orange else Line,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onToggleWatchlist() }
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (isWatchlist) Orange else Dim,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isWatchlist) "در لیست تماشا" else "جالبه، می‌بینمش",
                        color = if (isWatchlist) Orange else Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 3. Genres section (Chips)
            if (film.genres.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    film.genres.forEach { genre ->
                        Box(
                            modifier = Modifier
                                .background(Surface, RoundedCornerShape(100.dp))
                                .border(1.dp, Line, RoundedCornerShape(100.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = genre,
                                color = Dim,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Separator line
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Line,
                thickness = 1.dp
            )

            // 4. Lang & Country block
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "زبان: ${getLanguageFa(film.lang)}",
                    fontSize = 13.sp,
                    color = Dim
                )
                Text(
                    text = "کشور: ${getCountryFa(film.country)}",
                    fontSize = 13.sp,
                    color = Dim
                )
                if (film.runtime > 0) {
                    Text(
                        text = "مدت زمان: ${film.runtime.toFaDigits()} دقیقه",
                        fontSize = 13.sp,
                        color = Dim
                    )
                }
            }

            // 5. Overview / Synopsis (Phase 3)
            if (film.overview != null && film.overview.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Green, CircleShape)
                        )
                        Text(
                            text = "خلاصه داستان",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = film.overview,
                        fontSize = 14.sp,
                        color = Dim,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Justify
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            // 6. Cast Members Section
            if (film.cast.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    // "بازیگران" Header with a green indicator circle
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Green, CircleShape)
                        )
                        Text(
                            text = "بازیگران",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Cast List Cards
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        film.cast.forEach { castMember ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Surface, RoundedCornerShape(6.dp))
                                    .border(1.dp, Line, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Actor Name
                                Text(
                                    text = castMember.n,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )

                                // Character Name (if provided)
                                if (castMember.c != null && castMember.c.isNotEmpty()) {
                                    Text(
                                        text = castMember.c,
                                        fontSize = 12.sp,
                                        color = Faint,
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 7. Float Action Back Button (34dp Circle Shape, labeled "‹")
        Box(
            modifier = Modifier
                .statusBarsPadding()
                .padding(16.dp)
                .size(34.dp)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape)
                .clip(CircleShape)
                .clickable { onBack() }
                .testTag("detail_back_button"),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "‹",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 2.dp) // Stylize and center slightly
            )
        }
    }
}

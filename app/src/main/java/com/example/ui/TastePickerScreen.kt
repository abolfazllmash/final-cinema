package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.ui.theme.*

@Composable
fun TastePickerScreen(
    films: List<Film>,
    onFinish: (Set<String>) -> Unit
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedIds by remember { mutableStateOf(emptySet<String>()) }

    val filteredFilms = remember(searchQuery, films) {
        if (searchQuery.isBlank()) {
            films.take(18) // Show top popular films first as seeds
        } else {
            val q = searchQuery.trim().lowercase()
            films.filter { film ->
                film.fa.lowercase().contains(q) ||
                film.marquee.lowercase().contains(q) ||
                film.director.any { it.lowercase().contains(q) }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("taste_picker_screen")
    ) {
        // Top action bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "انتخاب سلیقه فیلم",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PTitle
            )

            // Skip button
            TextButton(
                onClick = { onFinish(emptySet()) },
                colors = ButtonDefaults.textButtonColors(contentColor = Dim),
                modifier = Modifier.testTag("taste_picker_skip_button")
            ) {
                Text(
                    text = "رد کردن",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        HorizontalDivider(color = Line, thickness = 1.dp)

        // Description / Guide
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "۳ فیلمی که بیشتر دوست داری رو انتخاب کن",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "با انتخاب فیلم‌های محبوبت، پیشنهادهای شخصی‌سازی شده دریافت می‌کنی.",
                fontSize = 12.sp,
                color = Dim,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }

        // Search Bar (using spelling "جست‌وجو")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("taste_picker_search_input"),
                placeholder = {
                    Text(
                        text = "جست‌وجوی فیلم، کارگردان...",
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
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
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

        // Selection Progress Indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            val count = selectedIds.size
            Text(
                text = "انتخاب شده: ${count.toFaDigits()} از ۳",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (count == 3) Green else Orange
            )
        }

        // Film Grid
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (filteredFilms.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "فیلمی پیدا نشد!",
                        color = Dim,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 80.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(
                        items = filteredFilms,
                        key = { _, film -> film.id }
                    ) { index, film ->
                        val isSelected = selectedIds.contains(film.id)
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) Green else Color.White.copy(alpha = 0.07f),
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .clickable {
                                    val current = selectedIds.toMutableSet()
                                    if (current.contains(film.id)) {
                                        current.remove(film.id)
                                    } else {
                                        if (current.size < 3) {
                                            current.add(film.id)
                                        }
                                    }
                                    selectedIds = current
                                }
                        ) {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(2f / 3f)
                                ) {
                                    if (film.posterUrl != null && film.posterUrl.isNotEmpty()) {
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
                                    } else {
                                        PosterPlaceholder(film, modifier = Modifier.fillMaxSize())
                                    }

                                    // Green transparent overlay if selected
                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Green.copy(alpha = 0.35f))
                                        )

                                        // Badge selection index
                                        val selectedIndex = selectedIds.toList().indexOf(film.id) + 1
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(6.dp)
                                                .size(20.dp)
                                                .background(Green, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = selectedIndex.toFaDigits(),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = GreenInk
                                            )
                                        }
                                    }
                                }

                                // Title
                                Text(
                                    text = film.fa,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Tile)
                                        .padding(horizontal = 6.dp, vertical = 6.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            // Confirm Button overlaying at bottom center
            if (selectedIds.size == 3) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Bg.copy(alpha = 0.95f), Bg)
                            )
                        )
                        .padding(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    Button(
                        onClick = { onFinish(selectedIds) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("taste_picker_confirm_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Green,
                            contentColor = GreenInk
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "تایید و شخصی‌سازی برنامه",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
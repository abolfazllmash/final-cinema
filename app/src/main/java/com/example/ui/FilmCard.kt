package com.example.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import com.example.ui.theme.Faint
import com.example.ui.theme.PTitle
import com.example.ui.theme.Tile
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PosterPlaceholder(film: Film, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(2f / 3f)
            .clip(RoundedCornerShape(6.dp))
            .background(Tile)
            .border(1.dp, Color.White.copy(alpha = 0.07f), RoundedCornerShape(6.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Beautiful generated custom movie poster background
        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.img_movie_poster_placeholder),
            contentDescription = film.marquee,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        
        // Deep modern gradient overlay from top-black (subtle) to bottom-black (heavier)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.75f)
                        )
                    )
                )
        )

        // Movie label over the stylized poster
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = film.marquee,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                    .padding(vertical = 4.dp, horizontal = 6.dp)
            )
        }
    }
}

@Composable
fun FilmCard(
    film: Film,
    index: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 1. Staggered Entrance Animation
    val animState = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay((index % 12) * 22L)
        animState.animateTo(
            targetValue = 1f,
            animationSpec = tween(260, easing = FastOutSlowInEasing)
        )
    }

    // 2. Press Scaling Animation (without ripple as requested)
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1.0f,
        animationSpec = tween(120),
        label = "film_card_press_scale"
    )

    Column(
        modifier = modifier
            .testTag("film_card_${film.id}")
            .graphicsLayer {
                alpha = animState.value
                translationY = (1f - animState.value) * 14.dp.toPx()
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null, // No ripple
                onClick = onClick
            )
    ) {
        // Poster area (loaded image or placeholder)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .clip(RoundedCornerShape(6.dp))
                .border(1.dp, Color.White.copy(alpha = 0.07f), RoundedCornerShape(6.dp))
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
                // Gradient overlay matching Design HTML: absolute inset-0 bg-gradient-to-t from-black/60 to-transparent
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                            )
                        )
                )
            } else {
                PosterPlaceholder(film, modifier = Modifier.fillMaxSize())
            }

            // Award Badges Overlay
            val (hasOscar, hasCannes) = getFilmAwards(film.id)
            if (hasOscar || hasCannes) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (hasOscar) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Black.copy(alpha = 0.75f))
                                .border(1.dp, Color(0xFFFFB300).copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "🏆 اسکار",
                                color = Color(0xFFFFB300),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    if (hasCannes) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Black.copy(alpha = 0.75f))
                                .border(1.dp, Color(0xFF4CAF50).copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "🌿 کن",
                                color = Color(0xFF4CAF50),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Film Persian Title (2 lines max, ellipsis)
        Text(
            text = film.fa,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 6.dp, start = 2.dp, end = 2.dp)
        )

        // Film Original Title (faint)
        Text(
            text = film.marquee,
            fontSize = 11.sp,
            color = Faint,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 1.dp, start = 2.dp, end = 2.dp)
        )

        // Film Cast / Actor Names
        if (film.cast.isNotEmpty()) {
            Text(
                text = "بازیگران: " + film.cast.take(2).map { it.n }.joinToString("، "),
                fontSize = 10.sp,
                color = Faint,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 1.dp, start = 2.dp, end = 2.dp)
            )
        }

        // Footer: Year (faint, Persian digits) + Stars
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 3.dp, start = 2.dp, end = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = film.year.toFaDigits(),
                fontSize = 11.sp,
                color = Faint
            )
            Stars(rating10 = film.rating)
        }
    }
}

private fun getFilmAwards(filmId: String): Pair<Boolean, Boolean> {
    return when (filmId) {
        "tt0068646" -> Pair(true, false)   // پدرخوانده (برنده اسکار بهترین فیلم)
        "tt0110912" -> Pair(false, true)   // داستان عامه‌پسند (برنده نخل طلای کن)
        "tt0114746" -> Pair(false, true)   // طعم گیلاس (برنده نخل طلای کن)
        "tt0108052" -> Pair(true, false)   // فهرست شیندلر (برنده اسکار بهترین فیلم و کارگردانی)
        "tt0034583" -> Pair(true, false)   // کازابلانکا (برنده اسکار بهترین فیلم و کارگردانی)
        "tt6751668" -> Pair(true, true)    // انگل (برنده اسکار بهترین فیلم و کارگردانی + نخل طلای کن)
        "tt0071562" -> Pair(true, false)   // پدرخوانده: قسمت دوم (برنده اسکار بهترین فیلم و کارگردانی)
        "tt0056172" -> Pair(true, false)   // لورنس عربستان (برنده اسکار بهترین فیلم و کارگردانی)
        "tt15398776" -> Pair(true, false)  // اوپنهایمر (برنده اسکار بهترین فیلم و کارگردانی)
        "tt0167260" -> Pair(true, false)   // ارباب حلقه‌ها: بازگشت پادشاه (برنده اسکار بهترین فیلم و کارگردانی)
        "tt0109830" -> Pair(true, false)   // فارست گامپ (برنده اسکار بهترین فیلم و کارگردانی)
        "tt0073486" -> Pair(true, false)   // دیوانه از قفس پرید (برنده اسکار بهترین فیلم و کارگردانی)
        "tt0102926" -> Pair(true, false)   // سکوت بره‌ها (برنده اسکار بهترین فیلم و کارگردانی)
        "tt0120815" -> Pair(true, false)   // نجات سرباز رایان (برنده اسکار بهترین کارگردانی)
        "tt0172495" -> Pair(true, false)   // گلادیاتور (برنده اسکار بهترین فیلم)
        "tt0407889" -> Pair(true, false)   // رفتگان (برنده اسکار بهترین فیلم و کارگردانی)
        "tt0822854" -> Pair(true, false)   // جایی برای پیرمردها نیست (برنده اسکار بهترین فیلم و کارگردانی)
        "tt3828984" -> Pair(true, false)   // افشاگر (برنده اسکار بهترین فیلم)
        "tt6710474" -> Pair(true, false)   // همه چیز همه جا به یکباره (برنده اسکار بهترین فیلم و کارگردانی)
        "tt10366460" -> Pair(true, false)  // کودا (برنده اسکار بهترین فیلم)
        "tt9770150" -> Pair(true, false)   // سرزمین خانه‌به‌دوش‌ها (برنده اسکار بهترین فیلم و کارگردانی)
        "tt15328130" -> Pair(false, true)  // آناتومی یک سقوط (برنده نخل طلای کن)
        else -> Pair(false, false)
    }
}


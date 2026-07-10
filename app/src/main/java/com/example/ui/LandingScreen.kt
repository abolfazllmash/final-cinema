package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.ui.theme.Bg
import com.example.ui.theme.Blue
import com.example.ui.theme.Dim
import com.example.ui.theme.Faint
import com.example.ui.theme.Green
import com.example.ui.theme.GreenInk
import com.example.ui.theme.Line
import com.example.ui.theme.Line2
import com.example.ui.theme.Orange
import com.example.ui.theme.Surface
import com.example.ui.theme.Tile

@Composable
fun LandingScreen(
    onStartClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .navigationBarsPadding()
            .verticalScroll(scrollState)
            .padding(bottom = 32.dp)
            .testTag("landing_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Hero Gradient Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Line2.copy(alpha = 0.5f),
                            Color(0xFF1B232D),
                            Bg
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                // Large styled app logo
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.app_logo_main_1783193355369),
                    contentDescription = "Film App Logo",
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.5.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Large styled title
                Text(
                    text = "سینمای جهان در دست تو!",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Friendly subtitle
                Text(
                    text = "مرور، جست‌و‌جو، کشف و لیست همه فیلم‌های جهان",
                    fontSize = 14.sp,
                    color = Dim,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }
        }

        // 2. Poster Ribbon (3 actual movie posters side-by-side)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Mock Poster 1
            MockPoster(
                title = "جدایی نادر از سیمین",
                posterUrl = "https://api.cashf-team.xyz/poster/poster.php?imdb=tt1738380&title=A+Separation&year=2011",
                modifier = Modifier.weight(1f)
            )
            // Mock Poster 2 (Featured center)
            MockPoster(
                title = "پدرخوانده",
                posterUrl = "https://api.cashf-team.xyz/poster/poster.php?imdb=tt0068646&title=The+Godfather&year=1972",
                modifier = Modifier.weight(1.1f)
            )
            // Mock Poster 3
            MockPoster(
                title = "تلقین",
                posterUrl = "https://api.cashf-team.xyz/poster/poster.php?imdb=tt1375666&title=Inception&year=2010",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        // 3. Primary Start Button ("شروع کن")
        Button(
            onClick = onStartClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(52.dp)
                .testTag("landing_start_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = Green,
                contentColor = GreenInk
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "شروع کن",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun MockPoster(
    title: String,
    posterUrl: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(2f / 3f)
            .clip(RoundedCornerShape(8.dp))
            .background(Tile)
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(posterUrl)
                .crossfade(true)
                .build(),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF151D26)),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.foundation.Image(
                        painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.img_movie_poster_placeholder),
                        contentDescription = title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                                )
                            )
                    )
                    Text(
                        text = title,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Dim,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            },
            error = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF151D26)),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.foundation.Image(
                        painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.img_movie_poster_placeholder),
                        contentDescription = title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                                )
                            )
                    )
                    Text(
                        text = title,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Dim,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
        )
        // Elegant overlay gradient for the ribbon posters
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.4f))
                    )
                )
        )
    }
}

package com.example.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun Stars(rating10: Double, size: TextUnit = 12.sp) {
    // Ratings are drawn in LTR direction so they fill from left to right consistently
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        val starColor = Color(0xFF00E054)     // Letterboxd green
        val inactiveColor = Color(0xFF2C3440)  // Tile line color
        val starSize = 16.dp
        val totalWidth = starSize * 5
        val fraction = (rating10 / 10.0).coerceIn(0.0, 1.0)

        Box {
            // Inactive stars in background
            Row {
                for (i in 0 until 5) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = inactiveColor,
                        modifier = Modifier.width(starSize)
                    )
                }
            }
            // Active green stars overlay clipped to rating fraction
            Box(
                modifier = Modifier
                    .width(totalWidth * fraction.toFloat())
                    .clipToBounds()
            ) {
                Row(modifier = Modifier.width(totalWidth)) {
                    for (i in 0 until 5) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = starColor,
                            modifier = Modifier.width(starSize)
                        )
                    }
                }
            }
        }
    }
}

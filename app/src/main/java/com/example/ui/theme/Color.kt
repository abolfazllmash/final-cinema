package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalBg = compositionLocalOf { Color(0xFF14181C) }
val LocalBar = compositionLocalOf { Color(0xFF10151A) }
val LocalSurface = compositionLocalOf { Color(0xFF1C2228) }

val Bg: Color
    @Composable
    get() = LocalBg.current

val Bar: Color
    @Composable
    get() = LocalBar.current

val Surface: Color
    @Composable
    get() = LocalSurface.current

val Tile = Color(0xFF2C3440)
val Line = Color(0xFF2C3440)
val Line2 = Color(0xFF384450)
val Ink = Color(0xFFFFFFFF)
val Dim = Color(0xFF99AABB)
val Faint = Color(0xFF667788)
val Green = Color(0xFF00E054)
val Orange = Color(0xFFFF8000)
val Blue = Color(0xFF40BCF4)
val PTitle = Color(0xFFB6C2CF)
val GreenInk = Color(0xFF0B1A10)

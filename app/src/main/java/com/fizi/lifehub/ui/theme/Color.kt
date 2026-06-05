package com.fizi.lifehub.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ─── Modern Palette ───
val Teal500 = Color(0xFF00BFA5)
val Teal700 = Color(0xFF00897B)
val Purple500 = Color(0xFF7C4DFF)
val Purple700 = Color(0xFF651FFF)
val Pink500 = Color(0xFFFF4081)
val Amber500 = Color(0xFFFFB300)
val Blue500 = Color(0xFF448AFF)
val Green500 = Color(0xFF66BB6A)
val Red500 = Color(0xFFEF5350)

// ─── Primary ───
val Primary = Teal500
val OnPrimary = Color(0xFFFFFFFF)
val PrimaryContainer = Color(0xFFB2F5EA)
val OnPrimaryContainer = Color(0xFF00382F)

// ─── Secondary ───
val Secondary = Purple500
val OnSecondary = Color(0xFFFFFFFF)
val SecondaryContainer = Color(0xFFE8DEF8)
val OnSecondaryContainer = Color(0xFF1D192B)

// ─── Tertiary ───
val Tertiary = Amber500
val OnTertiary = Color(0xFFFFFFFF)
val TertiaryContainer = Color(0xFFFFF8E1)
val OnTertiaryContainer = Color(0xFF3E2723)

// ─── Error ───
val Error = Red500
val OnError = Color(0xFFFFFFFF)

// ─── Background ───
val Background = Color(0xFFF0F4F8)
val OnBackground = Color(0xFF1A1C1E)
val Surface = Color(0xFFFFFFFF)
val OnSurface = Color(0xFF1A1C1E)
val SurfaceVariant = Color(0xFFE7E0EC)
val OnSurfaceVariant = Color(0xFF49454F)

// ─── Dark Theme ───
val DarkPrimary = Color(0xFF4DB6AC)
val DarkOnPrimary = Color(0xFF003731)
val DarkPrimaryContainer = Color(0xFF005048)
val DarkOnPrimaryContainer = Color(0xFF70F0E0)
val DarkSecondary = Color(0xFFB388FF)
val DarkOnSecondary = Color(0xFF381E72)
val DarkSecondaryContainer = Color(0xFF4F378B)
val DarkOnSecondaryContainer = Color(0xFFEADDFF)
val DarkTertiary = Color(0xFFFFD54F)
val DarkOnTertiary = Color(0xFF3E2723)
val DarkError = Color(0xFFEF9A9A)
val DarkOnError = Color(0xFF690005)
val DarkBackground = Color(0xFF121218)
val DarkOnBackground = Color(0xFFE6E1E5)
val DarkSurface = Color(0xFF1E1E24)
val DarkOnSurface = Color(0xFFE6E1E5)
val DarkSurfaceVariant = Color(0xFF2C2C34)
val DarkOnSurfaceVariant = Color(0xFFCAC4D0)

// ─── Functional Colors ───
val IncomeColor = Green500
val ExpenseColor = Red500
val HabitGreen = Green500
val HabitStreak = Amber500

// ─── Gradient Brushes ───
val GradientPrimary = Brush.linearGradient(listOf(Teal500, Teal700))
val GradientPurple = Brush.linearGradient(listOf(Purple500, Purple700))
val GradientSunset = Brush.linearGradient(listOf(Pink500, Amber500))
val GradientOcean = Brush.linearGradient(listOf(Blue500, Teal500))
val GradientForest = Brush.linearGradient(listOf(Green500, Teal500))
val GradientCard = Brush.linearGradient(
    listOf(
        Color.White.copy(alpha = 0.6f),
        Color.White.copy(alpha = 0.2f)
    )
)

// ─── Note Colors (vibrant) ───
val noteColors = listOf(
    Color(0xFFFFF9C4), // Yellow
    Color(0xFFC8E6C9), // Green
    Color(0xFFBBDEFB), // Blue
    Color(0xFFFFCDD2), // Pink
    Color(0xFFD1C4E9), // Purple
    Color(0xFFFFE0B2), // Orange
    Color(0xFFB2DFDB), // Teal
    Color(0xFFF5F5F5)  // Grey
)

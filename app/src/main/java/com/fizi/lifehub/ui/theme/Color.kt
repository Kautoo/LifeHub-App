package com.fizi.lifehub.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ═══════════════════════════════════════════
// LifeHub "Deep Cosmic" Design System (from Stitch)
// ═══════════════════════════════════════════

// ─── Primary (Purple-Blue) ───
val Primary = Color(0xFFC4C0FF)
val OnPrimary = Color(0xFF2000A4)
val PrimaryContainer = Color(0xFF8781FF)
val OnPrimaryContainer = Color(0xFF1B0091)

// ─── Secondary (Cyan) ───
val Secondary = Color(0xFFA5E7FF)
val OnSecondary = Color(0xFF003543)
val SecondaryContainer = Color(0xFF00D2FF)
val OnSecondaryContainer = Color(0xFF00566A)

// ─── Tertiary (Coral) ───
val Tertiary = Color(0xFFFFB3B0)
val OnTertiary = Color(0xFF68000F)
val TertiaryContainer = Color(0xFFF16161)
val OnTertiaryContainer = Color(0xFF5C000C)

// ─── Error ───
val Error = Color(0xFFFFB4AB)
val OnError = Color(0xFF690005)
val ErrorContainer = Color(0xFF93000A)
val OnErrorContainer = Color(0xFFFFDAD6)

// ─── Background & Surface (Deep Cosmic Dark) ───
val Background = Color(0xFF10141A)
val OnBackground = Color(0xFFDFE2EB)
val Surface = Color(0xFF10141A)
val OnSurface = Color(0xFFDFE2EB)
val SurfaceDim = Color(0xFF10141A)
val SurfaceBright = Color(0xFF353940)
val SurfaceContainerLowest = Color(0xFF0A0E14)
val SurfaceContainerLow = Color(0xFF181C22)
val SurfaceContainer = Color(0xFF1C2026)
val SurfaceContainerHigh = Color(0xFF262A31)
val SurfaceContainerHighest = Color(0xFF31353C)
val SurfaceVariant = Color(0xFF31353C)
val OnSurfaceVariant = Color(0xFFC7C4D8)

// ─── Outline ───
val Outline = Color(0xFF918FA1)
val OutlineVariant = Color(0xFF464555)

// ─── Inverse ───
val InverseSurface = Color(0xFFDFE2EB)
val InverseOnSurface = Color(0xFF2D3137)
val InversePrimary = Color(0xFF4F44E2)

// ─── Functional Colors ───
val IncomeColor = Color(0xFF4ADE80)   // emerald-400
val ExpenseColor = Color(0xFFFB7185)  // rose-400
val HabitGreen = Color(0xFF66BB6A)
val HabitStreak = Color(0xFFFFB300)
val WaterBlue = Color(0xFF00D2FF)
val SuccessGreen = Color(0xFF4CAF50)

// ─── Gradient Brushes ───
val GradientPrimary = Brush.linearGradient(listOf(PrimaryContainer, SecondaryContainer))
val GradientPurple = Brush.linearGradient(listOf(Color(0xFF8781FF), Color(0xFF4F44E2)))
val GradientCyan = Brush.linearGradient(listOf(Color(0xFF00D2FF), Color(0xFFA5E7FF)))
val GradientSunset = Brush.linearGradient(listOf(TertiaryContainer, Color(0xFFFFB3B0)))
val GradientCard = Brush.linearGradient(
    listOf(
        PrimaryContainer.copy(alpha = 0.2f),
        SecondaryContainer.copy(alpha = 0.05f)
    )
)
val GradientFab = Brush.linearGradient(listOf(Color(0xFF00D2FF), Color(0xFF8781FF)))
val GradientOcean = Brush.linearGradient(listOf(Color(0xFF448AFF), Color(0xFF00D2FF)))
val GradientForest = Brush.linearGradient(listOf(Color(0xFF00897B), Color(0xFF00695C)))

// ─── Note Colors (dark theme friendly) ───
val noteColors = listOf(
    Color(0xFF8781FF), // Purple
    Color(0xFF00D2FF), // Cyan
    Color(0xFFF16161), // Red
    Color(0xFFFFB3B0), // Coral
    Color(0xFF4ADE80), // Green
    Color(0xFFFFB300), // Amber
    Color(0xFFA5E7FF), // Light Blue
    Color(0xFF918FA1), // Grey
)

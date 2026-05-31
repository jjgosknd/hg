package com.panfil.carlog.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Современная палитра CarLog: глубокий индиго + электрический циан + янтарный
 * акцент. Светлая тема стремится к "slate / ice" фону, тёмная — к насыщенному
 * сине-чёрному без угольной серости.
 */

// Brand
val BrandIndigo = Color(0xFF4F46E5)       // primary
val BrandIndigoDeep = Color(0xFF312E81)
val BrandIndigoSoft = Color(0xFF818CF8)
val BrandViolet = Color(0xFF7C3AED)
val BrandCyan = Color(0xFF06B6D4)         // secondary
val BrandCyanSoft = Color(0xFF22D3EE)
val BrandAmber = Color(0xFFF59E0B)        // tertiary
val BrandRose = Color(0xFFEF4444)
val BrandEmerald = Color(0xFF10B981)

private val LightColors = lightColorScheme(
    primary = BrandIndigo,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0E7FF),
    onPrimaryContainer = Color(0xFF1E1B4B),

    secondary = BrandCyan,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCFFAFE),
    onSecondaryContainer = Color(0xFF083344),

    tertiary = BrandAmber,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFEF3C7),
    onTertiaryContainer = Color(0xFF451A03),

    background = Color(0xFFF6F7FB),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFEEF2F7),
    onSurfaceVariant = Color(0xFF475569),
    surfaceTint = BrandIndigo,
    inverseSurface = Color(0xFF1E293B),
    inverseOnSurface = Color(0xFFF8FAFC),

    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0),

    error = Color(0xFFDC2626),
    onError = Color.White,
    errorContainer = Color(0xFFFEE2E2),
    onErrorContainer = Color(0xFF7F1D1D),

    scrim = Color(0xCC0F172A),
)

private val DarkColors = darkColorScheme(
    primary = BrandIndigoSoft,
    onPrimary = Color(0xFF1E1B4B),
    primaryContainer = BrandIndigoDeep,
    onPrimaryContainer = Color(0xFFE0E7FF),

    secondary = BrandCyanSoft,
    onSecondary = Color(0xFF083344),
    secondaryContainer = Color(0xFF155E75),
    onSecondaryContainer = Color(0xFFCFFAFE),

    tertiary = Color(0xFFFBBF24),
    onTertiary = Color(0xFF451A03),
    tertiaryContainer = Color(0xFF92400E),
    onTertiaryContainer = Color(0xFFFEF3C7),

    background = Color(0xFF0B0F1A),
    onBackground = Color(0xFFE2E8F0),
    surface = Color(0xFF111827),
    onSurface = Color(0xFFE2E8F0),
    surfaceVariant = Color(0xFF1E293B),
    onSurfaceVariant = Color(0xFFCBD5E1),
    surfaceTint = BrandIndigoSoft,
    inverseSurface = Color(0xFFE2E8F0),
    inverseOnSurface = Color(0xFF1E293B),

    outline = Color(0xFF475569),
    outlineVariant = Color(0xFF334155),

    error = Color(0xFFF87171),
    onError = Color(0xFF7F1D1D),
    errorContainer = Color(0xFF7F1D1D),
    onErrorContainer = Color(0xFFFEE2E2),

    scrim = Color(0xEE000000),
)

@Composable
fun CarLogTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(colorScheme = colorScheme, content = content)
}

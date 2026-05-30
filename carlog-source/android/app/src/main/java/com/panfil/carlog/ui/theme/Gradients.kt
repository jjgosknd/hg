package com.panfil.carlog.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Готовые градиенты, чтобы экраны выглядели единообразно "дорого".
 * Все построены на текущей теме, поэтому работают и в светлой, и в тёмной.
 */
object AppGradients {

    /** Главный "брендовый" градиент: индиго -> фиолет -> циан. */
    val Brand: Brush
        @Composable @ReadOnlyComposable get() = Brush.linearGradient(
            0f to BrandIndigo,
            0.55f to BrandViolet,
            1f to BrandCyan,
        )

    /** Мягкий вертикальный фон для героя: primary -> tertiary. */
    val Hero: Brush
        @Composable @ReadOnlyComposable get() = Brush.linearGradient(
            0f to MaterialTheme.colorScheme.primary,
            1f to MaterialTheme.colorScheme.tertiary.copy(alpha = 0.85f),
        )

    /** Тёплый акцент для секций "расходы". */
    val Warm: Brush
        @Composable @ReadOnlyComposable get() = Brush.linearGradient(
            0f to BrandAmber,
            1f to BrandRose,
        )

    /** Прохладный для секций "ТО / прогноз". */
    val Cool: Brush
        @Composable @ReadOnlyComposable get() = Brush.linearGradient(
            0f to BrandCyan,
            1f to BrandIndigo,
        )

    /** Очень мягкий tinted-фон под секциями. */
    val SoftSurface: Brush
        @Composable @ReadOnlyComposable get() = Brush.verticalGradient(
            0f to MaterialTheme.colorScheme.primary.copy(alpha = 0.06f),
            1f to MaterialTheme.colorScheme.surface,
        )

    fun statusGradient(start: Color, end: Color): Brush =
        Brush.linearGradient(0f to start, 1f to end)
}

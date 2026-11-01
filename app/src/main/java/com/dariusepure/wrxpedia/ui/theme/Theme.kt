package com.dariusepure.wrxpedia.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = WrxBluePrimary,
    onPrimary = Color.White,
    primaryContainer = WrxBlueContainerDark,
    onPrimaryContainer = Color(0xFFDBEAFE),
    secondary = StiPinkSecondary,
    onSecondary = Color.White,
    secondaryContainer = StiPinkContainerDark,
    onSecondaryContainer = Color(0xFFFFE4E6),
    tertiary = RallyGoldTertiary,
    onTertiary = Color.Black,
    tertiaryContainer = RallyGoldContainerDark,
    onTertiaryContainer = Color(0xFFFEF3C7),
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceContainer = DarkSurfaceContainer,
    surfaceContainerHigh = DarkSurfaceContainerHigh,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant
)

private val LightColorScheme = lightColorScheme(
    primary = WrxBluePrimaryLight,
    onPrimary = Color.White,
    primaryContainer = WrxBlueContainerLight,
    onPrimaryContainer = Color(0xFF1E3A8A),
    secondary = StiPinkSecondary,
    onSecondary = Color.White,
    secondaryContainer = StiPinkContainerLight,
    onSecondaryContainer = Color(0xFF881337),
    tertiary = RallyGoldTertiary,
    onTertiary = Color.White,
    tertiaryContainer = RallyGoldContainerLight,
    onTertiaryContainer = Color(0xFF78350F),
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceContainer = LightSurfaceContainer,
    surfaceContainerHigh = LightSurfaceContainerHigh,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant
)

@Composable
fun WRXpediaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set to false so Subaru Rally Motorsport theme is guaranteed
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
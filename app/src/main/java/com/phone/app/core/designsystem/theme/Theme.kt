package com.phone.app.core.designsystem.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
// DarkColors and LightColors are defined in this package (Color.kt)
import androidx.compose.ui.unit.dp

val DarkColorScheme = darkColorScheme(
    primary = DarkColors.primary,
    onPrimary = DarkColors.onPrimary,
    primaryContainer = DarkColors.primaryContainer,
    onPrimaryContainer = DarkColors.onPrimaryContainer,
    secondary = DarkColors.secondary,
    onSecondary = DarkColors.onSecondary,
    secondaryContainer = DarkColors.secondaryContainer,
    onSecondaryContainer = DarkColors.onSecondaryContainer,
    tertiary = DarkColors.tertiary,
    onTertiary = DarkColors.onTertiary,
    tertiaryContainer = DarkColors.tertiaryContainer,
    onTertiaryContainer = DarkColors.onTertiaryContainer,
    error = DarkColors.error,
    onError = DarkColors.onError,
    errorContainer = DarkColors.errorContainer,
    onErrorContainer = DarkColors.onErrorContainer,
    background = DarkColors.background,
    onBackground = DarkColors.onBackground,
    surface = DarkColors.surface,
    onSurface = DarkColors.onSurface,
    surfaceVariant = DarkColors.surfaceVariant,
    onSurfaceVariant = DarkColors.onSurfaceVariant,
    outline = DarkColors.outline,
    outlineVariant = DarkColors.outlineVariant,
    inverseSurface = DarkColors.inverseSurface,
    inverseOnSurface = DarkColors.inverseOnSurface,
    inversePrimary = DarkColors.inversePrimary
)

val LightColorScheme = lightColorScheme(
    primary = LightColors.primary,
    onPrimary = LightColors.onPrimary,
    primaryContainer = LightColors.primaryContainer,
    onPrimaryContainer = LightColors.onPrimaryContainer,
    secondary = LightColors.secondary,
    onSecondary = LightColors.onSecondary,
    secondaryContainer = LightColors.secondaryContainer,
    onSecondaryContainer = LightColors.onSecondaryContainer,
    tertiary = LightColors.tertiary,
    onTertiary = LightColors.onTertiary,
    tertiaryContainer = LightColors.tertiaryContainer,
    onTertiaryContainer = LightColors.onTertiaryContainer,
    error = LightColors.error,
    onError = LightColors.onError,
    errorContainer = LightColors.errorContainer,
    onErrorContainer = LightColors.onErrorContainer,
    background = LightColors.background,
    onBackground = LightColors.onBackground,
    surface = LightColors.surface,
    onSurface = LightColors.onSurface,
    surfaceVariant = LightColors.surfaceVariant,
    onSurfaceVariant = LightColors.onSurfaceVariant,
    outline = LightColors.outline,
    outlineVariant = LightColors.outlineVariant,
    inverseSurface = LightColors.inverseSurface,
    inverseOnSurface = LightColors.inverseOnSurface,
    inversePrimary = LightColors.inversePrimary
)

data class PhoneThemeData(
    val colorScheme: androidx.compose.material3.ColorScheme,
    val typography: androidx.compose.material3.Typography,
    val shapes: PhoneShapes,
    val isDark: Boolean,
    val animationEnabled: Boolean,
    val reducedMotion: Boolean,
    val hapticFeedback: Boolean,
    val layoutDensity: LayoutDensity
)

enum class LayoutDensity {
    Compact,
    Comfortable,
    Spacious
}

data class PhoneShapes(
    val extraSmall: androidx.compose.foundation.shape.CornerBasedShape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
    val small: androidx.compose.foundation.shape.CornerBasedShape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    val medium: androidx.compose.foundation.shape.CornerBasedShape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
    val large: androidx.compose.foundation.shape.CornerBasedShape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
    val xLarge: androidx.compose.foundation.shape.CornerBasedShape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
    val xXLarge: androidx.compose.foundation.shape.CornerBasedShape = androidx.compose.foundation.shape.RoundedCornerShape(32.dp),
    val pill: androidx.compose.foundation.shape.CornerBasedShape = androidx.compose.foundation.shape.RoundedCornerShape(100.dp),
    val dialpadButton: androidx.compose.foundation.shape.CornerBasedShape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
    val contactCard: androidx.compose.foundation.shape.CornerBasedShape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
    val button: androidx.compose.foundation.shape.CornerBasedShape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
    val fab: androidx.compose.foundation.shape.CornerBasedShape = androidx.compose.foundation.shape.RoundedCornerShape(32.dp),
    val surface: androidx.compose.foundation.shape.CornerBasedShape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
    val avatarCircle: androidx.compose.foundation.shape.CornerBasedShape = androidx.compose.foundation.shape.CircleShape,
    val avatarRounded: androidx.compose.foundation.shape.CornerBasedShape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
    val avatarSoft: androidx.compose.foundation.shape.CornerBasedShape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
)

val LocalPhoneTheme = staticCompositionLocalOf {
    PhoneThemeData(
        colorScheme = LightColorScheme,
        typography = androidx.compose.material3.Typography(),
        shapes = PhoneShapes(),
        isDark = false,
        animationEnabled = true,
        reducedMotion = false,
        hapticFeedback = true,
        layoutDensity = LayoutDensity.Comfortable
    )
}

@Composable
fun PhoneTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    customColorScheme: androidx.compose.material3.ColorScheme? = null,
    typography: androidx.compose.material3.Typography = androidx.compose.material3.Typography(),
    shapes: PhoneShapes = PhoneShapes(),
    animationEnabled: Boolean = true,
    reducedMotion: Boolean = false,
    hapticFeedback: Boolean = true,
    layoutDensity: LayoutDensity = LayoutDensity.Comfortable,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        customColorScheme != null -> customColorScheme
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    val phoneThemeData = PhoneThemeData(
        colorScheme = colorScheme,
        typography = typography,
        shapes = shapes,
        isDark = darkTheme,
        animationEnabled = animationEnabled,
        reducedMotion = reducedMotion,
        hapticFeedback = hapticFeedback,
        layoutDensity = layoutDensity
    )

    CompositionLocalProvider(LocalPhoneTheme provides phoneThemeData) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            shapes = androidx.compose.material3.Shapes(
                extraSmall = shapes.extraSmall,
                small = shapes.small,
                medium = shapes.medium,
                large = shapes.large,
                extraLarge = shapes.xLarge
            ),
            content = content
        )
    }
}


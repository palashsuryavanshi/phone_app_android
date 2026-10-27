package com.phone.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

enum class ThemeMode(val title: String) {
    DARK("Dark Identity"),
    LIGHT("Light Contrast"),
    AMOLED("Deep AMOLED"),
    SYSTEM("Follow System")
}

enum class AccentPalette(val label: String, val primary: Color, val secondary: Color) {
    VIOLET("Electric Violet", Color(0xFF7565FF), Color(0xFF20D6CB)),
    CYAN("Arctic Cyan", Color(0xFF20D6CB), Color(0xFF7565FF)),
    MAGENTA("Radiant Magenta", Color(0xFFEF63C8), Color(0xFF7565FF)),
    AMBER("Sunset Ember", Color(0xFFFF996B), Color(0xFFEF63C8)),
    EMERALD("Emerald Mist", Color(0xFF39D9A5), Color(0xFF20D6CB))
}

@Immutable
data class PhoneSemanticColors(
    val background: Color,
    val surface: Color,
    val surfaceSecondary: Color,
    val surfaceTertiary: Color,
    val primary: Color,
    val primaryPressed: Color,
    val primarySoft: Color,
    val secondary: Color,
    val tertiary: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val outline: Color,
    val success: Color,
    val warning: Color,
    val error: Color,
    val incomingCall: Color,
    val endCall: Color
)

val LocalPhoneColors = staticCompositionLocalOf {
    PhoneSemanticColors(
        background = PhoneDarkBackground,
        surface = PhoneDarkSurface,
        surfaceSecondary = PhoneDarkSurfaceSecondary,
        surfaceTertiary = PhoneDarkSurfaceTertiary,
        primary = PhoneDarkPrimary,
        primaryPressed = PhoneDarkPrimaryPressed,
        primarySoft = PhoneDarkPrimarySoft,
        secondary = PhoneDarkSecondary,
        tertiary = PhoneDarkTertiary,
        textPrimary = PhoneDarkTextPrimary,
        textSecondary = PhoneDarkTextSecondary,
        textMuted = PhoneDarkTextMuted,
        outline = PhoneDarkOutline,
        success = PhoneDarkSuccess,
        warning = PhoneDarkWarning,
        error = PhoneDarkError,
        incomingCall = PhoneIncomingCall,
        endCall = PhoneEndCall
    )
}

object PhoneTheme {
    val colors: PhoneSemanticColors
        @Composable
        get() = LocalPhoneColors.current
}

@Composable
fun PhonePhoneTheme(
    themeMode: ThemeMode = ThemeMode.DARK,
    accent: AccentPalette = AccentPalette.VIOLET,
    shapePreset: ShapePreset = ShapePreset.EXPRESSIVE,
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val effectiveDark = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.AMOLED -> true
        ThemeMode.SYSTEM -> isSystemDark
    }
    val isAmoled = themeMode == ThemeMode.AMOLED

    val PhoneSemanticColors = when {
        isAmoled -> PhoneSemanticColors(
            background = PhoneAmoledBackground,
            surface = PhoneAmoledSurface,
            surfaceSecondary = PhoneAmoledElevatedSurface,
            surfaceTertiary = PhoneAmoledSurfaceTertiary,
            primary = accent.primary,
            primaryPressed = PhoneDarkPrimaryPressed,
            primarySoft = PhoneDarkPrimarySoft,
            secondary = accent.secondary,
            tertiary = PhoneDarkTertiary,
            textPrimary = PhoneAmoledTextPrimary,
            textSecondary = PhoneAmoledTextSecondary,
            textMuted = PhoneAmoledTextMuted,
            outline = PhoneAmoledOutline,
            success = PhoneDarkSuccess,
            warning = PhoneDarkWarning,
            error = PhoneDarkError,
            incomingCall = PhoneIncomingCall,
            endCall = PhoneEndCall
        )
        effectiveDark -> PhoneSemanticColors(
            background = PhoneDarkBackground,
            surface = PhoneDarkSurface,
            surfaceSecondary = PhoneDarkSurfaceSecondary,
            surfaceTertiary = PhoneDarkSurfaceTertiary,
            primary = accent.primary,
            primaryPressed = PhoneDarkPrimaryPressed,
            primarySoft = PhoneDarkPrimarySoft,
            secondary = accent.secondary,
            tertiary = PhoneDarkTertiary,
            textPrimary = PhoneDarkTextPrimary,
            textSecondary = PhoneDarkTextSecondary,
            textMuted = PhoneDarkTextMuted,
            outline = PhoneDarkOutline,
            success = PhoneDarkSuccess,
            warning = PhoneDarkWarning,
            error = PhoneDarkError,
            incomingCall = PhoneIncomingCall,
            endCall = PhoneEndCall
        )
        else -> PhoneSemanticColors(
            background = PhoneLightBackground,
            surface = PhoneLightSurface,
            surfaceSecondary = PhoneLightSurfaceSecondary,
            surfaceTertiary = PhoneLightSurfaceTertiary,
            primary = if (accent == AccentPalette.VIOLET) PhoneLightPrimary else accent.primary,
            primaryPressed = PhoneDarkPrimaryPressed,
            primarySoft = PhoneLightPrimarySoft,
            secondary = if (accent == AccentPalette.VIOLET) PhoneLightSecondary else accent.secondary,
            tertiary = PhoneDarkTertiary,
            textPrimary = PhoneLightTextPrimary,
            textSecondary = PhoneLightTextSecondary,
            textMuted = PhoneLightTextMuted,
            outline = PhoneLightOutline,
            success = PhoneLightSuccess,
            warning = PhoneLightWarning,
            error = PhoneLightError,
            incomingCall = PhoneLightIncomingCall,
            endCall = PhoneLightEndCall
        )
    }

    val materialColorScheme = if (effectiveDark) {
        darkColorScheme(
            primary = PhoneSemanticColors.primary,
            onPrimary = Color.White,
            primaryContainer = PhoneSemanticColors.primarySoft,
            onPrimaryContainer = PhoneSemanticColors.textPrimary,
            secondary = PhoneSemanticColors.secondary,
            onSecondary = Color.Black,
            background = PhoneSemanticColors.background,
            onBackground = PhoneSemanticColors.textPrimary,
            surface = PhoneSemanticColors.surface,
            onSurface = PhoneSemanticColors.textPrimary,
            surfaceVariant = PhoneSemanticColors.surfaceSecondary,
            onSurfaceVariant = PhoneSemanticColors.textSecondary,
            outline = PhoneSemanticColors.outline,
            error = PhoneSemanticColors.error,
            onError = Color.White
        )
    } else {
        lightColorScheme(
            primary = PhoneSemanticColors.primary,
            onPrimary = Color.White,
            primaryContainer = PhoneSemanticColors.primarySoft,
            onPrimaryContainer = PhoneSemanticColors.primary,
            secondary = PhoneSemanticColors.secondary,
            onSecondary = Color.White,
            background = PhoneSemanticColors.background,
            onBackground = PhoneSemanticColors.textPrimary,
            surface = PhoneSemanticColors.surface,
            onSurface = PhoneSemanticColors.textPrimary,
            surfaceVariant = PhoneSemanticColors.surfaceSecondary,
            onSurfaceVariant = PhoneSemanticColors.textSecondary,
            outline = PhoneSemanticColors.outline,
            error = PhoneSemanticColors.error,
            onError = Color.White
        )
    }

    CompositionLocalProvider(LocalPhoneColors provides PhoneSemanticColors) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = PhoneTypography,
            shapes = getPhoneShapes(shapePreset),
            content = content
        )
    }
}



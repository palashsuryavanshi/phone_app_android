package com.phone.app.core.designsystem.theme

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.phone.app.core.designsystem.color.*
import com.phone.app.core.designsystem.theme.PhoneAppearance
import com.phone.app.core.designsystem.theme.PhoneDensity
import com.phone.app.core.designsystem.theme.PhonePalette
import com.phone.app.core.designsystem.theme.LayoutDensity
import com.phone.app.core.designsystem.theme.MotionPreference
import com.phone.app.core.designsystem.theme.LocalPhoneTheme
import com.phone.app.core.designsystem.theme.PhoneThemeData
import com.phone.app.core.designsystem.theme.PhoneShapes
import com.phone.app.core.designsystem.theme.ThemeConfiguration

@Composable
fun PhoneTheme(
    config: ThemeConfiguration = ThemeConfiguration(),
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val darkTheme = when (config.appearance) {
        PhoneAppearance.System -> systemDark
        PhoneAppearance.Light -> false
        PhoneAppearance.Dark -> true
    }

    val colorScheme = when (config.palette) {
        PhonePalette.MidnightAurora -> if (darkTheme) MidnightAuroraDark else MidnightAuroraLight
        PhonePalette.SoftCloud -> if (darkTheme) SoftCloudDark else SoftCloudLight
        PhonePalette.EmberPulse -> if (darkTheme) EmberPulseDark else EmberPulseLight
        PhonePalette.OceanGlass -> if (darkTheme) OceanGlassDark else OceanGlassLight
        PhonePalette.ForestMist -> if (darkTheme) ForestMistDark else ForestMistLight
        PhonePalette.RoseQuartz -> if (darkTheme) RoseQuartzDark else RoseQuartzLight
        PhonePalette.MonochromeStudio -> if (darkTheme) MonochromeStudioDark else MonochromeStudioLight
        PhonePalette.SolarFlare -> if (darkTheme) SolarFlareDark else SolarFlareLight
        PhonePalette.LavenderDream -> if (darkTheme) LavenderDreamDark else LavenderDreamLight
        PhonePalette.Custom -> if (darkTheme) darkColorScheme(primary = config.customSeedColor) else lightColorScheme(primary = config.customSeedColor)
        PhonePalette.DefaultPhone -> when {
            config.dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }
            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }
    }

    val animationEnabled = config.motion.preference != MotionPreference.Reduced
    val reducedMotion = config.motion.preference == MotionPreference.Reduced

    val densityLayout = when (config.density) {
        PhoneDensity.Compact -> LayoutDensity.Compact
        PhoneDensity.Comfortable -> LayoutDensity.Comfortable
        PhoneDensity.Spacious -> LayoutDensity.Spacious
    }

    val shapes = PhoneShapes(config.shapes.cornerRadiusScale)

    val phoneThemeData = PhoneThemeData(
        colorScheme = colorScheme,
        typography = androidx.compose.material3.Typography(),
        shapes = shapes,
        isDark = darkTheme,
        animationEnabled = animationEnabled,
        reducedMotion = reducedMotion,
        hapticFeedback = config.hapticFeedback,
        layoutDensity = densityLayout
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? android.app.Activity)?.window
            window?.let {
                it.statusBarColor = colorScheme.background.value.toInt()
                it.navigationBarColor = colorScheme.background.value.toInt()
                WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = !darkTheme
                WindowCompat.getInsetsController(it, view).isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(LocalPhoneTheme provides phoneThemeData) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = androidx.compose.material3.Typography(),
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

private fun PhoneShapes(scale: Float): PhoneShapes {
    val s = scale.coerceIn(0.5f, 2f)
    return PhoneShapes(
        extraSmall = androidx.compose.foundation.shape.RoundedCornerShape((4 * s).dp),
        small = androidx.compose.foundation.shape.RoundedCornerShape((8 * s).dp),
        medium = androidx.compose.foundation.shape.RoundedCornerShape((12 * s).dp),
        large = androidx.compose.foundation.shape.RoundedCornerShape((16 * s).dp),
        xLarge = androidx.compose.foundation.shape.RoundedCornerShape((24 * s).dp),
        xXLarge = androidx.compose.foundation.shape.RoundedCornerShape((32 * s).dp),
        pill = androidx.compose.foundation.shape.RoundedCornerShape(100.dp),
        dialpadButton = androidx.compose.foundation.shape.RoundedCornerShape((28 * s).dp),
        contactCard = androidx.compose.foundation.shape.RoundedCornerShape((24 * s).dp),
        button = androidx.compose.foundation.shape.RoundedCornerShape((16 * s).dp),
        fab = androidx.compose.foundation.shape.RoundedCornerShape((32 * s).dp),
        surface = androidx.compose.foundation.shape.RoundedCornerShape((16 * s).dp),
        avatarCircle = androidx.compose.foundation.shape.CircleShape,
        avatarRounded = androidx.compose.foundation.shape.RoundedCornerShape((16 * s).dp),
        avatarSoft = androidx.compose.foundation.shape.RoundedCornerShape((12 * s).dp)
    )
}



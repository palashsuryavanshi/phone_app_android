package com.phone.app.core.designsystem.tokens

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Centralized Phone design tokens — Futuristic dark aesthetic.
 * No screen should hardcode these values.
 */
object PhoneSpacing {
    val none: Dp = 0.dp
    val xxs: Dp = 2.dp
    val xs: Dp = 4.dp
    val sm: Dp = 8.dp
    val md: Dp = 12.dp
    val lg: Dp = 16.dp
    val xl: Dp = 24.dp
    val xxl: Dp = 32.dp
    val xxxl: Dp = 48.dp
    val huge: Dp = 64.dp
}

object PhoneTouchTargets {
    val min: Dp = 48.dp
    val comfortable: Dp = 56.dp
    val large: Dp = 72.dp
    val dialpad: Dp = 72.dp
    val fab: Dp = 64.dp
}

object PhoneIconSizes {
    val xs = 12.dp
    val sm = 16.dp
    val md = 20.dp
    val lg = 24.dp
    val xl = 32.dp
    val xxl = 48.dp
    val huge = 64.dp
}

object PhoneElevation {
    val level0: Dp = 0.dp
    val level1: Dp = 1.dp
    val level2: Dp = 2.dp
    val level3: Dp = 4.dp
    val level4: Dp = 8.dp
    val level5: Dp = 12.dp
}

object PhoneCornerRadii {
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 24.dp
    val xxl = 32.dp
    val pill = 100.dp
}

object PhoneTypographyTokens {
    val displayLargeSize = 57.sp
    val displayMediumSize = 45.sp
    val displaySmallSize = 36.sp
    val headlineLargeSize = 32.sp
    val headlineMediumSize = 28.sp
    val headlineSmallSize = 24.sp
    val titleLargeSize = 22.sp
    val titleMediumSize = 16.sp
    val titleSmallSize = 14.sp
    val bodyLargeSize = 16.sp
    val bodyMediumSize = 14.sp
    val bodySmallSize = 12.sp
    val labelLargeSize = 14.sp
    val labelMediumSize = 12.sp
    val labelSmallSize = 11.sp
}

object PhoneMotionDurations {
    val instant = 80
    val fast = 150
    val standard = 300
    val slow = 450
    val slower = 600
}

object PhoneMotionSpecs {
    const val dampingRatio = 0.85f
    const val stiffness = 600f
    const val pressScale = 0.96f
}

// Futuristic dark color palette matching the UI showcase
object PhoneColors {
    // Deep backgrounds
    val Background = androidx.compose.ui.graphics.Color(0xFF0A0B10)
    val BackgroundDark = androidx.compose.ui.graphics.Color(0xFF050508)
    val Surface = androidx.compose.ui.graphics.Color(0xFF12131A)
    val SurfaceDark = androidx.compose.ui.graphics.Color(0xFF0E0F14)

    // Cards and elevated surfaces
    val SurfaceContainer = androidx.compose.ui.graphics.Color(0xFF1A1B24)
    val SurfaceContainerHigh = androidx.compose.ui.graphics.Color(0xFF22232E)

    // Text
    val OnBackground = androidx.compose.ui.graphics.Color(0xFFFFFFFF)
    val OnSurface = androidx.compose.ui.graphics.Color(0xFFE8EAF0)
    val OnSurfaceVariant = androidx.compose.ui.graphics.Color(0xFF8E919C)

    // Primary brand - vibrant purple/blue gradient
    val Primary = androidx.compose.ui.graphics.Color(0xFF7C3AED)
    val PrimaryDark = androidx.compose.ui.graphics.Color(0xFFBCA0FF)
    val PrimaryContainer = androidx.compose.ui.graphics.Color(0xFF3B078F)
    val OnPrimary = androidx.compose.ui.graphics.Color(0xFFFFFFFF)
    val OnPrimaryContainer = androidx.compose.ui.graphics.Color(0xFFFFD4FF)

    // Secondary - electric blue
    val Secondary = androidx.compose.ui.graphics.Color(0xFF06B6D4)
    val SecondaryDark = androidx.compose.ui.graphics.Color(0xFF22D3EE)
    val SecondaryContainer = androidx.compose.ui.graphics.Color(0xFF0E5A78)

    // Tertiary - magenta/pink
    val Tertiary = androidx.compose.ui.graphics.Color(0xFFEB4B9C)
    val TertiaryDark = androidx.compose.ui.graphics.Color(0xFFF0ABFC)

    // Call semantics
    val Incoming = androidx.compose.ui.graphics.Color(0xFF10B981)
    val IncomingDark = androidx.compose.ui.graphics.Color(0xFF34D399)
    val Outgoing = androidx.compose.ui.graphics.Color(0xFF3B82F6)
    val OutgoingDark = androidx.compose.ui.graphics.Color(0xFF60A5FA)
    val Missed = androidx.compose.ui.graphics.Color(0xFFEF4444)
    val MissedDark = androidx.compose.ui.graphics.Color(0xFFF87171)
    val Voicemail = androidx.compose.ui.graphics.Color(0xFF8B5CF6)

    // Semantic
    val Error = androidx.compose.ui.graphics.Color(0xFFEF4444)
    val OnError = androidx.compose.ui.graphics.Color(0xFFFFFFFF)
    val Success = androidx.compose.ui.graphics.Color(0xFF10B981)
    val Warning = androidx.compose.ui.graphics.Color(0xFFF59E0B)
    val Info = androidx.compose.ui.graphics.Color(0xFF3B82F6)

    // Dividers and outlines
    val Divider = androidx.compose.ui.graphics.Color(0xFF1E1F2A)
    val Outline = androidx.compose.ui.graphics.Color(0xFF2D2E38)
    val OutlineVariant = androidx.compose.ui.graphics.Color(0xFF22232E)
    val Scrim = androidx.compose.ui.graphics.Color(0xB3000000)

    // Gradients (used in PhoneGradientConfig)
    val GradientStart = Primary
    val GradientEnd = Secondary
}

// Color extension for convenience
val androidx.compose.ui.graphics.Color.Companion.PhonePrimary: androidx.compose.ui.graphics.Color
    get() = PhoneColors.Primary
val androidx.compose.ui.graphics.Color.Companion.PhoneSecondary: androidx.compose.ui.graphics.Color
    get() = PhoneColors.Secondary


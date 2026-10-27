package com.phone.app.core.designsystem.theme

import androidx.compose.ui.graphics.Color

enum class PhoneAppearance { System, Light, Dark }
enum class PhoneDensity { Compact, Comfortable, Spacious }
enum class PhonePalette { DefaultPhone, MidnightAurora, SoftCloud, EmberPulse, OceanGlass, ForestMist, RoseQuartz, MonochromeStudio, SolarFlare, LavenderDream, Custom }
enum class PhoneTypographyScale { Default, Large, ExtraLarge }
enum class PhoneAnimationPreference { System, Enabled, Reduced }
enum class ShapeFamily { Circular, RoundedSquare, SoftPill, StandardRounded, MinimalGeometric }
enum class MotionPreference { Standard, Subtle, Reduced, System }
enum class GradientDirection { TopBottom, BottomTop, LeftRight, RightLeft, Diagonal }

data class GradientConfig(
    val enabled: Boolean = false,
    val startColor: Color = Color(0xFF6750A4),
    val endColor: Color = Color(0xFFD0BCFF),
    val direction: GradientDirection = GradientDirection.TopBottom
)

data class TypographyConfig(
    val scale: PhoneTypographyScale = PhoneTypographyScale.Default,
    val fontFamily: String = "default",
    val numberSpacing: Float = 0f
)

data class ShapeConfig(
    val family: ShapeFamily = ShapeFamily.StandardRounded,
    val cornerRadiusScale: Float = 1f
)

data class DialerConfig(
    val keyShape: ShapeFamily = ShapeFamily.RoundedSquare,
    val keySizeScale: Float = 1f,
    val keySpacingScale: Float = 1f,
    val letterLabels: Boolean = true,
    val numberTypography: String = "default"
)

data class ContactsConfig(
    val density: PhoneDensity = PhoneDensity.Comfortable,
    val avatarSizeScale: Float = 1f,
    val metadataVisible: Boolean = true
)

data class CallScreenConfig(
    val backgroundPalette: String = "default",
    val avatarStyle: String = "circle",
    val controlShapes: ShapeFamily = ShapeFamily.StandardRounded,
    val motionIntensity: Float = 1f
)

data class NavConfig(
    val appearance: String = "default"
)

data class MotionConfig(
    val preference: MotionPreference = MotionPreference.System
)

data class ThemeConfiguration(
    val version: Int = 1,
    val appearance: PhoneAppearance = PhoneAppearance.System,
    val dynamicColor: Boolean = true,
    val palette: PhonePalette = PhonePalette.DefaultPhone,
    val customSeedColor: Color = Color(0xFF6750A4),
    val gradient: GradientConfig = GradientConfig(),
    val typography: TypographyConfig = TypographyConfig(),
    val shapes: ShapeConfig = ShapeConfig(),
    val dialer: DialerConfig = DialerConfig(),
    val contacts: ContactsConfig = ContactsConfig(),
    val calls: CallScreenConfig = CallScreenConfig(),
    val navigation: NavConfig = NavConfig(),
    val motion: MotionConfig = MotionConfig(),
    val hapticFeedback: Boolean = true,
    val density: PhoneDensity = PhoneDensity.Comfortable
)


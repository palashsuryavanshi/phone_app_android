package com.phone.app.core.designsystem.theme

import androidx.compose.ui.graphics.Color
import com.phone.app.core.designsystem.color.*

// Light theme colors
val LightColors = androidx.compose.material3.ColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = Tertiary,
    onTertiary = OnTertiary,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    error = Error,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Outline,
    outlineVariant = OutlineVariant,
    inverseSurface = InverseSurface,
    inverseOnSurface = InverseOnSurface,
    inversePrimary = InversePrimary,
    surfaceTint = Primary,
    scrim = Color(0x80000000)
)

// Dark theme colors
val DarkColors = androidx.compose.material3.ColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary = TertiaryDark,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    inverseSurface = InverseSurfaceDark,
    inverseOnSurface = InverseOnSurfaceDark,
    inversePrimary = InversePrimaryDark,
    surfaceTint = PrimaryDark,
    scrim = Color(0x80000000)
)

// Semantic call colors
object CallColors {
    val Incoming = Color(0xFF4CAF50)
    val IncomingDark = Color(0xFF81C784)
    val Outgoing = Color(0xFF2196F3)
    val OutgoingDark = Color(0xFF64B5F6)
    val Missed = Color(0xFFF44336)
    val MissedDark = Color(0xFFE57373)
    val Voicemail = Color(0xFF9C27B0)
    val VoicemailDark = Color(0xFFBA68C8)

    fun getCallColor(type: CallType, isDarkTheme: Boolean): Color {
        return when (type) {
            CallType.Incoming -> if (isDarkTheme) IncomingDark else Incoming
            CallType.Outgoing -> if (isDarkTheme) OutgoingDark else Outgoing
            CallType.Missed -> if (isDarkTheme) MissedDark else Missed
            CallType.Voicemail -> if (isDarkTheme) VoicemailDark else Voicemail
        }
    }
}

enum class CallType {
    Incoming,
    Outgoing,
    Missed,
    Voicemail
}

package com.phone.app.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ==========================================
// Phone PHONE DESIGN SYSTEM COLOR TOKENS
// ==========================================

// --- Dark Theme Tokens (Default Primary Canvas) ---
val PhoneDarkBackground = Color(0xFF080D16)
val PhoneDarkSurface = Color(0xFF101A28)
val PhoneDarkSurfaceSecondary = Color(0xFF142436)
val PhoneDarkSurfaceTertiary = Color(0xFF1B2C40)
val PhoneDarkPrimary = Color(0xFF7565FF)
val PhoneDarkPrimaryPressed = Color(0xFF6251EA)
val PhoneDarkPrimarySoft = Color(0xFF28234F)
val PhoneDarkSecondary = Color(0xFF20D6CB)
val PhoneDarkTertiary = Color(0xFFEF63C8)
val PhoneDarkTextPrimary = Color(0xFFF4F6FC)
val PhoneDarkTextSecondary = Color(0xFFA8B4C8)
val PhoneDarkTextMuted = Color(0xFF77879E)
val PhoneDarkOutline = Color(0xFF29384D)
val PhoneDarkSuccess = Color(0xFF39D9A5)
val PhoneDarkWarning = Color(0xFFFFC66D)
val PhoneDarkError = Color(0xFFFF647C)
val PhoneIncomingCall = Color(0xFF27D9A2)
val PhoneEndCall = Color(0xFFFF5267)

// --- Light Theme Tokens ---
val PhoneLightBackground = Color(0xFFF7F8FC)
val PhoneLightSurface = Color(0xFFFFFFFF)
val PhoneLightSurfaceSecondary = Color(0xFFEFF1F8)
val PhoneLightSurfaceTertiary = Color(0xFFE6E9F3)
val PhoneLightPrimary = Color(0xFF5847E8)
val PhoneLightPrimarySoft = Color(0xFFEAE6FF)
val PhoneLightSecondary = Color(0xFF087F82)
val PhoneLightTextPrimary = Color(0xFF171A27)
val PhoneLightTextSecondary = Color(0xFF555E70)
val PhoneLightTextMuted = Color(0xFF737D8D)
val PhoneLightOutline = Color(0xFFD8DDEA)
val PhoneLightSuccess = Color(0xFF087F62)
val PhoneLightWarning = Color(0xFF8B5700)
val PhoneLightError = Color(0xFFB4233C)
val PhoneLightIncomingCall = Color(0xFF087F62)
val PhoneLightEndCall = Color(0xFFC92D43)

// --- AMOLED Theme Tokens ---
val PhoneAmoledBackground = Color(0xFF000000)
val PhoneAmoledSurface = Color(0xFF080A10)
val PhoneAmoledElevatedSurface = Color(0xFF10131C)
val PhoneAmoledSurfaceTertiary = Color(0xFF161B28)
val PhoneAmoledPrimary = Color(0xFF8B7CFF)
val PhoneAmoledSecondary = Color(0xFF35E5D8)
val PhoneAmoledTextPrimary = Color(0xFFF7F7FC)
val PhoneAmoledTextSecondary = Color(0xFFA5ADBE)
val PhoneAmoledTextMuted = Color(0xFF6B768B)
val PhoneAmoledOutline = Color(0xFF222938)

// ==========================================
// GRADIENT SYSTEM
// ==========================================
object PhoneGradients {
    // Violet Pulse: Expressive highlights, active call, hero buttons
    val VioletPulse = Brush.linearGradient(
        colors = listOf(Color(0xFF7565FF), Color(0xFFA35DFF), Color(0xFFEF63C8))
    )
    
    // Arctic Flow: Secondary active highlights, call screening, incoming badges
    val ArcticFlow = Brush.linearGradient(
        colors = listOf(Color(0xFF20D6CB), Color(0xFF318DF4), Color(0xFF7565FF))
    )
    
    // Aurora Night: Atmospheric incoming call canvas and immersive backgrounds
    val AuroraNight = Brush.linearGradient(
        colors = listOf(Color(0xFF101A28), Color(0xFF183B53), Color(0xFF442D70))
    )
    
    // Sunset Ember: Warm personalized call wallpaper & accents
    val SunsetEmber = Brush.linearGradient(
        colors = listOf(Color(0xFFFF996B), Color(0xFFED72B7), Color(0xFF7565FF))
    )
    
    // Emerald Night: Protected spam shield and security status
    val EmeraldNight = Brush.linearGradient(
        colors = listOf(Color(0xFF071A19), Color(0xFF0D4744), Color(0xFF20D6CB))
    )
    
    // Monochrome: Understated, focused call & dialer card textures
    val Monochrome = Brush.linearGradient(
        colors = listOf(Color(0xFF202430), Color(0xFF40475A), Color(0xFF0B0D13))
    )
    
    // Soft Surface Shimmer: used for card ambient highlights
    val SurfaceShimmer = Brush.linearGradient(
        colors = listOf(Color(0x207565FF), Color(0x0520D6CB), Color(0x00000000))
    )
}



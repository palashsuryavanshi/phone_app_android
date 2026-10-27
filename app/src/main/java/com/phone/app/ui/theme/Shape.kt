package com.phone.app.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// ==========================================
// Phone PHONE SHAPE SYSTEM
// ==========================================

enum class ShapePreset(val displayName: String) {
    EXPRESSIVE("Expressive Curved"), // Default 18dp standard
    SMOOTH("Modern Smooth"),         // 14dp standard
    MINIMAL("Sharp Minimal")         // 8dp standard
}

fun getPhoneShapes(preset: ShapePreset = ShapePreset.EXPRESSIVE): Shapes {
    return when (preset) {
        ShapePreset.EXPRESSIVE -> Shapes(
            extraSmall = RoundedCornerShape(8.dp),
            small = RoundedCornerShape(12.dp),      // Small controls (10-14dp)
            medium = RoundedCornerShape(18.dp),     // Standard cards (16-20dp)
            large = RoundedCornerShape(26.dp),      // Large panels (22-28dp)
            extraLarge = RoundedCornerShape(32.dp)  // Modal sheets (28-32dp)
        )
        ShapePreset.SMOOTH -> Shapes(
            extraSmall = RoundedCornerShape(6.dp),
            small = RoundedCornerShape(10.dp),
            medium = RoundedCornerShape(14.dp),
            large = RoundedCornerShape(20.dp),
            extraLarge = RoundedCornerShape(26.dp)
        )
        ShapePreset.MINIMAL -> Shapes(
            extraSmall = RoundedCornerShape(4.dp),
            small = RoundedCornerShape(6.dp),
            medium = RoundedCornerShape(8.dp),
            large = RoundedCornerShape(12.dp),
            extraLarge = RoundedCornerShape(16.dp)
        )
    }
}

// Modal bottom sheet top corner radius
val PhoneModalSheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
val PhonePillShape = CircleShape
val PhoneCallButtonShape = CircleShape
val PhoneDialerKeyShape = CircleShape



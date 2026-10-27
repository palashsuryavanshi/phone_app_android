package com.phone.app.core.designsystem.artwork

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Abstract flowing ribbon artwork using Box-based gradients.
 * Original composition representing communication flow.
 */
@Composable
fun PhoneRibbonArtwork(
    modifier: Modifier = Modifier,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    secondaryColor: Color = MaterialTheme.colorScheme.secondary
) = Box(
    modifier = modifier.fillMaxSize()
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(
                brush = androidx.compose.ui.graphics.Brush.linearGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.35f),
                        accentColor.copy(alpha = 0.05f)
                    )
                ),
                shape = CircleShape
            )
            .align(Alignment.TopCenter)
            .padding(top = -40.dp)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(
                brush = androidx.compose.ui.graphics.Brush.linearGradient(
                    colors = listOf(
                        secondaryColor.copy(alpha = 0.25f),
                        secondaryColor.copy(alpha = 0.05f)
                    )
                ),
                shape = CircleShape
            )
            .align(Alignment.Center)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(
                brush = androidx.compose.ui.graphics.Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFEF63C8).copy(alpha = 0.2f),
                        Color(0xFFEF63C8).copy(alpha = 0.05f)
                    )
                ),
                shape = CircleShape
            )
            .align(Alignment.BottomCenter)
            .padding(bottom = -20.dp)
    )

    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(accentColor.copy(alpha = 0.15f))
            .align(Alignment.Center)
            .offset(x = -40.dp, y = -60.dp)
    )
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(secondaryColor.copy(alpha = 0.12f))
            .align(Alignment.Center)
            .offset(x = 40.dp, y = 80.dp)
    )
}

@Composable
fun PhoneCallArtwork(
    modifier: Modifier = Modifier,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    sizeDp: Dp = 200.dp
) = Box(
    modifier = modifier.size(sizeDp)
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .background(
                brush = androidx.compose.ui.graphics.Brush.radialGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.12f),
                        accentColor.copy(alpha = 0.02f)
                    ),
                    center = Offset(0f, 0f)
                )
            )
    )
    Box(
        modifier = Modifier
            .size((sizeDp.value * 0.8f).dp)
            .clip(CircleShape)
            .background(
                brush = androidx.compose.ui.graphics.Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f),
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.02f)
                    ),
                    center = Offset(0f, 0f)
                )
            )
            .align(Alignment.Center)
    )
    Box(
        modifier = Modifier
            .size((sizeDp.value * 0.6f).dp)
            .clip(CircleShape)
            .background(
                brush = androidx.compose.ui.graphics.Brush.radialGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.3f),
                        accentColor.copy(alpha = 0.0f)
                    ),
                    center = Offset(0f, 0f)
                )
            )
            .align(Alignment.Center)
    )

    Box(
        modifier = Modifier
            .size((sizeDp.value * 0.12f).dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.6f))
            .align(Alignment.Center)
    )
}

@Composable
fun PhoneCommunicationSymbol(
    modifier: Modifier = Modifier,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    sizeDp: Dp = 120.dp
) = Box(
    modifier = modifier.size(sizeDp)
) {
    Box(
        modifier = Modifier
            .size((sizeDp.value * 0.9f).dp)
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(28.dp))
            .background(accentColor.copy(alpha = 0.15f))
            .align(Alignment.Center)
            .offset(x = -18.dp, y = -6.dp)
    )
    Box(
        modifier = Modifier
            .size((sizeDp.value * 0.8f).dp)
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f))
            .align(Alignment.Center)
            .offset(x = 12.dp, y = 6.dp)
    )

    Box(
        modifier = Modifier
            .size((sizeDp.value * 0.2f).dp, 3.dp)
            .background(accentColor.copy(alpha = 0.3f))
            .align(Alignment.Center)
            .offset(y = (sizeDp.value * 0.25f).dp)
    )

    Box(
        modifier = Modifier
            .size(6.dp)
            .clip(CircleShape)
            .background(accentColor.copy(alpha = 0.8f))
            .align(Alignment.Center)
            .offset(x = -30.dp, y = 30.dp)
    )
    Box(
        modifier = Modifier
            .size(6.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f))
            .align(Alignment.Center)
            .offset(x = 0.dp, y = 36.dp)
    )
    Box(
        modifier = Modifier
            .size(6.dp)
            .clip(CircleShape)
            .background(Color(0xFFEF63C8).copy(alpha = 0.7f))
            .align(Alignment.Center)
            .offset(x = 30.dp, y = 30.dp)
    )
}

@Composable
fun PhoneGradientSurface(
    colors: List<Color> = PhoneArtworkPalette.themePreviewGradient,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp
) = Box(
    modifier = modifier
        .background(
            brush = androidx.compose.ui.graphics.Brush.radialGradient(
                colors = colors,
                center = Offset(0f, 0f)
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(cornerRadius)
        )
)

object PhoneArtworkPalette {
    val onboardingRibbon = listOf(
        Color(0xFF080D16),
        Color(0xFF0F1A28),
        Color(0xFF080D16)
    )
    val themePreviewGradient = listOf(
        Color(0xFF142436),
        Color(0xFF0F1A28),
        Color(0xFF080D16)
    )
    val callScreenGradient = listOf(
        Color(0xFF080D16),
        Color(0xFF0A1524),
        Color(0xFF080D16)
    )
    val privacyGradient = listOf(
        Color(0xFF080D16),
        Color(0xFF0C1522),
        Color(0xFF080D16)
    )
}

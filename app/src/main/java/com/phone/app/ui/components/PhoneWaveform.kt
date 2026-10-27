package com.phone.app.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.phone.app.ui.theme.PhoneTheme

@Composable
fun PhoneWaveformVisualizer(
    isMuted: Boolean,
    isHeld: Boolean,
    modifier: Modifier = Modifier
) {
    val colors = PhoneTheme.colors
    val infiniteTransition = rememberInfiniteTransition(label = "waveform_anim")

    // Multiple phase animations for realistic voice waveform cadence
    val scale1 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(450, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "w1"
    )

    val scale2 by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "w2"
    )

    val scale3 by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(520, delayMillis = 200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "w3"
    )

    val scale4 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(480, delayMillis = 50, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "w4"
    )

    val heights = if (isMuted || isHeld) {
        listOf(0.15f, 0.15f, 0.15f, 0.15f, 0.15f, 0.15f, 0.15f, 0.15f, 0.15f)
    } else {
        listOf(scale1 * 0.5f, scale2, scale3 * 0.8f, scale1, scale3, scale2 * 0.9f, scale4, scale1 * 0.7f, scale2 * 0.4f)
    }

    val waveBrush = if (isMuted) {
        Brush.verticalGradient(listOf(colors.textMuted, colors.textMuted))
    } else if (isHeld) {
        Brush.verticalGradient(listOf(colors.warning, colors.warning.copy(alpha = 0.4f)))
    } else {
        Brush.verticalGradient(listOf(colors.secondary, colors.primary))
    }

    Row(
        modifier = modifier.height(38.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        heights.forEach { frac ->
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight(fraction = frac.coerceIn(0.1f, 1f))
                    .clip(CircleShape)
                    .background(waveBrush)
            )
        }
    }
}



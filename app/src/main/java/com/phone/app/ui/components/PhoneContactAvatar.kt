package com.phone.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phone.app.ui.theme.PhoneGradients
import com.phone.app.ui.theme.PhoneTheme

enum class AvatarSize(val dimension: Dp, val fontSize: Int, val badgeSize: Dp) {
    SMALL(40.dp, 14, 10.dp),
    MEDIUM(52.dp, 18, 12.dp),
    LARGE(80.dp, 28, 16.dp),
    HERO(108.dp, 38, 22.dp)
}

@Composable
fun PhoneContactAvatar(
    initials: String,
    modifier: Modifier = Modifier,
    size: AvatarSize = AvatarSize.MEDIUM,
    gradient: Brush = PhoneGradients.VioletPulse,
    badgeColor: Color? = null,
    badgeContent: @Composable (() -> Unit)? = null
) {
    val colors = PhoneTheme.colors

    Box(
        modifier = modifier
            .size(size.dimension)
            .semantics { contentDescription = "Avatar $initials" },
        contentAlignment = Alignment.Center
    ) {
        // Base Avatar Circle with Gradient Background
        Box(
            modifier = Modifier
                .size(size.dimension)
                .clip(CircleShape)
                .background(gradient)
                .border(1.5.dp, colors.outline.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials.take(2).uppercase(),
                color = Color.White,
                fontSize = size.fontSize.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }

        // Optional Status Badge in bottom-end corner
        if (badgeColor != null) {
            Box(
                modifier = Modifier
                    .size(size.badgeSize)
                    .align(Alignment.BottomEnd)
                    .offset(x = 1.dp, y = 1.dp)
                    .clip(CircleShape)
                    .background(badgeColor)
                    .border(2.dp, colors.background, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                badgeContent?.invoke()
            }
        }
    }
}



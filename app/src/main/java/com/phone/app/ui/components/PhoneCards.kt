package com.phone.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CallMissed
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phone.app.data.model.CallLogEntry
import com.phone.app.data.model.CallType
import com.phone.app.data.model.Contact
import com.phone.app.ui.theme.PhoneGradients
import com.phone.app.ui.theme.PhoneTheme

@Composable
fun PhoneCallRow(
    callLog: CallLogEntry,
    onRowClick: () -> Unit,
    onCallClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = PhoneTheme.colors
    val shapes = MaterialTheme.shapes

    val (typeIcon, typeColor, typeDesc) = when (callLog.callType) {
        CallType.INCOMING -> Triple(Icons.Default.CallReceived, colors.secondary, "Incoming")
        CallType.OUTGOING -> Triple(Icons.Default.CallMade, colors.primary, "Outgoing")
        CallType.MISSED -> Triple(Icons.Default.CallMissed, colors.error, "Missed")
        CallType.BLOCKED_SPAM -> Triple(Icons.Default.Shield, colors.warning, "Blocked Spam")
    }

    val initials = (callLog.name ?: callLog.phoneNumber)
        .split(" ")
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")
        .ifEmpty { "C" }

    Row(
        modifier = modifier
            .testTag("call_log_row_${callLog.id}")
            .fillMaxWidth()
            .clip(shapes.medium)
            .background(colors.surfaceSecondary.copy(alpha = 0.6f))
            .border(1.dp, colors.outline.copy(alpha = 0.35f), shapes.medium)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true),
                onClick = onRowClick
            )
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PhoneContactAvatar(
            initials = initials,
            size = AvatarSize.MEDIUM,
            gradient = when (callLog.callType) {
                CallType.MISSED -> PhoneGradients.SunsetEmber
                CallType.BLOCKED_SPAM -> PhoneGradients.EmeraldNight
                else -> PhoneGradients.VioletPulse
            }
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = callLog.name ?: callLog.phoneNumber,
                color = colors.textPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(3.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = typeIcon,
                    contentDescription = typeDesc,
                    tint = typeColor,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${callLog.timestampFormatted} • ${callLog.durationFormatted}",
                    color = colors.textMuted,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
        }

        IconButton(
            onClick = onCallClick,
            modifier = Modifier
                .testTag("callback_button_${callLog.id}")
                .size(42.dp)
                .clip(CircleShape)
                .background(colors.surfaceTertiary)
        ) {
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = "Call ${callLog.name ?: callLog.phoneNumber}",
                tint = colors.secondary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun PhoneContactRow(
    contact: Contact,
    onClick: () -> Unit,
    onCallClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = PhoneTheme.colors
    val shapes = MaterialTheme.shapes

    Row(
        modifier = modifier
            .testTag("contact_row_${contact.id}")
            .fillMaxWidth()
            .clip(shapes.medium)
            .background(colors.surfaceSecondary.copy(alpha = 0.5f))
            .border(1.dp, colors.outline.copy(alpha = 0.35f), shapes.medium)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true),
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PhoneContactAvatar(
            initials = contact.avatarInitials,
            size = AvatarSize.MEDIUM,
            gradient = if (contact.isFavorite) PhoneGradients.SunsetEmber else PhoneGradients.ArcticFlow
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = contact.name,
                    color = colors.textPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (contact.isFavorite) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Favorite",
                        tint = colors.warning,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "${contact.phoneNumber} • ${contact.group}",
                color = colors.textSecondary,
                fontSize = 12.sp,
                maxLines = 1
            )
        }

        IconButton(
            onClick = onCallClick,
            modifier = Modifier
                .testTag("call_contact_button_${contact.id}")
                .size(40.dp)
                .clip(CircleShape)
                .background(colors.surfaceTertiary)
        ) {
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = "Call ${contact.name}",
                tint = colors.incomingCall,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun PhoneSettingsRow(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingBadge: String? = null,
    testTag: String = "settings_row"
) {
    val colors = PhoneTheme.colors
    val shapes = MaterialTheme.shapes

    Row(
        modifier = modifier
            .testTag(testTag)
            .fillMaxWidth()
            .clip(shapes.medium)
            .background(colors.surfaceSecondary.copy(alpha = 0.6f))
            .border(1.dp, colors.outline.copy(alpha = 0.4f), shapes.medium)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true),
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(colors.surfaceTertiary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colors.primary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = colors.textPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                color = colors.textMuted,
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (trailingBadge != null) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(colors.primary.copy(alpha = 0.18f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = trailingBadge,
                    color = colors.primary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = colors.textMuted,
            modifier = Modifier.size(14.dp)
        )
    }
}

@Composable
fun PhoneToggleRow(
    title: String,
    description: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    testTag: String = "toggle_row"
) {
    val colors = PhoneTheme.colors
    val shapes = MaterialTheme.shapes

    Row(
        modifier = modifier
            .testTag(testTag)
            .fillMaxWidth()
            .clip(shapes.medium)
            .background(colors.surfaceSecondary.copy(alpha = 0.6f))
            .border(1.dp, colors.outline.copy(alpha = 0.4f), shapes.medium)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colors.surfaceTertiary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = colors.textPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                color = colors.textMuted,
                fontSize = 12.sp,
                maxLines = 2
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = colors.primary,
                uncheckedThumbColor = colors.textMuted,
                uncheckedTrackColor = colors.surfaceTertiary
            ),
            modifier = Modifier.semantics { contentDescription = "$title toggle" }
        )
    }
}

@Composable
fun PhoneActiveCallMiniBar(
    callerName: String,
    durationText: String,
    onClick: () -> Unit,
    onEndClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = PhoneTheme.colors

    Row(
        modifier = modifier
            .testTag("active_call_minibar")
            .fillMaxWidth()
            .clip(CircleShape)
            .background(colors.surfaceTertiary)
            .border(1.dp, colors.primary.copy(alpha = 0.6f), CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true),
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(colors.incomingCall),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PhoneInTalk,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = callerName,
                    color = colors.textPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Active Call • $durationText",
                    color = colors.secondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(colors.endCall)
                .clickable(onClick = onEndClick)
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Text(
                text = "End",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}



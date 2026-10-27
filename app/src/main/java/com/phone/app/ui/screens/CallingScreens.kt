package com.phone.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phone.app.data.model.ActiveCallSession
import com.phone.app.data.model.AudioRoute
import com.phone.app.data.model.CallStatus
import com.phone.app.ui.components.AvatarSize
import com.phone.app.ui.components.PhoneActionIconButton
import com.phone.app.ui.components.PhoneCallButton
import com.phone.app.ui.components.PhoneContactAvatar
import com.phone.app.ui.components.PhoneEndCallButton
import com.phone.app.ui.components.PhoneWaveformVisualizer
import com.phone.app.ui.theme.PhoneGradients
import com.phone.app.ui.theme.PhoneModalSheetShape
import com.phone.app.ui.theme.PhoneSpecialTypography
import com.phone.app.ui.theme.PhoneTheme
import com.phone.app.ui.viewmodel.PhoneViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenCallContainer(
    session: ActiveCallSession,
    viewModel: PhoneViewModel,
    modifier: Modifier = Modifier
) {
    val colors = PhoneTheme.colors

    var showQuickReplySheet by remember { mutableStateOf(false) }
    var showAudioRouteSheet by remember { mutableStateOf(false) }
    var showInCallKeypad by remember { mutableStateOf(false) }

    val quickMessages = listOf(
        "Can't talk now. What's up?",
        "I'll call you right back.",
        "In a meeting. Please text me.",
        "On my way!"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PhoneGradients.AuroraNight)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        when (session.status) {
            CallStatus.INCOMING_RINGING -> {
                IncomingCallContent(
                    session = session,
                    onAnswer = { viewModel.answerCall() },
                    onDecline = { viewModel.declineCall() },
                    onQuickReply = { showQuickReplySheet = true }
                )
            }
            CallStatus.OUTGOING_RINGING -> {
                OutgoingCallContent(
                    session = session,
                    onEndCall = { viewModel.endCall() }
                )
            }
            CallStatus.ACTIVE, CallStatus.ON_HOLD -> {
                if (showInCallKeypad) {
                    InCallKeypadContent(
                        session = session,
                        onDigitClick = { viewModel.appendDtmfInCall(it) },
                        onClose = { showInCallKeypad = false }
                    )
                } else {
                    ActiveCallContent(
                        session = session,
                        viewModel = viewModel,
                        onOpenKeypad = { showInCallKeypad = true },
                        onOpenAudioRoute = { showAudioRouteSheet = true },
                        onEndCall = { viewModel.endCall() }
                    )
                }
            }
            CallStatus.ENDED -> {
                CallEndedContent(session = session)
            }
            CallStatus.IDLE -> {}
        }

        // Quick Message Reply Bottom Sheet
        if (showQuickReplySheet) {
            ModalBottomSheet(
                onDismissRequest = { showQuickReplySheet = false },
                sheetState = rememberModalBottomSheetState(),
                containerColor = colors.surfaceSecondary,
                shape = PhoneModalSheetShape
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Quick Reply to ${session.callerName}",
                        color = colors.textPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    quickMessages.forEach { msg ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clip(CircleShape)
                                .background(colors.surfaceTertiary)
                                .clickable {
                                    showQuickReplySheet = false
                                    viewModel.declineCall("Sent: \"$msg\"")
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = msg,
                                color = colors.textPrimary,
                                fontSize = 14.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        // Audio Route Selector Bottom Sheet
        if (showAudioRouteSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAudioRouteSheet = false },
                sheetState = rememberModalBottomSheetState(),
                containerColor = colors.surfaceSecondary,
                shape = PhoneModalSheetShape
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Select Audio Output",
                        color = colors.textPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    AudioRoute.values().forEach { route ->
                        val isSelected = session.audioRoute == route
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) colors.primary.copy(alpha = 0.2f) else colors.surfaceTertiary)
                                .border(
                                    1.dp,
                                    if (isSelected) colors.primary else Color.Transparent,
                                    CircleShape
                                )
                                .clickable {
                                    viewModel.setAudioRoute(route)
                                    showAudioRouteSheet = false
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = when (route) {
                                    AudioRoute.EARPIECE -> Icons.Default.Hearing
                                    AudioRoute.SPEAKER -> Icons.Default.VolumeUp
                                    AudioRoute.BLUETOOTH -> Icons.Default.Bluetooth
                                },
                                contentDescription = null,
                                tint = if (isSelected) colors.primary else colors.textPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = route.label,
                                color = if (isSelected) colors.primary else colors.textPrimary,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun IncomingCallContent(
    session: ActiveCallSession,
    onAnswer: () -> Unit,
    onDecline: () -> Unit,
    onQuickReply: () -> Unit
) {
    val colors = PhoneTheme.colors

    val infiniteTransition = rememberInfiniteTransition(label = "halo_ring")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "halo_scale"
    )

    Column(
        modifier = Modifier
            .testTag("incoming_call_screen")
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top caller info & Spam badge
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            if (session.isSpamSuspect) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(colors.warning.copy(alpha = 0.22f))
                        .border(1.dp, colors.warning, CircleShape)
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = colors.warning,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "SUSPECTED SPAM CALL",
                            color = colors.warning,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            } else {
                Text(
                    text = "INCOMING CALL",
                    color = colors.secondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.8.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = session.callerName,
                color = colors.textPrimary,
                style = PhoneSpecialTypography.IncomingCallerName,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = session.callerNumber,
                color = colors.textSecondary,
                fontSize = 16.sp
            )

            if (session.spamReason != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = session.spamReason,
                    color = colors.warning,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }

        // Center Pulsing Avatar
        Box(
            modifier = Modifier.size(160.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size((140 * pulseScale).dp)
                    .clip(CircleShape)
                    .background(
                        if (session.isSpamSuspect) colors.warning.copy(alpha = 0.15f)
                        else colors.primary.copy(alpha = 0.15f)
                    )
            )
            PhoneContactAvatar(
                initials = session.avatarInitials,
                size = AvatarSize.HERO,
                gradient = if (session.isSpamSuspect) PhoneGradients.SunsetEmber else PhoneGradients.VioletPulse
            )
        }

        // Bottom Actions: Decline (Red), Quick Message, Answer (Green)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(colors.surfaceSecondary)
                    .clickable(onClick = onQuickReply)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Message,
                        contentDescription = null,
                        tint = colors.textSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Quick Reply",
                        color = colors.textSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(0.85f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    PhoneEndCallButton(
                        onClick = onDecline,
                        size = 72.dp,
                        label = "Decline Call",
                        testTag = "incoming_decline_button"
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Decline", color = colors.textSecondary, fontSize = 12.sp)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    PhoneCallButton(
                        onClick = onAnswer,
                        size = 72.dp,
                        label = "Answer Call",
                        testTag = "incoming_answer_button"
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Answer", color = colors.incomingCall, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun OutgoingCallContent(
    session: ActiveCallSession,
    onEndCall: () -> Unit
) {
    val colors = PhoneTheme.colors

    Column(
        modifier = Modifier
            .testTag("outgoing_call_screen")
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text(
                text = "CALLING...",
                color = colors.secondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.8.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = session.callerName,
                color = colors.textPrimary,
                style = PhoneSpecialTypography.IncomingCallerName
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = session.callerNumber,
                color = colors.textSecondary,
                fontSize = 15.sp
            )
        }

        PhoneContactAvatar(
            initials = session.avatarInitials,
            size = AvatarSize.HERO,
            gradient = PhoneGradients.ArcticFlow
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            PhoneEndCallButton(
                onClick = onEndCall,
                size = 72.dp,
                label = "Cancel Call",
                testTag = "outgoing_cancel_button"
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Cancel", color = colors.textSecondary, fontSize = 13.sp)
        }
    }
}

@Composable
private fun ActiveCallContent(
    session: ActiveCallSession,
    viewModel: PhoneViewModel,
    onOpenKeypad: () -> Unit,
    onOpenAudioRoute: () -> Unit,
    onEndCall: () -> Unit
) {
    val colors = PhoneTheme.colors

    Column(
        modifier = Modifier
            .testTag("active_call_screen")
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Header: Caller Name, Number, Duration
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            if (session.isHeld) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(colors.warning.copy(alpha = 0.2f))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "CALL ON HOLD",
                        color = colors.warning,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            Text(
                text = session.callerName,
                color = colors.textPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = session.callerNumber,
                color = colors.textSecondary,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = session.durationString,
                color = if (session.isHeld) colors.warning else colors.secondary,
                style = PhoneSpecialTypography.CallDuration,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Live Audio Waveform and Avatar
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            PhoneContactAvatar(
                initials = session.avatarInitials,
                size = AvatarSize.LARGE,
                gradient = PhoneGradients.VioletPulse
            )

            Spacer(modifier = Modifier.height(18.dp))

            PhoneWaveformVisualizer(
                isMuted = session.isMuted,
                isHeld = session.isHeld
            )
        }

        // Active Call Controls (Mute, Keypad, Speaker, Audio Route, Hold, Add Call)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.9f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PhoneActionIconButton(
                    icon = if (session.isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                    label = if (session.isMuted) "Unmute" else "Mute",
                    isActive = session.isMuted,
                    onClick = { viewModel.toggleMute() },
                    testTag = "call_control_mute"
                )

                PhoneActionIconButton(
                    icon = Icons.Default.Dialpad,
                    label = "Keypad",
                    isActive = false,
                    onClick = onOpenKeypad,
                    testTag = "call_control_keypad"
                )

                PhoneActionIconButton(
                    icon = Icons.Default.VolumeUp,
                    label = "Speaker",
                    isActive = session.isSpeakerOn,
                    onClick = { viewModel.toggleSpeaker() },
                    testTag = "call_control_speaker"
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(0.9f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PhoneActionIconButton(
                    icon = when (session.audioRoute) {
                        AudioRoute.EARPIECE -> Icons.Default.Hearing
                        AudioRoute.SPEAKER -> Icons.Default.VolumeUp
                        AudioRoute.BLUETOOTH -> Icons.Default.Bluetooth
                    },
                    label = "Audio",
                    isActive = session.audioRoute != AudioRoute.EARPIECE,
                    onClick = onOpenAudioRoute,
                    testTag = "call_control_audio"
                )

                PhoneActionIconButton(
                    icon = if (session.isHeld) Icons.Default.PlayArrow else Icons.Default.Pause,
                    label = if (session.isHeld) "Resume" else "Hold",
                    isActive = session.isHeld,
                    onClick = { viewModel.toggleHold() },
                    testTag = "call_control_hold"
                )

                PhoneActionIconButton(
                    icon = Icons.Default.PersonAdd,
                    label = "Add Call",
                    isActive = false,
                    onClick = { viewModel.showToast("Add Call / Conference simulated") },
                    testTag = "call_control_add"
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // End Call Button
            PhoneEndCallButton(
                onClick = onEndCall,
                size = 72.dp,
                label = "End Call",
                testTag = "active_end_call_button"
            )
        }
    }
}

@Composable
private fun InCallKeypadContent(
    session: ActiveCallSession,
    onDigitClick: (Char) -> Unit,
    onClose: () -> Unit
) {
    val colors = PhoneTheme.colors

    val keypadDigits = listOf(
        listOf('1', '2', '3'),
        listOf('4', '5', '6'),
        listOf('7', '8', '9'),
        listOf('*', '0', '#')
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "In Call: ${session.callerName} (${session.durationString})",
                color = colors.secondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = session.dtmfDigits.ifEmpty { "Touch Tones (DTMF)" },
                color = if (session.dtmfDigits.isEmpty()) colors.textMuted else colors.textPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            keypadDigits.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(0.85f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    row.forEach { char ->
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(colors.surfaceSecondary)
                                .border(1.dp, colors.outline.copy(alpha = 0.5f), CircleShape)
                                .clickable { onDigitClick(char) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char.toString(),
                                color = colors.textPrimary,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(colors.surfaceSecondary)
                .clickable(onClick = onClose)
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Return to Call",
                color = colors.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CallEndedContent(session: ActiveCallSession) {
    val colors = PhoneTheme.colors

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PhoneContactAvatar(
            initials = session.avatarInitials,
            size = AvatarSize.HERO,
            gradient = PhoneGradients.Monochrome
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Call Ended",
            color = colors.textPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "${session.callerName} • ${session.durationString}",
            color = colors.textSecondary,
            fontSize = 14.sp
        )
    }
}



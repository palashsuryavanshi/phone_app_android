package com.phone.app.feature.calling

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phone.app.core.designsystem.artwork.PhoneCallArtwork
import com.phone.app.core.designsystem.components.IncomingCallButton
import com.phone.app.core.platform.telecom.CallStateManager
import com.phone.app.core.platform.telecom.CallUiState
import com.phone.app.core.platform.telecom.PhoneInCallService
import kotlinx.coroutines.launch

@Composable
fun IncomingCallScreen(
    phoneNumber: String,
    callerName: String?,
    onAnswer: () -> Unit,
    onDecline: () -> Unit,
    onMessage: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val service = PhoneInCallService.instance

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            PhoneCallArtwork(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp),
                accentColor = Color(0xFF10B981),
                sizeDp = 240.dp
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Incoming Call",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = callerName ?: phoneNumber,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = phoneNumber,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
        ) {
            IncomingCallButton(
                text = "Decline",
                modifier = Modifier.weight(1f),
                onClick = {
                    service?.reject()
                    onDecline()
                },
                isAccept = false
            )
            IncomingCallButton(
                text = "Answer",
                modifier = Modifier.weight(1f),
                onClick = {
                    service?.answer()
                    onAnswer()
                },
                isAccept = true
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        androidx.compose.material3.TextButton(
            onClick = { onMessage() },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                text = "Message",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Composable
fun CompactIncomingCallOverlay(
    phoneNumber: String,
    callerName: String?,
    onAnswer: () -> Unit,
    onDecline: () -> Unit,
    modifier: Modifier = Modifier
) {
    val service = PhoneInCallService.instance

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
    ) {
        androidx.compose.material3.Surface(
            modifier = Modifier.size(44.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = CircleShape
        ) {
            Text(
                text = (callerName ?: phoneNumber).take(2).uppercase(),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                ),
                color = Color.White
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Incoming Call",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = callerName ?: phoneNumber,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = phoneNumber,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
        ) {
            IncomingCallButton(
                text = "Decline",
                modifier = Modifier.width(120.dp).height(48.dp),
                onClick = {
                    service?.reject()
                    onDecline()
                },
                isAccept = false
            )
            IncomingCallButton(
                text = "Answer",
                modifier = Modifier.width(120.dp).height(48.dp),
                onClick = {
                    service?.answer()
                    onAnswer()
                },
                isAccept = true
            )
        }
    }
}

@Composable
fun OutgoingCallScreen(
    phoneNumber: String,
    callerName: String?,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val service = PhoneInCallService.instance

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            PhoneCallArtwork(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp),
                accentColor = Color(0xFF3B82F6),
                sizeDp = 240.dp
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Calling…",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = callerName ?: phoneNumber,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = phoneNumber,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        androidx.compose.material3.Button(
            onClick = {
                service?.hangup()
                onCancel()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 32.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = Color.White
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(28.dp)
        ) {
            Text(
                text = "Cancel",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                )
            )
        }
    }
}

@Composable
fun ActiveCallScreen(
    phoneNumber: String,
    callerName: String?,
    onEnd: () -> Unit,
    onMute: (Boolean) -> Unit,
    onSpeaker: (Boolean) -> Unit,
    onHold: (Boolean) -> Unit,
    onKeypad: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val service = PhoneInCallService.instance
    var isMuted by remember { mutableStateOf(false) }
    var isSpeaker by remember { mutableStateOf(false) }
    var isOnHold by remember { mutableStateOf(false) }
    var callStartTime by remember { mutableStateOf(System.currentTimeMillis()) }

    val durationMillis = System.currentTimeMillis() - callStartTime
    val minutes = durationMillis / 60000
    val seconds = (durationMillis % 60000) / 1000
    val durationText = "${String.format("%02d", minutes)}:${String.format("%02d", seconds)}"

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            PhoneCallArtwork(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp),
                accentColor = Color(0xFF7565FF),
                sizeDp = 200.dp
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = callerName ?: phoneNumber,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = durationText,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
        ) {
            ActiveCallControl(
                label = if (isMuted) "Unmute" else "Mute",
                isActive = isMuted,
                onClick = {
                    isMuted = !isMuted
                    service?.toggleMute()
                    onMute(isMuted)
                },
                activeColor = MaterialTheme.colorScheme.error
            )
            ActiveCallControl(
                label = if (isSpeaker) "Earpiece" else "Speaker",
                isActive = isSpeaker,
                onClick = {
                    isSpeaker = !isSpeaker
                    service?.toggleSpeaker()
                    onSpeaker(isSpeaker)
                },
                activeColor = MaterialTheme.colorScheme.primary
            )
            ActiveCallControl(
                label = "Keypad",
                isActive = false,
                onClick = { onKeypad() },
                activeColor = MaterialTheme.colorScheme.secondary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
        ) {
            ActiveCallControl(
                label = if (isOnHold) "Resume" else "Hold",
                isActive = isOnHold,
                onClick = {
                    isOnHold = !isOnHold
                    service?.hold()
                    onHold(isOnHold)
                },
                activeColor = MaterialTheme.colorScheme.error
            )
            ActiveCallControl(
                label = "Add Call",
                isActive = false,
                onClick = { /* Add call */ },
                activeColor = MaterialTheme.colorScheme.secondary
            )
            ActiveCallControl(
                label = "More",
                isActive = false,
                onClick = { /* More */ },
                activeColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        androidx.compose.material3.Button(
            onClick = {
                service?.hangup()
                onEnd()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 32.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = Color.White
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(30.dp)
        ) {
            Text(
                text = "End Call",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                )
            )
        }
    }
}

@Composable
private fun ActiveCallControl(
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
    activeColor: Color
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = Modifier.width(120.dp).height(56.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = if (isActive) activeColor.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceContainer,
            contentColor = if (isActive) activeColor else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
            ),
            color = if (isActive) activeColor else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

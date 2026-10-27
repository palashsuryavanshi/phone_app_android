package com.phone.app.feature.calling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.phone.app.core.designsystem.theme.PhoneTheme
import com.phone.app.core.designsystem.theme.PhoneThemeManager
import com.phone.app.core.platform.telecom.CallStateManager
import com.phone.app.core.platform.telecom.CallUiState
import com.phone.app.core.platform.telecom.PhoneInCallService

class CallActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val config by PhoneThemeManager.config.collectAsState()
            PhoneTheme(config = config) {
                CallScreenContent()
            }
        }
    }
}

@Composable
fun CallScreenContent() {
    val state by CallStateManager.state.collectAsState()
    val service = PhoneInCallService.instance

    when (val s = state) {
        is CallUiState.Idle -> {
            androidx.compose.material3.Text(
                text = "No active call",
                style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        is CallUiState.Incoming -> {
            IncomingCallScreen(
                phoneNumber = s.number,
                callerName = s.displayName,
                onAnswer = {
                    CallStateManager.setActive(
                        call = s.call,
                        number = s.number,
                        name = s.displayName,
                        muted = false,
                        speaker = false,
                        hold = false,
                        state = 2
                    )
                },
                onDecline = {
                    CallStateManager.setIdle()
                },
                onMessage = { /* Message action */ }
            )
        }
        is CallUiState.Outgoing -> {
            OutgoingCallScreen(
                phoneNumber = s.number,
                callerName = s.displayName,
                onCancel = {
                    CallStateManager.setIdle()
                }
            )
        }
        is CallUiState.Active -> {
            ActiveCallScreen(
                phoneNumber = s.number,
                callerName = s.displayName,
                onEnd = {
                    CallStateManager.setIdle()
                },
                onMute = { isMuted -> },
                onSpeaker = { isSpeaker -> },
                onHold = { isOnHold -> },
                onKeypad = { /* Show DTMF keypad */ }
            )
        }
    }
}

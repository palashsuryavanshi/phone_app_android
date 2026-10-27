package com.phone.app.core.platform.telecom

import android.telecom.Call
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface CallUiState {
    data object Idle : CallUiState
    data class Incoming(
        val call: Call,
        val number: String,
        val displayName: String?
    ) : CallUiState
    data class Outgoing(
        val call: Call,
        val number: String,
        val displayName: String?
    ) : CallUiState
    data class Active(
        val call: Call,
        val number: String,
        val displayName: String?,
        val isMuted: Boolean,
        val isSpeaker: Boolean,
        val isOnHold: Boolean,
        val state: Int
    ) : CallUiState
}

object CallStateManager {
    private val _state = MutableStateFlow<CallUiState>(CallUiState.Idle)
    val state: StateFlow<CallUiState> = _state.asStateFlow()

    fun setIncoming(call: Call, number: String, name: String?) {
        _state.value = CallUiState.Incoming(call, number, name)
    }

    fun setOutgoing(call: Call, number: String, name: String?) {
        _state.value = CallUiState.Outgoing(call, number, name)
    }

    fun setActive(
        call: Call,
        number: String,
        name: String?,
        muted: Boolean,
        speaker: Boolean,
        hold: Boolean,
        state: Int
    ) {
        _state.value = CallUiState.Active(call, number, name, muted, speaker, hold, state)
    }

    fun setIdle() {
        _state.value = CallUiState.Idle
    }
}
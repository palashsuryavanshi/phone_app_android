package com.phone.app.core.platform.telecom

import android.content.Intent
import android.os.Bundle
import android.telecom.Call
import android.telecom.CallAudioState
import android.telecom.InCallService
import android.telecom.VideoProfile
import com.phone.app.feature.calling.CallActivity

class PhoneInCallService : InCallService() {

    companion object {
        @Volatile
        var instance: PhoneInCallService? = null
            private set
    }

    private val callCallbacks = mutableMapOf<Call, Call.Callback>()

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun onDestroy() {
        instance = null
        super.onDestroy()
    }

    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        val callback = object : Call.Callback() {
            override fun onStateChanged(call: Call, state: Int) {
                updateState(call)
            }
            override fun onDetailsChanged(call: Call, details: Call.Details) {
                updateState(call)
            }
        }
        callCallbacks[call] = callback
        call.registerCallback(callback)
        updateState(call)

        // Launch in-call UI
        val intent = Intent(this, CallActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
    }

    override fun onCallRemoved(call: Call) {
        super.onCallRemoved(call)
        callCallbacks[call]?.let { call.unregisterCallback(it) }
        callCallbacks.remove(call)
        CallStateManager.setIdle()
    }

    private fun updateState(call: Call) {
        val details = call.details
        val number = details?.handle?.schemeSpecificPart ?: ""
        val name = details?.callerDisplayName?.toString()
        val state = call.state
        val audioState = callAudioState
        val muted = audioState?.isMuted ?: false
        val speaker = audioState?.route == CallAudioState.ROUTE_SPEAKER
        val hold = state == Call.STATE_HOLDING
        when (state) {
            Call.STATE_RINGING -> CallStateManager.setIncoming(call, number, name)
            Call.STATE_DIALING, Call.STATE_CONNECTING, Call.STATE_SELECT_PHONE_ACCOUNT -> CallStateManager.setOutgoing(call, number, name)
            else -> CallStateManager.setActive(call, number, name, muted, speaker, hold, state)
        }
    }

    override fun onCallAudioStateChanged(callAudioState: CallAudioState?) {
        super.onCallAudioStateChanged(callAudioState)
        // update active call state if any
        calls?.firstOrNull()?.let { updateState(it) }
    }

    // Actions
    fun answer() { calls?.firstOrNull()?.answer(VideoProfile.STATE_AUDIO_ONLY) }
    fun reject() { calls?.firstOrNull()?.reject(false, null) }
    fun hangup() { calls?.firstOrNull()?.disconnect() }
    fun toggleMute() {
        val muted = callAudioState?.isMuted ?: false
        setMuted(!muted)
    }
    fun toggleSpeaker() {
        val route = if (callAudioState?.route == CallAudioState.ROUTE_SPEAKER) CallAudioState.ROUTE_EARPIECE else CallAudioState.ROUTE_SPEAKER
        setAudioRoute(route)
    }
    fun hold() {
        val call = calls?.firstOrNull() ?: return
        if (call.state == Call.STATE_HOLDING) call.unhold() else call.hold()
    }
    fun sendDtmf(digit: Char) {
        calls?.firstOrNull()?.playDtmfTone(digit)
    }
    fun stopDtmf() {
        calls?.firstOrNull()?.stopDtmfTone()
    }
}


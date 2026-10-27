package com.phone.app.data.model

enum class AudioRoute(val label: String, val iconName: String) {
    EARPIECE("Phone Earpiece", "earpiece"),
    SPEAKER("Speakerphone", "speaker"),
    BLUETOOTH("Bluetooth Audio", "bluetooth")
}

enum class CallStatus {
    IDLE,
    OUTGOING_RINGING,
    INCOMING_RINGING,
    ACTIVE,
    ON_HOLD,
    ENDED
}

data class ActiveCallSession(
    val id: String = "call_curr",
    val callerName: String,
    val callerNumber: String,
    val avatarInitials: String = "NP",
    val status: CallStatus = CallStatus.ACTIVE,
    val durationSeconds: Int = 0,
    val isMuted: Boolean = false,
    val isSpeakerOn: Boolean = false,
    val audioRoute: AudioRoute = AudioRoute.EARPIECE,
    val isHeld: Boolean = false,
    val isVideo: Boolean = false,
    val isSpamSuspect: Boolean = false,
    val spamReason: String? = null,
    val dtmfDigits: String = ""
) {
    val durationString: String
        get() {
            val mins = durationSeconds / 60
            val secs = durationSeconds % 60
            return "%02d:%02d".format(mins, secs)
        }
}


package com.phone.app.data.model

data class Contact(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val secondaryNumber: String? = null,
    val email: String? = null,
    val avatarInitials: String,
    val avatarColorHex: Long = 0xFF7565FF,
    val isFavorite: Boolean = false,
    val group: String = "Personal",
    val note: String = ""
)

enum class CallType {
    INCOMING,
    OUTGOING,
    MISSED,
    BLOCKED_SPAM
}

data class CallLogEntry(
    val id: String,
    val contactId: String? = null,
    val name: String?,
    val phoneNumber: String,
    val callType: CallType,
    val timestampFormatted: String,
    val durationSeconds: Int,
    val simSlot: Int = 1,
    val isSpamSuspected: Boolean = false
) {
    val durationFormatted: String
        get() {
            if (durationSeconds <= 0) return "Missed"
            val mins = durationSeconds / 60
            val secs = durationSeconds % 60
            return if (mins > 0) "${mins}m ${secs}s" else "${secs}s"
        }
}

data class BlockedNumber(
    val id: String,
    val number: String,
    val reason: String = "User Blocked",
    val addedDate: String
)

data class SpamEvent(
    val id: String,
    val number: String,
    val reportedName: String,
    val reportsCount: Int,
    val riskLevel: String, // "High Risk", "Suspected Telemarketer", "Robocall"
    val timestamp: String,
    val actionTaken: String = "Call Silenced & Screened"
)


package com.phone.app.core.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CallLogFormatter {
    fun formatTimestamp(millis: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - millis
        return when {
            diff < 24 * 60 * 60 * 1000L -> SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(millis))
            diff < 7 * 24 * 60 * 60 * 1000L -> SimpleDateFormat("EEE HH:mm", Locale.getDefault()).format(Date(millis))
            else -> SimpleDateFormat("MMM d, HH:mm", Locale.getDefault()).format(Date(millis))
        }
    }

    fun formatDuration(seconds: Long): String {
        if (seconds <= 0) return ""
        val m = seconds / 60
        val s = seconds % 60
        return if (m > 0) "${m}m ${s}s" else "${s}s"
    }

    fun typeLabel(type: Int): String = when (type) {
        android.provider.CallLog.Calls.INCOMING_TYPE -> "Incoming"
        android.provider.CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
        android.provider.CallLog.Calls.MISSED_TYPE -> "Missed"
        android.provider.CallLog.Calls.REJECTED_TYPE -> "Rejected"
        android.provider.CallLog.Calls.BLOCKED_TYPE -> "Blocked"
        else -> "Unknown"
    }
}

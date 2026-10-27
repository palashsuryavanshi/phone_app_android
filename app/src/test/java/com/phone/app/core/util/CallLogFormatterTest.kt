package com.phone.app.core.util

import org.junit.Assert.assertEquals
import org.junit.Test

class CallLogFormatterTest {
    @Test
    fun typeLabel_missed() {
        assertEquals("Missed", CallLogFormatter.typeLabel(android.provider.CallLog.Calls.MISSED_TYPE))
    }

    @Test
    fun formatDuration_zero() {
        assertEquals("", CallLogFormatter.formatDuration(0))
    }

    @Test
    fun formatDuration_seconds() {
        assertEquals("45s", CallLogFormatter.formatDuration(45))
    }
}

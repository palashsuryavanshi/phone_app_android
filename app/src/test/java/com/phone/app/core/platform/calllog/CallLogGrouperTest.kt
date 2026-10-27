package com.phone.app.core.platform.calllog

import org.junit.Assert.assertEquals
import org.junit.Test

class CallLogGrouperTest {
    private fun rec(num: String, date: Long, type: Int = android.provider.CallLog.Calls.OUTGOING_TYPE) =
        CallRecord(id = date.toString(), number = num, cachedName = null, type = type, date = date, duration = 0)

    @Test
    fun groupsByNumber() {
        val records = listOf(rec("123", 100), rec("123", 200), rec("456", 300))
        val grouped = CallLogGrouper.group(records)
        assertEquals(2, grouped.size)
        val g123 = grouped.first { it.number == "123" }
        assertEquals(2, g123.count)
    }

    @Test
    fun sortsByLatestDate() {
        val records = listOf(rec("123", 100), rec("456", 300))
        val grouped = CallLogGrouper.group(records)
        assertEquals("456", grouped.first().number)
    }
}

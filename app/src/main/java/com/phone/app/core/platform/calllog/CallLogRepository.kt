package com.phone.app.core.platform.calllog

import android.content.Context
import android.provider.CallLog

data class CallRecord(
    val id: String,
    val number: String,
    val cachedName: String?,
    val type: Int,
    val date: Long,
    val duration: Long,
    val simId: String? = null,
    val isNew: Boolean = false
)

class CallLogRepository(private val context: Context) {
    fun getAll(): List<CallRecord> {
        val list = mutableListOf<CallRecord>()
        val uri = CallLog.Calls.CONTENT_URI
        val projection = arrayOf(
            CallLog.Calls._ID,
            CallLog.Calls.NUMBER,
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.TYPE,
            CallLog.Calls.DATE,
            CallLog.Calls.DURATION,
            CallLog.Calls.PHONE_ACCOUNT_ID,
            CallLog.Calls.NEW
        )
        try {
            context.contentResolver.query(uri, projection, null, null, "${CallLog.Calls.DATE} DESC")?.use { c ->
                val idIdx = c.getColumnIndexOrThrow(CallLog.Calls._ID)
                val numIdx = c.getColumnIndexOrThrow(CallLog.Calls.NUMBER)
                val nameIdx = c.getColumnIndex(CallLog.Calls.CACHED_NAME)
                val typeIdx = c.getColumnIndexOrThrow(CallLog.Calls.TYPE)
                val dateIdx = c.getColumnIndexOrThrow(CallLog.Calls.DATE)
                val durIdx = c.getColumnIndexOrThrow(CallLog.Calls.DURATION)
                val simIdx = c.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID)
                val newIdx = c.getColumnIndex(CallLog.Calls.NEW)
                while (c.moveToNext()) {
                    list.add(
                        CallRecord(
                            id = c.getString(idIdx),
                            number = c.getString(numIdx) ?: "",
                            cachedName = if (nameIdx >= 0) c.getString(nameIdx) else null,
                            type = c.getInt(typeIdx),
                            date = c.getLong(dateIdx),
                            duration = c.getLong(durIdx),
                            simId = if (simIdx >= 0) c.getString(simIdx) else null,
                            isNew = if (newIdx >= 0) c.getInt(newIdx) == 1 else false
                        )
                    )
                }
            }
        } catch (_: SecurityException) {}
        return list
    }

    fun delete(id: String): Boolean {
        return try {
            val uri = android.net.Uri.withAppendedPath(CallLog.Calls.CONTENT_URI, id)
            context.contentResolver.delete(uri, null, null) > 0
        } catch (_: Exception) {
            false
        }
    }
}

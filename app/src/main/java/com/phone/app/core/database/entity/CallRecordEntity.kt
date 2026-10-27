package com.phone.app.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "call_records")
data class CallRecordEntity(
    @PrimaryKey
    val id: String,
    val phoneNumber: String,
    val contactId: String?,
    val contactName: String?,
    val callType: String, // INCOMING, OUTGOING, MISSED, VOICEMAIL
    val durationMillis: Long,
    val timestampMillis: Long,
    val isFavorite: Boolean = false
)

package com.phone.app.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey
    val id: String,
    val displayName: String,
    val givenName: String?,
    val familyName: String?,
    val phoneNumbers: String?, // comma-separated normalized numbers
    val emailAddresses: String?,
    val photoUri: String?,
    val isFavorite: Boolean = false,
    val lastCalled: Long? = null,
    val callCount: Int = 0
)

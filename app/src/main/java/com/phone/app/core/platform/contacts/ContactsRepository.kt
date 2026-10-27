package com.phone.app.core.platform.contacts

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract

data class ContactMatch(
    val id: String,
    val displayName: String,
    val phoneNumber: String
)

data class ContactSummary(
    val id: String,
    val displayName: String,
    val primaryNumber: String?,
    val photoUri: String? = null
)

data class ContactDetail(
    val id: String,
    val displayName: String,
    val phoneNumbers: List<String>,
    val emails: List<String>,
    val organization: String?,
    val photoUri: String?
)

class ContactsRepository(private val context: Context) {
    fun getAll(): List<ContactSummary> {
        val list = mutableListOf<ContactSummary>()
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.PHOTO_URI
        )
        try {
            context.contentResolver.query(uri, projection, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)?.use { c ->
                val idIdx = c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                val nameIdx = c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val numIdx = c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val photoIdx = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)
                val seen = mutableSetOf<String>()
                while (c.moveToNext()) {
                    val id = c.getString(idIdx)
                    if (seen.contains(id)) continue
                    seen.add(id)
                    list.add(ContactSummary(id, c.getString(nameIdx) ?: "", c.getString(numIdx), if (photoIdx >= 0) c.getString(photoIdx) else null))
                }
            }
        } catch (_: SecurityException) {}
        return list
    }

    fun search(query: String): List<ContactSummary> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return getAll()
        return getAll().filter { c ->
            c.displayName.lowercase().contains(q) ||
                (c.primaryNumber?.replace(Regex("[^0-9]"), "")?.contains(q.replace(Regex("[^0-9]"), "")) == true) ||
                c.primaryNumber?.lowercase()?.contains(q) == true
        }
    }

    fun getDetail(id: String): ContactDetail? {
        var name = ""
        var org: String? = null
        var photo: String? = null
        val phones = mutableListOf<String>()
        val emails = mutableListOf<String>()
        // name + org + photo from contacts
        val contactUri = android.net.Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, id)
        context.contentResolver.query(contactUri, arrayOf(ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.PHOTO_URI), null, null, null)?.use { c ->
            if (c.moveToFirst()) {
                name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)) ?: ""
                val photoIdx = c.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)
                if (photoIdx >= 0) photo = c.getString(photoIdx)
            }
        }
        // phones
        context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER), "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID}=?", arrayOf(id), null)?.use { c ->
            val idx = c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (c.moveToNext()) phones.add(c.getString(idx))
        }
        // emails
        context.contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, arrayOf(ContactsContract.CommonDataKinds.Email.ADDRESS), "${ContactsContract.CommonDataKinds.Email.CONTACT_ID}=?", arrayOf(id), null)?.use { c ->
            val idx = c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.ADDRESS)
            while (c.moveToNext()) emails.add(c.getString(idx))
        }
        // org
        context.contentResolver.query(ContactsContract.Data.CONTENT_URI, arrayOf(ContactsContract.CommonDataKinds.Organization.COMPANY), "${ContactsContract.Data.CONTACT_ID}=? AND ${ContactsContract.Data.MIMETYPE}=?", arrayOf(id, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE), null)?.use { c ->
            val idx = c.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY)
            if (c.moveToFirst() && idx >= 0) org = c.getString(idx)
        }
        if (name.isBlank() && phones.isEmpty()) return null
        return ContactDetail(id, name, phones.distinct(), emails.distinct(), org, photo)
    }

    fun searchByNumber(query: String): List<ContactMatch> {
        return search(query).map { ContactMatch(it.id, it.displayName, it.primaryNumber ?: "") }
    }
}

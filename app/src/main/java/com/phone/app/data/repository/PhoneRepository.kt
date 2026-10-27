package com.phone.app.data.repository

import com.phone.app.data.model.BlockedNumber
import com.phone.app.data.model.CallLogEntry
import com.phone.app.data.model.CallType
import com.phone.app.data.model.Contact
import com.phone.app.data.model.SpamEvent
import com.phone.app.ui.theme.AccentPalette
import com.phone.app.ui.theme.ShapePreset
import com.phone.app.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class PhoneRepository {

    private val _contacts = MutableStateFlow<List<Contact>>(initialContacts())
    val contacts: StateFlow<List<Contact>> = _contacts.asStateFlow()

    private val _callLogs = MutableStateFlow<List<CallLogEntry>>(initialCallLogs())
    val callLogs: StateFlow<List<CallLogEntry>> = _callLogs.asStateFlow()

    private val _blockedNumbers = MutableStateFlow<List<BlockedNumber>>(initialBlockedNumbers())
    val blockedNumbers: StateFlow<List<BlockedNumber>> = _blockedNumbers.asStateFlow()

    private val _spamEvents = MutableStateFlow<List<SpamEvent>>(initialSpamEvents())
    val spamEvents: StateFlow<List<SpamEvent>> = _spamEvents.asStateFlow()

    // Settings & Personalization State
    val themeMode = MutableStateFlow(ThemeMode.DARK)
    val accentPalette = MutableStateFlow(AccentPalette.VIOLET)
    val shapePreset = MutableStateFlow(ShapePreset.EXPRESSIVE)
    val selectedWallpaperIndex = MutableStateFlow(0) // 0: Aurora Night, 1: Violet Pulse, 2: Arctic Flow, 3: Sunset Ember, 4: Emerald Night, 5: Monochrome
    val spamProtectionEnabled = MutableStateFlow(true)
    val appLockEnabled = MutableStateFlow(false)
    val appLockPin = MutableStateFlow("1234")
    val selectedRingtone = MutableStateFlow("Celestial Chime")
    val selectedVibrationPattern = MutableStateFlow("Pulse Sync") // Pulse Sync, Rapid Staccato, Gentle Wave
    val dtmfKeypadTones = MutableStateFlow(true)
    val dualSimDefault = MutableStateFlow(1) // SIM 1 or SIM 2

    // CRUD Contacts
    fun addContact(name: String, phone: String, email: String = "", group: String = "Personal"): Contact {
        val initials = name.split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .map { it.first().uppercaseChar() }
            .joinToString("")
            .ifEmpty { "C" }
        
        val newContact = Contact(
            id = UUID.randomUUID().toString(),
            name = name,
            phoneNumber = phone,
            email = email.ifBlank { null },
            avatarInitials = initials,
            avatarColorHex = 0xFF7565FF,
            group = group
        )
        _contacts.value = listOf(newContact) + _contacts.value
        return newContact
    }

    fun updateContact(contact: Contact) {
        _contacts.value = _contacts.value.map { if (it.id == contact.id) contact else it }
    }

    fun deleteContact(contactId: String) {
        _contacts.value = _contacts.value.filterNot { it.id == contactId }
    }

    fun toggleFavorite(contactId: String) {
        _contacts.value = _contacts.value.map {
            if (it.id == contactId) it.copy(isFavorite = !it.isFavorite) else it
        }
    }

    // Call Log Management
    fun addCallLog(name: String?, number: String, type: CallType, durationSeconds: Int, sim: Int = 1) {
        val entry = CallLogEntry(
            id = UUID.randomUUID().toString(),
            name = name,
            phoneNumber = number,
            callType = type,
            timestampFormatted = "Just now",
            durationSeconds = durationSeconds,
            simSlot = sim
        )
        _callLogs.value = listOf(entry) + _callLogs.value
    }

    fun deleteCallLog(id: String) {
        _callLogs.value = _callLogs.value.filterNot { it.id == id }
    }

    fun clearAllCallLogs() {
        _callLogs.value = emptyList()
    }

    // Block List
    fun blockNumber(number: String, reason: String = "User Blocked") {
        if (_blockedNumbers.value.none { it.number == number }) {
            val entry = BlockedNumber(
                id = UUID.randomUUID().toString(),
                number = number,
                reason = reason,
                addedDate = "Today"
            )
            _blockedNumbers.value = listOf(entry) + _blockedNumbers.value
        }
    }

    fun unblockNumber(id: String) {
        _blockedNumbers.value = _blockedNumbers.value.filterNot { it.id == id }
    }

    // Spam Protection Reporting
    fun reportSpam(number: String, reportedName: String, reason: String) {
        val event = SpamEvent(
            id = UUID.randomUUID().toString(),
            number = number,
            reportedName = reportedName.ifBlank { "Suspicious Telemarketer" },
            reportsCount = 142,
            riskLevel = "High Risk",
            timestamp = "Just now",
            actionTaken = "Flagged & Blocked"
        )
        _spamEvents.value = listOf(event) + _spamEvents.value
        blockNumber(number, reason)
    }

    private fun initialContacts(): List<Contact> = listOf(
        Contact(
            id = "c1",
            name = "Elena Rostova",
            phoneNumber = "+1 (555) 839-2041",
            secondaryNumber = "+1 (555) 482-1099",
            email = "elena.rostova@email.io",
            avatarInitials = "ER",
            avatarColorHex = 0xFF7565FF,
            isFavorite = true,
            group = "Favorites",
            note = "Lead Design Architect at Prism"
        ),
        Contact(
            id = "c2",
            name = "Marcus Chen",
            phoneNumber = "+1 (555) 624-9102",
            email = "marcus.chen@techcore.com",
            avatarInitials = "MC",
            avatarColorHex = 0xFF20D6CB,
            isFavorite = true,
            group = "Work",
            note = "Product engineering partner"
        ),
        Contact(
            id = "c3",
            name = "Sofia Vance",
            phoneNumber = "+1 (555) 390-4819",
            email = "sofia.v@creative.studio",
            avatarInitials = "SV",
            avatarColorHex = 0xFFEF63C8,
            isFavorite = true,
            group = "Friends",
            note = "Graphic designer & photographer"
        ),
        Contact(
            id = "c4",
            name = "Aiden Thorne",
            phoneNumber = "+1 (555) 712-4091",
            email = "aiden.thorne@ambient.co",
            avatarInitials = "AT",
            avatarColorHex = 0xFFFF996B,
            isFavorite = false,
            group = "Work"
        ),
        Contact(
            id = "c5",
            name = "Dr. Sarah Lin",
            phoneNumber = "+1 (555) 901-2384",
            email = "dr.lin@valleyclinic.org",
            avatarInitials = "SL",
            avatarColorHex = 0xFF39D9A5,
            isFavorite = true,
            group = "Personal"
        ),
        Contact(
            id = "c6",
            name = "Kaelen Voss",
            phoneNumber = "+1 (555) 441-8923",
            avatarInitials = "KV",
            avatarColorHex = 0xFF8B7CFF,
            isFavorite = false,
            group = "Friends"
        ),
        Contact(
            id = "c7",
            name = "Maya Patel",
            phoneNumber = "+1 (555) 238-7109",
            email = "maya.patel@horizon.net",
            avatarInitials = "MP",
            avatarColorHex = 0xFF20D6CB,
            isFavorite = false,
            group = "Work"
        ),
        Contact(
            id = "c8",
            name = "Lucas Miller",
            phoneNumber = "+1 (555) 192-8347",
            avatarInitials = "LM",
            avatarColorHex = 0xFF7565FF,
            isFavorite = false,
            group = "Family"
        )
    )

    private fun initialCallLogs(): List<CallLogEntry> = listOf(
        CallLogEntry(
            id = "log1",
            contactId = "c1",
            name = "Elena Rostova",
            phoneNumber = "+1 (555) 839-2041",
            callType = CallType.INCOMING,
            timestampFormatted = "12m ago",
            durationSeconds = 248,
            simSlot = 1
        ),
        CallLogEntry(
            id = "log2",
            contactId = "c2",
            name = "Marcus Chen",
            phoneNumber = "+1 (555) 624-9102",
            callType = CallType.OUTGOING,
            timestampFormatted = "1h ago",
            durationSeconds = 114,
            simSlot = 1
        ),
        CallLogEntry(
            id = "log3",
            contactId = null,
            name = "Unknown Caller",
            phoneNumber = "+1 (555) 983-2019",
            callType = CallType.MISSED,
            timestampFormatted = "3h ago",
            durationSeconds = 0,
            simSlot = 2
        ),
        CallLogEntry(
            id = "log4",
            contactId = "c3",
            name = "Sofia Vance",
            phoneNumber = "+1 (555) 390-4819",
            callType = CallType.INCOMING,
            timestampFormatted = "Yesterday, 6:40 PM",
            durationSeconds = 480,
            simSlot = 1
        ),
        CallLogEntry(
            id = "log5",
            contactId = null,
            name = "Suspected Telemarketer",
            phoneNumber = "+1 (800) 230-9182",
            callType = CallType.BLOCKED_SPAM,
            timestampFormatted = "Yesterday, 3:15 PM",
            durationSeconds = 0,
            simSlot = 1,
            isSpamSuspected = true
        ),
        CallLogEntry(
            id = "log6",
            contactId = "c5",
            name = "Dr. Sarah Lin",
            phoneNumber = "+1 (555) 901-2384",
            callType = CallType.OUTGOING,
            timestampFormatted = "Oct 25, 11:20 AM",
            durationSeconds = 85,
            simSlot = 1
        )
    )

    private fun initialBlockedNumbers(): List<BlockedNumber> = listOf(
        BlockedNumber("b1", "+1 (800) 230-9182", "Spam Telemarketing", "Oct 20, 2026"),
        BlockedNumber("b2", "+1 (888) 555-0199", "Robocall Insurance", "Oct 15, 2026"),
        BlockedNumber("b3", "+1 (555) 000-1122", "Harassment / Silent", "Oct 08, 2026")
    )

    private fun initialSpamEvents(): List<SpamEvent> = listOf(
        SpamEvent(
            id = "sp1",
            number = "+1 (800) 230-9182",
            reportedName = "National Survey Corp",
            reportsCount = 384,
            riskLevel = "High Risk",
            timestamp = "Yesterday, 3:15 PM",
            actionTaken = "Silenced & Blocked Automatically"
        ),
        SpamEvent(
            id = "sp2",
            number = "+1 (877) 412-9081",
            reportedName = "Extended Warranty Bot",
            reportsCount = 1290,
            riskLevel = "Extreme Risk",
            timestamp = "Oct 24, 2:48 PM",
            actionTaken = "Network Screen Rejected"
        ),
        SpamEvent(
            id = "sp3",
            number = "+1 (555) 789-0123",
            reportedName = "Unknown Aggressive Sales",
            reportsCount = 67,
            riskLevel = "Moderate Risk",
            timestamp = "Oct 22, 10:04 AM",
            actionTaken = "Flagged in Incoming Notification"
        )
    )
}


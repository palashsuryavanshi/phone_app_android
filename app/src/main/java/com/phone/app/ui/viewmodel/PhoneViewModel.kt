package com.phone.app.ui.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.phone.app.data.model.ActiveCallSession
import com.phone.app.data.model.AudioRoute
import com.phone.app.data.model.CallLogEntry
import com.phone.app.data.model.CallStatus
import com.phone.app.data.model.CallType
import com.phone.app.data.model.Contact
import com.phone.app.data.repository.PhoneRepository
import com.phone.app.ui.theme.AccentPalette
import com.phone.app.ui.theme.ShapePreset
import com.phone.app.ui.theme.ThemeMode
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MainTab {
    KEYPAD,
    RECENTS,
    CONTACTS
}

enum class SubScreen {
    NONE,
    CONTACT_DETAIL,
    ADD_CONTACT,
    EDIT_CONTACT,
    SETTINGS,
    APPEARANCE,
    PRIVACY,
    SPAM,
    SOUNDS,
    CALL_SETTINGS,
    COMPONENT_LIBRARY,
    ABOUT,
    ONBOARDING
}

enum class RecentsFilter {
    ALL,
    MISSED,
    INCOMING,
    OUTGOING
}

enum class ContactsFilter {
    ALL,
    FAVORITES,
    GROUPS
}

class PhoneViewModel(application: Application) : AndroidViewModel(application) {

    val repository = PhoneRepository()

    // Navigation State
    private val _currentTab = MutableStateFlow(MainTab.KEYPAD)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    private val _currentSubScreen = MutableStateFlow(SubScreen.NONE)
    val currentSubScreen: StateFlow<SubScreen> = _currentSubScreen.asStateFlow()

    // Active Contact Selection for Detail / Edit
    private val _selectedContact = MutableStateFlow<Contact?>(null)
    val selectedContact: StateFlow<Contact?> = _selectedContact.asStateFlow()

    // Dialer State
    private val _dialerInput = MutableStateFlow("")
    val dialerInput: StateFlow<String> = _dialerInput.asStateFlow()

    private val _simSlot = MutableStateFlow(1)
    val simSlot: StateFlow<Int> = _simSlot.asStateFlow()

    // Search Query State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Filters
    private val _recentsFilter = MutableStateFlow(RecentsFilter.ALL)
    val recentsFilter: StateFlow<RecentsFilter> = _recentsFilter.asStateFlow()

    private val _contactsFilter = MutableStateFlow(ContactsFilter.ALL)
    val contactsFilter: StateFlow<ContactsFilter> = _contactsFilter.asStateFlow()

    // Active Call Management
    private val _activeCall = MutableStateFlow<ActiveCallSession?>(null)
    val activeCall: StateFlow<ActiveCallSession?> = _activeCall.asStateFlow()

    private var callDurationJob: Job? = null

    // Audio preview playing flag for ringtone selector
    private val _isPlayingSoundPreview = MutableStateFlow(false)
    val isPlayingSoundPreview: StateFlow<Boolean> = _isPlayingSoundPreview.asStateFlow()

    // Transient feedback snackbar
    private val _transientMessage = MutableStateFlow<String?>(null)
    val transientMessage: StateFlow<String?> = _transientMessage.asStateFlow()

    // T9 / Digit Matching Contacts in Keypad
    val dialerMatchingContacts: StateFlow<List<Contact>> = combine(
        repository.contacts,
        _dialerInput
    ) { contacts, query ->
        if (query.isBlank()) emptyList()
        else {
            val digitsOnly = query.filter { it.isDigit() }
            contacts.filter { contact ->
                val phoneDigits = contact.phoneNumber.filter { it.isDigit() }
                phoneDigits.contains(digitsOnly) ||
                        contact.name.contains(query, ignoreCase = true) ||
                        matchesT9(contact.name, digitsOnly)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Recents
    val filteredRecents: StateFlow<List<CallLogEntry>> = combine(
        repository.callLogs,
        _recentsFilter
    ) { logs, filter ->
        when (filter) {
            RecentsFilter.ALL -> logs
            RecentsFilter.MISSED -> logs.filter { it.callType == CallType.MISSED }
            RecentsFilter.INCOMING -> logs.filter { it.callType == CallType.INCOMING }
            RecentsFilter.OUTGOING -> logs.filter { it.callType == CallType.OUTGOING }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Contacts
    val filteredContacts: StateFlow<List<Contact>> = combine(
        repository.contacts,
        _searchQuery,
        _contactsFilter
    ) { contacts, query, filter ->
        var list = when (filter) {
            ContactsFilter.ALL -> contacts
            ContactsFilter.FAVORITES -> contacts.filter { it.isFavorite }
            ContactsFilter.GROUPS -> contacts // will group by category in UI
        }
        if (query.isNotBlank()) {
            list = list.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.phoneNumber.contains(query)
            }
        }
        list.sortedBy { it.name }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Navigation Methods
    fun setTab(tab: MainTab) {
        _currentTab.value = tab
        _currentSubScreen.value = SubScreen.NONE
    }

    fun openSubScreen(sub: SubScreen, contact: Contact? = null) {
        _selectedContact.value = contact
        _currentSubScreen.value = sub
    }

    fun navigateBack(): Boolean {
        if (_currentSubScreen.value != SubScreen.NONE) {
            _currentSubScreen.value = SubScreen.NONE
            return true
        }
        if (_currentTab.value != MainTab.KEYPAD) {
            _currentTab.value = MainTab.KEYPAD
            return true
        }
        return false
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setRecentsFilter(filter: RecentsFilter) {
        _recentsFilter.value = filter
    }

    fun setContactsFilter(filter: ContactsFilter) {
        _contactsFilter.value = filter
    }

    fun toggleSimSlot() {
        _simSlot.value = if (_simSlot.value == 1) 2 else 1
    }

    // Keypad Actions
    fun appendDialerDigit(char: Char) {
        _dialerInput.value += char
        triggerHaptic()
    }

    fun deleteDialerDigit() {
        if (_dialerInput.value.isNotEmpty()) {
            _dialerInput.value = _dialerInput.value.dropLast(1)
            triggerHaptic()
        }
    }

    fun clearDialerDigits() {
        _dialerInput.value = ""
        triggerHaptic()
    }

    // Active Calling Engine
    fun startOutgoingCall(phoneNumber: String, contactName: String? = null) {
        val name = contactName ?: repository.contacts.value.find { it.phoneNumber == phoneNumber }?.name ?: phoneNumber
        val initials = name.split(" ").take(2).mapNotNull { it.firstOrNull()?.uppercaseChar() }.joinToString("").ifEmpty { "NP" }

        val session = ActiveCallSession(
            callerName = name,
            callerNumber = phoneNumber,
            avatarInitials = initials,
            status = CallStatus.OUTGOING_RINGING,
            durationSeconds = 0
        )
        _activeCall.value = session

        // Transition from ringing to active after brief simulated connection delay
        viewModelScope.launch {
            delay(2200)
            if (_activeCall.value?.status == CallStatus.OUTGOING_RINGING) {
                _activeCall.value = _activeCall.value?.copy(status = CallStatus.ACTIVE)
                startCallTimer()
            }
        }
    }

    fun simulateIncomingCall(variant: String = "NORMAL") {
        val (name, number, initials, isSpam, spamReason) = when (variant) {
            "SPAM" -> Tuple5(
                "Suspected Telemarketer",
                "+1 (800) 230-9182",
                "!",
                true,
                "Reported by 380+ users as Aggressive Sales"
            )
            "UNKNOWN" -> Tuple5(
                "Unknown Number",
                "+1 (555) 019-8472",
                "?",
                false,
                null
            )
            else -> Tuple5(
                "Elena Rostova",
                "+1 (555) 839-2041",
                "ER",
                false,
                null
            )
        }

        _activeCall.value = ActiveCallSession(
            callerName = name,
            callerNumber = number,
            avatarInitials = initials,
            status = CallStatus.INCOMING_RINGING,
            isSpamSuspect = isSpam,
            spamReason = spamReason
        )
        triggerVibration("Pulse")
    }

    fun answerCall() {
        val current = _activeCall.value ?: return
        _activeCall.value = current.copy(status = CallStatus.ACTIVE)
        startCallTimer()
        showToast("Call Connected")
    }

    fun declineCall(reason: String = "Call Declined") {
        val current = _activeCall.value ?: return
        repository.addCallLog(
            name = current.callerName,
            number = current.callerNumber,
            type = if (current.isSpamSuspect) CallType.BLOCKED_SPAM else CallType.MISSED,
            durationSeconds = 0,
            sim = _simSlot.value
        )
        _activeCall.value = current.copy(status = CallStatus.ENDED)
        callDurationJob?.cancel()
        viewModelScope.launch {
            delay(1200)
            _activeCall.value = null
        }
        showToast(reason)
    }

    fun endCall() {
        val current = _activeCall.value ?: return
        val duration = current.durationSeconds
        repository.addCallLog(
            name = current.callerName,
            number = current.callerNumber,
            type = CallType.OUTGOING,
            durationSeconds = duration,
            sim = _simSlot.value
        )
        _activeCall.value = current.copy(status = CallStatus.ENDED)
        callDurationJob?.cancel()
        viewModelScope.launch {
            delay(1200)
            _activeCall.value = null
        }
        showToast("Call Ended (${current.durationString})")
    }

    fun toggleMute() {
        _activeCall.value = _activeCall.value?.let { it.copy(isMuted = !it.isMuted) }
    }

    fun toggleSpeaker() {
        _activeCall.value = _activeCall.value?.let {
            val nextSpeaker = !it.isSpeakerOn
            it.copy(
                isSpeakerOn = nextSpeaker,
                audioRoute = if (nextSpeaker) AudioRoute.SPEAKER else AudioRoute.EARPIECE
            )
        }
    }

    fun setAudioRoute(route: AudioRoute) {
        _activeCall.value = _activeCall.value?.copy(
            audioRoute = route,
            isSpeakerOn = route == AudioRoute.SPEAKER
        )
        showToast("Audio routed to ${route.label}")
    }

    fun toggleHold() {
        _activeCall.value = _activeCall.value?.let {
            val nextHeld = !it.isHeld
            it.copy(
                isHeld = nextHeld,
                status = if (nextHeld) CallStatus.ON_HOLD else CallStatus.ACTIVE
            )
        }
    }

    fun appendDtmfInCall(digit: Char) {
        _activeCall.value = _activeCall.value?.let {
            it.copy(dtmfDigits = it.dtmfDigits + digit)
        }
        triggerHaptic()
    }

    private fun startCallTimer() {
        callDurationJob?.cancel()
        callDurationJob = viewModelScope.launch {
            while (_activeCall.value?.status == CallStatus.ACTIVE || _activeCall.value?.status == CallStatus.ON_HOLD) {
                delay(1000)
                _activeCall.value = _activeCall.value?.let {
                    it.copy(durationSeconds = it.durationSeconds + 1)
                }
            }
        }
    }

    // Sound Preview Control
    fun toggleSoundPreview() {
        _isPlayingSoundPreview.value = !_isPlayingSoundPreview.value
        if (_isPlayingSoundPreview.value) {
            triggerVibration("Gentle")
            viewModelScope.launch {
                delay(4000)
                _isPlayingSoundPreview.value = false
            }
        }
    }

    fun showToast(msg: String) {
        _transientMessage.value = msg
        viewModelScope.launch {
            delay(2500)
            if (_transientMessage.value == msg) {
                _transientMessage.value = null
            }
        }
    }

    fun clearToast() {
        _transientMessage.value = null
    }

    // Haptics & Vibration
    fun triggerHaptic() {
        try {
            val ctx = getApplication<Application>()
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = ctx.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                ctx.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(20)
            }
        } catch (_: Exception) {}
    }

    fun triggerVibration(pattern: String) {
        try {
            val ctx = getApplication<Application>()
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = ctx.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                ctx.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val timings = when (pattern) {
                    "Rapid Staccato" -> longArrayOf(0, 80, 80, 80, 80, 80)
                    "Gentle Wave" -> longArrayOf(0, 200, 150, 300)
                    else -> longArrayOf(0, 150, 100, 150, 200) // Pulse Sync
                }
                vibrator?.vibrate(VibrationEffect.createWaveform(timings, -1))
            }
        } catch (_: Exception) {}
    }

    // T9 Matching helper
    private fun matchesT9(name: String, digits: String): Boolean {
        if (digits.isEmpty()) return false
        val t9Map = mapOf(
            'a' to '2', 'b' to '2', 'c' to '2',
            'd' to '3', 'e' to '3', 'f' to '3',
            'g' to '4', 'h' to '4', 'i' to '4',
            'j' to '5', 'k' to '5', 'l' to '5',
            'm' to '6', 'n' to '6', 'o' to '6',
            'p' to '7', 'q' to '7', 'r' to '7', 's' to '7',
            't' to '8', 'u' to '8', 'v' to '8',
            'w' to '9', 'x' to '9', 'y' to '9', 'z' to '9'
        )
        val nameDigits = name.lowercase().map { t9Map[it] ?: ' ' }.joinToString("")
        return nameDigits.contains(digits)
    }

    private data class Tuple5<A, B, C, D, E>(val a: A, val b: B, val c: C, val d: D, val e: E)
}


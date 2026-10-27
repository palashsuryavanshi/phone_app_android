package com.phone.app.feature.dialer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.phone.app.core.platform.contacts.ContactMatch
import com.phone.app.core.platform.contacts.ContactsRepository
import com.phone.app.core.util.PhoneNumberFormatter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DialerUiState(
    val dialString: String = "",
    val displayString: String = "",
    val matches: List<ContactMatch> = emptyList(),
    val isMatching: Boolean = false,
    val error: String? = null
)

class DialerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ContactsRepository(application)
    private val _state = MutableStateFlow(DialerUiState())
    val state: StateFlow<DialerUiState> = _state.asStateFlow()

    private var matchJob: Job? = null

    fun insert(ch: Char) {
        val current = _state.value.dialString
        val next = current + ch
        updateDialString(next)
    }

    fun delete() {
        val current = _state.value.dialString
        if (current.isNotEmpty()) updateDialString(current.dropLast(1))
    }

    fun clear() {
        updateDialString("")
    }

    fun paste(text: String) {
        val sanitized = PhoneNumberFormatter.sanitize(text)
        updateDialString(_state.value.dialString + sanitized)
    }

    private fun updateDialString(value: String) {
        val sanitized = PhoneNumberFormatter.sanitize(value)
        _state.value = _state.value.copy(
            dialString = sanitized,
            displayString = PhoneNumberFormatter.formatForDisplay(sanitized)
        )
        scheduleMatch(sanitized)
    }

    private fun scheduleMatch(query: String) {
        matchJob?.cancel()
        if (query.isBlank()) {
            _state.value = _state.value.copy(matches = emptyList(), isMatching = false)
            return
        }
        matchJob = viewModelScope.launch {
            _state.value = _state.value.copy(isMatching = true)
            delay(150)
            val results = repository.searchByNumber(query)
            _state.value = _state.value.copy(matches = results, isMatching = false)
        }
    }
}

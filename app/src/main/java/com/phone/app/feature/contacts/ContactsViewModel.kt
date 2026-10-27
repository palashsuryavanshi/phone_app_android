package com.phone.app.feature.contacts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.phone.app.core.platform.contacts.ContactSummary
import com.phone.app.core.platform.contacts.ContactsRepository
import com.phone.app.core.platform.contacts.FavoritesRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ContactsUiState(
    val contacts: List<ContactSummary> = emptyList(),
    val filtered: List<ContactSummary> = emptyList(),
    val query: String = "",
    val isLoading: Boolean = true,
    val permissionDenied: Boolean = false,
    val error: String? = null
)

class ContactsViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = ContactsRepository(application)
    private val favRepo = FavoritesRepository(application)
    private val _state = MutableStateFlow(ContactsUiState())
    val state: StateFlow<ContactsUiState> = _state.asStateFlow()
    private var searchJob: Job? = null

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val all = repo.getAll()
                _state.value = _state.value.copy(contacts = all, filtered = all, isLoading = false)
            } catch (e: SecurityException) {
                _state.value = _state.value.copy(isLoading = false, permissionDenied = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun onQueryChange(q: String) {
        _state.value = _state.value.copy(query = q)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(150)
            val filtered = if (q.isBlank()) _state.value.contacts else repo.search(q)
            _state.value = _state.value.copy(filtered = filtered)
        }
    }

    fun isFavorite(id: String): Boolean = favRepo.isFavorite(id)

    fun toggleFavorite(id: String): Boolean = favRepo.toggle(id)

    fun deleteContact(id: String) {
        favRepo.setFavorite(id, false)
    }
}

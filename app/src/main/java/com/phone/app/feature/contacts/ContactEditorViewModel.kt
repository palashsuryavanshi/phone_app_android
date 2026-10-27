package com.phone.app.feature.contacts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.phone.app.core.database.entity.ContactEntity
import com.phone.app.core.database.dao.ContactDao
import com.phone.app.core.database.PhoneDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ContactEditorUiState(
    val contact: ContactEntity? = null,
    val isEditing: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

class ContactEditorViewModel(application: Application) : AndroidViewModel(application) {
    private val db = PhoneDatabase.getInstance(application)
    private val contactDao: ContactDao = db.contactDao()
    private val _state = MutableStateFlow(ContactEditorUiState())
    val state: StateFlow<ContactEditorUiState> = _state.asStateFlow()

    // Public access to state for error handling
    val currentError: String?
        get() = _state.value.error
    val currentSuccess: Boolean
        get() = _state.value.success

    fun loadContact(id: String) {
        viewModelScope.launch {
            val contact = contactDao.getContactById(id)
            _state.value = _state.value.copy(
                contact = contact,
                isEditing = contact != null
            )
        }
    }

    fun saveContact(
        displayName: String,
        givenName: String?,
        familyName: String?,
        phoneNumbers: List<String>,
        emailAddresses: List<String>,
        photoUri: String?,
        isFavorite: Boolean
    ) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true, error = null, success = false)
            try {
                val normalizedNumbers = phoneNumbers.filter { it.isNotEmpty() }.joinToString(",")
                val normalizedEmails = emailAddresses.filter { it.isNotEmpty() }.joinToString(",")

                val contact = ContactEntity(
                    id = _state.value.contact?.id ?: java.util.UUID.randomUUID().toString(),
                    displayName = displayName,
                    givenName = givenName,
                    familyName = familyName,
                    phoneNumbers = normalizedNumbers,
                    emailAddresses = normalizedEmails,
                    photoUri = photoUri,
                    isFavorite = isFavorite,
                    lastCalled = _state.value.contact?.lastCalled,
                    callCount = _state.value.contact?.callCount ?: 0
                )

                contactDao.insertContact(contact)
                _state.value = _state.value.copy(isSaving = false, success = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isSaving = false, error = e.message)
            }
        }
    }

    fun deleteContact() {
        viewModelScope.launch {
            val id = _state.value.contact?.id
            if (id != null) {
                contactDao.deleteContactById(id)
                _state.value = _state.value.copy(success = true)
            }
        }
    }

    fun resetState() {
        _state.value = ContactEditorUiState()
    }
}
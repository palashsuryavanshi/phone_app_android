package com.phone.app.feature.contacts

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.phone.app.core.designsystem.components.PhonePermissionPanel
import com.phone.app.core.designsystem.components.PhoneSearchField
import com.phone.app.core.designsystem.components.ContactRow
import com.phone.app.core.designsystem.components.EmptyState
import com.phone.app.core.designsystem.theme.PhoneTheme

@Composable
fun ContactsScreen(
    onContactClick: (String) -> Unit = {},
    onCallClick: (String) -> Unit = {}
) {
    val viewModel: ContactsViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var granted by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
    }
    var showContactEditor by remember { mutableStateOf(false) }
    var editingContactId by remember { mutableStateOf<String?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        granted = it
        if (it) viewModel.load()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Contacts",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )
        PhoneSearchField(
            query = state.query,
            onQueryChange = viewModel::onQueryChange,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = "Search contacts"
        )
        when {
            !granted || state.permissionDenied -> {
                PhonePermissionPanel(
                    title = "Contacts access",
                    rationale = "Phone uses contacts to show names and enable quick dialing.",
                    onGrant = { launcher.launch(Manifest.permission.READ_CONTACTS) },
                    onDismiss = {}
                )
            }
            state.isLoading -> {
                androidx.compose.material3.CircularProgressIndicator(modifier = Modifier.padding(32.dp))
            }
            state.filtered.isEmpty() -> {
                EmptyState(
                    title = "No contacts",
                    subtitle = "Your contacts will appear here.",
                    modifier = Modifier
                )
            }
            else -> {
                androidx.compose.foundation.lazy.LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.filtered, key = { it.id }) { c ->
                        ContactRow(
                            name = c.displayName,
                            subtitle = c.primaryNumber,
                            avatarInitials = c.displayName.take(2).uppercase(),
                            onClick = { onContactClick(c.id) },
                            onEditClick = { editingContactId = c.id; showContactEditor = true },
                            onDeleteClick = {
                                viewModel.deleteContact(c.id)
                            },
                            onCallClick = { onCallClick(c.primaryNumber ?: "") },
                            isFavorite = viewModel.isFavorite(c.id),
                            onFavoriteToggle = { viewModel.toggleFavorite(c.id) }
                        )
                    }
                }
            }
        }

        // Contact Editor overlay
        if (showContactEditor) {
            ContactEditorScreen(
                contactId = editingContactId,
                onContactSaved = {
                    showContactEditor = false
                    editingContactId = null
                    viewModel.load()
                },
                onContactDeleted = {
                    showContactEditor = false
                    editingContactId = null
                    viewModel.load()
                },
                onBack = {
                    showContactEditor = false
                    editingContactId = null
                }
            )
        }

        // Floating Action Button - Add Contact
        FloatingActionButton(
            onClick = {
                editingContactId = null
                showContactEditor = true
            },
            modifier = Modifier
                .align(Alignment.End)
                .padding(16.dp)
        ) {
            androidx.compose.material3.Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Contact"
            )
        }
    }
}

// Preview
@Composable
@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
fun ContactsPreview() {
    com.phone.app.core.designsystem.theme.PhoneTheme {
        ContactsScreen()
    }
}

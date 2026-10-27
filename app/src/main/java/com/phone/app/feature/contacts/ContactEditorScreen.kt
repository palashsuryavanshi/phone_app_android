package com.phone.app.feature.contacts

import android.app.Activity
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.phone.app.core.designsystem.theme.PhoneTheme
import com.phone.app.core.designsystem.theme.PhoneThemeManager

@Composable
fun ContactEditorScreen(
    contactId: String? = null,
    onContactSaved: (() -> Unit)? = null,
    onContactDeleted: (() -> Unit)? = null,
    onBack: () -> Unit = {}
) {
    val viewModel: ContactEditorViewModel = viewModel()
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    var displayName by remember { mutableStateOf(state.contact?.displayName ?: "") }
    var phoneNumbers by remember { mutableStateOf(state.contact?.phoneNumbers?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()) }
    var isFavorite by remember { mutableStateOf(state.contact?.isFavorite ?: false) }

    LaunchedEffect(contactId) {
        if (contactId != null) {
            viewModel.loadContact(contactId)
        }
    }

    // Success handling
    LaunchedEffect(state.success) {
        if (state.success) {
            onContactSaved?.invoke()
        }
    }

    PhoneTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Text(
                text = if (state.isEditing) "Edit Contact" else "Add Contact",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(16.dp))

            TextField(
                value = displayName,
                onValueChange = { displayName = it },
                label = { Text("Display Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )

            Spacer(Modifier.height(8.dp))

            phoneNumbers.forEachIndexed { index, number ->
                TextField(
                    value = number,
                    onValueChange = { phoneNumbers = phoneNumbers.toMutableList().apply { set(index, it) } },
                    label = { Text("Phone ${index + 1}") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            TextButton(
                onClick = { phoneNumbers = phoneNumbers + "" },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Phone Number")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Favorite Contact",
                    style = MaterialTheme.typography.bodyMedium
                )
                Switch(
                    checked = isFavorite,
                    onCheckedChange = { isFavorite = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            androidx.compose.material3.Button(
                onClick = {
                    val validNumbers = phoneNumbers.filter { it.isNotBlank() }
                    if (displayName.isBlank()) {
                        // Error handled by ViewModel
                    } else if (validNumbers.isEmpty()) {
                        // Error handled by ViewModel
                    } else {
                        viewModel.saveContact(
                            displayName = displayName,
                            givenName = null,
                            familyName = null,
                            phoneNumbers = validNumbers,
                            emailAddresses = emptyList(),
                            photoUri = null,
                            isFavorite = isFavorite
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (state.isEditing) "Save Changes" else "Create Contact")
            }

            if (state.isEditing) {
                Spacer(modifier = Modifier.height(8.dp))
                androidx.compose.material3.Button(
                    onClick = { viewModel.deleteContact() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            }
        }
    }
}

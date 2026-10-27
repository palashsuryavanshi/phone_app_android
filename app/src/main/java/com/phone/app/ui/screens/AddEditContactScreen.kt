package com.phone.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phone.app.data.model.Contact
import com.phone.app.ui.components.PhonePrimaryButton
import com.phone.app.ui.components.PhoneSecondaryButton
import com.phone.app.ui.components.PhoneTopBar
import com.phone.app.ui.theme.PhoneTheme
import com.phone.app.ui.viewmodel.PhoneViewModel

@Composable
fun AddEditContactScreen(
    contactToEdit: Contact? = null,
    viewModel: PhoneViewModel,
    modifier: Modifier = Modifier
) {
    val colors = PhoneTheme.colors
    val shapes = MaterialTheme.shapes
    val isEditMode = contactToEdit != null

    var name by remember { mutableStateOf(contactToEdit?.name ?: "") }
    var phone by remember { mutableStateOf(contactToEdit?.phoneNumber ?: "") }
    var secondaryPhone by remember { mutableStateOf(contactToEdit?.secondaryNumber ?: "") }
    var email by remember { mutableStateOf(contactToEdit?.email ?: "") }
    var selectedGroup by remember { mutableStateOf(contactToEdit?.group ?: "Personal") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }

    val groupOptions = listOf("Personal", "Work", "Favorites", "Family", "Friends")

    Column(
        modifier = modifier
            .testTag("add_edit_contact_screen")
            .fillMaxSize()
            .background(colors.background)
    ) {
        PhoneTopBar(
            title = if (isEditMode) "Edit Contact" else "New Contact",
            onBackClick = { viewModel.navigateBack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Contact Information",
                color = colors.textPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(14.dp))

            // Full Name Input
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    if (nameError != null) nameError = null
                },
                label = { Text("Full Name *") },
                isError = nameError != null,
                supportingText = {
                    if (nameError != null) Text(nameError!!, color = colors.error)
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.outline,
                    focusedTextColor = colors.textPrimary,
                    unfocusedTextColor = colors.textPrimary,
                    focusedLabelColor = colors.primary,
                    unfocusedLabelColor = colors.textMuted
                ),
                shape = shapes.small,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("contact_input_name")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Phone Number Input
            OutlinedTextField(
                value = phone,
                onValueChange = {
                    phone = it
                    if (phoneError != null) phoneError = null
                },
                label = { Text("Phone Number *") },
                isError = phoneError != null,
                supportingText = {
                    if (phoneError != null) Text(phoneError!!, color = colors.error)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.outline,
                    focusedTextColor = colors.textPrimary,
                    unfocusedTextColor = colors.textPrimary,
                    focusedLabelColor = colors.primary,
                    unfocusedLabelColor = colors.textMuted
                ),
                shape = shapes.small,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("contact_input_phone")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Secondary Phone Input
            OutlinedTextField(
                value = secondaryPhone,
                onValueChange = { secondaryPhone = it },
                label = { Text("Secondary Phone (Optional)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.outline,
                    focusedTextColor = colors.textPrimary,
                    unfocusedTextColor = colors.textPrimary,
                    focusedLabelColor = colors.primary,
                    unfocusedLabelColor = colors.textMuted
                ),
                shape = shapes.small,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("contact_input_secondary_phone")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Email Input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address (Optional)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.outline,
                    focusedTextColor = colors.textPrimary,
                    unfocusedTextColor = colors.textPrimary,
                    focusedLabelColor = colors.primary,
                    unfocusedLabelColor = colors.textMuted
                ),
                shape = shapes.small,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("contact_input_email")
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Category / Group Selection
            Text(
                text = "Group / Category",
                color = colors.textPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                groupOptions.take(3).forEach { group ->
                    val isSelected = selectedGroup == group
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) colors.primary else colors.surfaceSecondary)
                            .clickable { selectedGroup = group },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = group,
                            color = if (isSelected) Color.White else colors.textSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                groupOptions.drop(3).forEach { group ->
                    val isSelected = selectedGroup == group
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) colors.primary else colors.surfaceSecondary)
                            .clickable { selectedGroup = group },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = group,
                            color = if (isSelected) Color.White else colors.textSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Save and Cancel Actions
            PhonePrimaryButton(
                text = if (isEditMode) "Save Changes" else "Create Contact",
                onClick = {
                    var hasError = false
                    if (name.isBlank()) {
                        nameError = "Please enter a contact name"
                        hasError = true
                    }
                    if (phone.isBlank()) {
                        phoneError = "Please enter a valid phone number"
                        hasError = true
                    }
                    if (!hasError) {
                        if (isEditMode && contactToEdit != null) {
                            val updated = contactToEdit.copy(
                                name = name.trim(),
                                phoneNumber = phone.trim(),
                                secondaryNumber = secondaryPhone.trim().ifBlank { null },
                                email = email.trim().ifBlank { null },
                                group = selectedGroup
                            )
                            viewModel.repository.updateContact(updated)
                            viewModel.showToast("Contact updated")
                        } else {
                            viewModel.repository.addContact(
                                name = name.trim(),
                                phone = phone.trim(),
                                email = email.trim(),
                                group = selectedGroup
                            )
                            viewModel.showToast("Contact created")
                        }
                        viewModel.navigateBack()
                    }
                },
                testTag = "save_contact_button"
            )

            Spacer(modifier = Modifier.height(10.dp))

            PhoneSecondaryButton(
                text = "Cancel",
                onClick = { viewModel.navigateBack() },
                testTag = "cancel_contact_button"
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}



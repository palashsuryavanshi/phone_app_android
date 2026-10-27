package com.phone.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phone.app.ui.components.PhoneContactRow
import com.phone.app.ui.components.PhoneEmptyState
import com.phone.app.ui.components.PhoneFilterChip
import com.phone.app.ui.components.PhoneSearchField
import com.phone.app.ui.components.PhoneTopBar
import com.phone.app.ui.theme.PhoneGradients
import com.phone.app.ui.theme.PhoneTheme
import com.phone.app.ui.viewmodel.ContactsFilter
import com.phone.app.ui.viewmodel.PhoneViewModel
import com.phone.app.ui.viewmodel.SubScreen

@Composable
fun ContactsScreen(
    viewModel: PhoneViewModel,
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {}
) {
    val colors = PhoneTheme.colors

    val filteredContacts by viewModel.filteredContacts.collectAsState()
    val allContacts by viewModel.repository.contacts.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val activeFilter by viewModel.contactsFilter.collectAsState()

    Box(
        modifier = modifier
            .testTag("contacts_screen")
            .fillMaxSize()
            .background(colors.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            PhoneTopBar(
                title = "Contacts",
                subtitle = "${allContacts.size} people",
                onMenuClick = onMenuClick
            )

            // Search Bar
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                PhoneSearchField(
                    query = searchQuery,
                    onQueryChange = { viewModel.setSearchQuery(it) },
                    placeholderText = "Filter by name or phone number..."
                )
            }

            // Filter Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    PhoneFilterChip(
                        label = "All Contacts",
                        isSelected = activeFilter == ContactsFilter.ALL,
                        onClick = { viewModel.setContactsFilter(ContactsFilter.ALL) },
                        count = allContacts.size,
                        testTag = "contact_filter_all"
                    )
                }
                item {
                    PhoneFilterChip(
                        label = "Favorites",
                        isSelected = activeFilter == ContactsFilter.FAVORITES,
                        onClick = { viewModel.setContactsFilter(ContactsFilter.FAVORITES) },
                        count = allContacts.count { it.isFavorite },
                        testTag = "contact_filter_favorites"
                    )
                }
                item {
                    PhoneFilterChip(
                        label = "Groups",
                        isSelected = activeFilter == ContactsFilter.GROUPS,
                        onClick = { viewModel.setContactsFilter(ContactsFilter.GROUPS) },
                        testTag = "contact_filter_groups"
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (filteredContacts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    PhoneEmptyState(
                        title = "No contacts found",
                        description = if (searchQuery.isNotEmpty()) "No results matching \"$searchQuery\"" else "Add your primary contacts to start calling effortlessly.",
                        icon = Icons.Default.People,
                        actionText = "Add Contact",
                        onActionClick = { viewModel.openSubScreen(SubScreen.ADD_CONTACT) }
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredContacts, key = { it.id }) { contact ->
                        PhoneContactRow(
                            contact = contact,
                            onClick = { viewModel.openSubScreen(SubScreen.CONTACT_DETAIL, contact) },
                            onCallClick = { viewModel.startOutgoingCall(contact.phoneNumber, contact.name) }
                        )
                    }
                }
            }
        }

        // Floating Action Button to Add New Contact
        FloatingActionButton(
            onClick = { viewModel.openSubScreen(SubScreen.ADD_CONTACT) },
            containerColor = colors.primary,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .testTag("add_contact_fab")
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 80.dp)
                .size(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Contact",
                modifier = Modifier.size(26.dp)
            )
        }
    }
}



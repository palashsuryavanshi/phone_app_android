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
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phone.app.ui.components.PhoneCallRow
import com.phone.app.ui.components.PhoneEmptyState
import com.phone.app.ui.components.PhoneFilterChip
import com.phone.app.ui.components.PhoneTopBar
import com.phone.app.ui.theme.PhoneTheme
import com.phone.app.ui.viewmodel.PhoneViewModel
import com.phone.app.ui.viewmodel.RecentsFilter
import com.phone.app.ui.viewmodel.SubScreen

@Composable
fun RecentsScreen(
    viewModel: PhoneViewModel,
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {}
) {
    val colors = PhoneTheme.colors

    val filteredLogs by viewModel.filteredRecents.collectAsState()
    val allLogs by viewModel.repository.callLogs.collectAsState()
    val activeFilter by viewModel.recentsFilter.collectAsState()
    val contacts by viewModel.repository.contacts.collectAsState()

    var showClearDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .testTag("recents_screen")
            .fillMaxSize()
            .background(colors.background)
    ) {
        PhoneTopBar(
            title = "Recents",
            subtitle = "${filteredLogs.size} calls recorded",
            onMenuClick = onMenuClick,
            actions = {
                if (allLogs.isNotEmpty()) {
                    IconButton(
                        onClick = { showClearDialog = true },
                        modifier = Modifier
                            .testTag("clear_history_button")
                            .size(40.dp)
                            .clip(CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Clear Call History",
                            tint = colors.textMuted
                        )
                    }
                }
            }
        )

        // Filter Chips Row
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                PhoneFilterChip(
                    label = "All",
                    isSelected = activeFilter == RecentsFilter.ALL,
                    onClick = { viewModel.setRecentsFilter(RecentsFilter.ALL) },
                    count = allLogs.size,
                    testTag = "filter_all"
                )
            }
            item {
                PhoneFilterChip(
                    label = "Missed",
                    isSelected = activeFilter == RecentsFilter.MISSED,
                    onClick = { viewModel.setRecentsFilter(RecentsFilter.MISSED) },
                    count = allLogs.count { it.callType == com.phone.app.data.model.CallType.MISSED },
                    testTag = "filter_missed"
                )
            }
            item {
                PhoneFilterChip(
                    label = "Incoming",
                    isSelected = activeFilter == RecentsFilter.INCOMING,
                    onClick = { viewModel.setRecentsFilter(RecentsFilter.INCOMING) },
                    count = allLogs.count { it.callType == com.phone.app.data.model.CallType.INCOMING },
                    testTag = "filter_incoming"
                )
            }
            item {
                PhoneFilterChip(
                    label = "Outgoing",
                    isSelected = activeFilter == RecentsFilter.OUTGOING,
                    onClick = { viewModel.setRecentsFilter(RecentsFilter.OUTGOING) },
                    count = allLogs.count { it.callType == com.phone.app.data.model.CallType.OUTGOING },
                    testTag = "filter_outgoing"
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (filteredLogs.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                PhoneEmptyState(
                    title = "No call history found",
                    description = "When you make or receive calls, your call records with timestamp and duration will be displayed here.",
                    icon = Icons.Default.History,
                    actionText = "Open Keypad",
                    onActionClick = { viewModel.setTab(com.phone.app.ui.viewmodel.MainTab.KEYPAD) }
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredLogs, key = { it.id }) { log ->
                    PhoneCallRow(
                        callLog = log,
                        onRowClick = {
                            val contact = contacts.find { it.phoneNumber == log.phoneNumber }
                            if (contact != null) {
                                viewModel.openSubScreen(SubScreen.CONTACT_DETAIL, contact)
                            } else {
                                viewModel.startOutgoingCall(log.phoneNumber, log.name)
                            }
                        },
                        onCallClick = {
                            viewModel.startOutgoingCall(log.phoneNumber, log.name)
                        }
                    )
                }
            }
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = {
                Text("Clear All Call Logs?", color = colors.textPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "This action will remove all incoming, outgoing, and missed call records from Phone.",
                    color = colors.textSecondary,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.repository.clearAllCallLogs()
                        showClearDialog = false
                        viewModel.showToast("Call history cleared")
                    }
                ) {
                    Text("Clear All", color = colors.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel", color = colors.textMuted)
                }
            },
            containerColor = colors.surfaceSecondary
        )
    }
}



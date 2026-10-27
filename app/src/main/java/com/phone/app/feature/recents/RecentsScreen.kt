package com.phone.app.feature.recents

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.phone.app.core.designsystem.components.PhoneSearchField
import com.phone.app.core.designsystem.components.CallHistoryRow
import com.phone.app.core.designsystem.components.EmptyState
import com.phone.app.core.util.CallLogFormatter

@Composable
fun RecentsScreen() {
    val viewModel: RecentsViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var selectedRecord by remember { mutableStateOf<com.phone.app.core.platform.calllog.CallRecord?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        PhoneSearchField(
            query = state.query,
            onQueryChange = viewModel::onQueryChange,
            modifier = Modifier.padding(16.dp),
            placeholder = "Search calls"
        )
        Row(modifier = Modifier.padding(horizontal = 16.dp), horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
            RecentsFilter.values().forEach { f ->
                FilterChip(selected = state.filter == f, onClick = { viewModel.setFilter(f) }, label = { Text(f.name) })
            }
        }
        when {
            state.permissionDenied -> {
                EmptyState(title = "Call log access", subtitle = "Permission denied. Grant access in settings.")
            }
            state.isLoading -> {
                androidx.compose.material3.CircularProgressIndicator(modifier = Modifier.padding(32.dp))
            }
            state.error != null -> {
                EmptyState(title = "Unable to load", subtitle = state.error)
            }
            state.grouped.isEmpty() -> {
                EmptyState(title = "No recent calls", subtitle = "Your call history will appear here.")
            }
            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.grouped, key = { it.key }) { g ->
                        CallHistoryRow(
                            name = g.displayName ?: g.number,
                            phoneNumber = g.number,
                            callTypeLabel = CallLogFormatter.typeLabel(g.latest.type) + if (g.count > 1) " (${g.count})" else "",
                            timestampLabel = CallLogFormatter.formatTimestamp(g.latest.date),
                            callTypeColor = MaterialTheme.colorScheme.primary,
                            onClick = { selectedRecord = g.latest }
                        )
                    }
                }
            }
        }

        selectedRecord?.let { rec ->
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { selectedRecord = null },
                title = { Text(rec.cachedName ?: rec.number) },
                text = {
                    Column {
                        Text(rec.number)
                        Text(CallLogFormatter.formatTimestamp(rec.date))
                        if (rec.duration > 0) Text(CallLogFormatter.formatDuration(rec.duration))
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${rec.number}"))
                        context.startActivity(intent)
                        selectedRecord = null
                    }) { Text("Call back") }
                },
                dismissButton = {
                    TextButton(onClick = { selectedRecord = null }) { Text("Close") }
                }
            )
        }
    }
}


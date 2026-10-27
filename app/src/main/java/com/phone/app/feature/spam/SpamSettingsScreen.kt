package com.phone.app.feature.spam

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.phone.app.core.designsystem.components.PhoneSwitchRow
import com.phone.app.core.platform.spam.SpamRulesRepository

@Composable
fun SpamSettingsScreen() {
    val context = LocalContext.current
    val repo = remember { SpamRulesRepository(context) }
    var enabled by remember { mutableStateOf(repo.isSpamFeatureEnabled()) }
    var blocked by remember { mutableStateOf(repo.blockedNumbers()) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Spam protection", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        PhoneSwitchRow(title = "Enable spam protection", checked = enabled, onCheckedChange = {
            enabled = it
            repo.setSpamFeatureEnabled(it)
        })
        Spacer(Modifier.height(16.dp))
        Text("Blocked numbers", style = MaterialTheme.typography.titleSmall)
        if (blocked.isEmpty()) {
            Text("No blocked numbers", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            LazyColumn {
                items(blocked.toList()) { num ->
                    Row {
                        Text(num, modifier = Modifier.weight(1f))
                        TextButton(onClick = { repo.unblock(num); blocked = repo.blockedNumbers() }) { Text("Remove") }
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text("Screening uses supported Android CallScreeningService when available.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}


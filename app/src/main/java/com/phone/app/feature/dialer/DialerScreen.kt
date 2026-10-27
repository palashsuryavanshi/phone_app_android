package com.phone.app.feature.dialer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.phone.app.core.designsystem.components.CallButton
import com.phone.app.core.designsystem.components.DialpadButton
import com.phone.app.core.designsystem.components.EmptyState
import com.phone.app.core.platform.contacts.ContactMatch
import com.phone.app.core.util.PhoneNumberFormatter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialerScreen() {
    val viewModel: DialerViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var showSimDialog by remember { mutableStateOf(false) }
    var simAccounts by remember { mutableStateOf<List<PhoneAccountHandle>>(emptyList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown) {
                    when (event.key) {
                        Key.NumPad0, Key.Zero -> { viewModel.insert('0'); true }
                        Key.NumPad1, Key.One -> { viewModel.insert('1'); true }
                        Key.NumPad2, Key.Two -> { viewModel.insert('2'); true }
                        Key.NumPad3, Key.Three -> { viewModel.insert('3'); true }
                        Key.NumPad4, Key.Four -> { viewModel.insert('4'); true }
                        Key.NumPad5, Key.Five -> { viewModel.insert('5'); true }
                        Key.NumPad6, Key.Six -> { viewModel.insert('6'); true }
                        Key.NumPad7, Key.Seven -> { viewModel.insert('7'); true }
                        Key.NumPad8, Key.Eight -> { viewModel.insert('8'); true }
                        Key.NumPad9, Key.Nine -> { viewModel.insert('9'); true }
                        Key.NumPadMultiply -> { viewModel.insert('*'); true }
                        Key.NumPadAdd -> { viewModel.insert('#'); true }
                        Key.Backspace -> { viewModel.delete(); true }
                        else -> false
                    }
                } else false
            }
    ) {
        TopAppBar(title = { Text("Dialer") })

        Spacer(modifier = Modifier.height(16.dp))

        // Contact match suggestion
        state.matches.firstOrNull()?.let { match ->
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = match.displayName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = match.phoneNumber,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Number display
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val fontSize = when {
                state.displayString.length > 18 -> 24.sp
                state.displayString.length > 12 -> 30.sp
                else -> 38.sp
            }
            Text(
                text = state.displayString.ifEmpty { "" },
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = fontSize,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Keypad
        val rows = listOf(
            listOf('1', '2', '3'),
            listOf('4', '5', '6'),
            listOf('7', '8', '9'),
            listOf('*', '0', '#')
        )
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { k ->
                    DialpadButton(
                        number = k.toString(),
                        onClick = { viewModel.insert(k) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Backspace - using text button instead of icon
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            androidx.compose.material3.TextButton(
                onClick = { viewModel.delete() },
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "⌫ Backspace",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CallButton(
            modifier = Modifier.padding(horizontal = 24.dp),
            onClick = {
                val number = PhoneNumberFormatter.toDialUri(state.dialString)
                if (number.isBlank()) return@CallButton
                val telecom = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
                val accounts = try { telecom.callCapablePhoneAccounts } catch (e: SecurityException) { emptyList() }
                if (accounts.size > 1) {
                    simAccounts = accounts
                    showSimDialog = true
                } else {
                    placeCall(context, number, accounts.firstOrNull())
                }
            }
        )
    }

    if (showSimDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showSimDialog = false },
            title = { Text("Choose SIM") },
            text = {
                Column {
                    simAccounts.forEach { acc ->
                        androidx.compose.material3.TextButton(onClick = {
                            placeCall(context, PhoneNumberFormatter.toDialUri(state.dialString), acc)
                            showSimDialog = false
                        }) { Text(acc.id) }
                    }
                }
            },
            confirmButton = {},
            dismissButton = { androidx.compose.material3.TextButton(onClick = { showSimDialog = false }) { Text("Cancel") } }
        )
    }
}

private fun placeCall(context: Context, number: String, account: PhoneAccountHandle?) {
    val granted = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) == android.content.pm.PackageManager.PERMISSION_GRANTED
    if (account != null) {
        val telecom = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        val extras = android.os.Bundle().apply {
            putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, account)
        }
        telecom.placeCall(Uri.parse("tel:$number"), extras)
    } else if (granted) {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
        context.startActivity(intent)
    } else {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        context.startActivity(intent)
    }
}
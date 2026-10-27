package com.phone.app.feature.privacycenter

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import com.phone.app.core.privacy.PermissionChecker
import com.phone.app.core.util.DefaultDialer
import com.phone.app.core.designsystem.components.PhonePreferenceRow
import com.phone.app.core.designsystem.components.PhonePrimaryButton
import com.phone.app.core.designsystem.components.EmptyState

@Composable
fun PrivacyCenterScreen() {
    val context = LocalContext.current
    val statuses = PermissionChecker.checkAll(context)
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Text("Privacy Center", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text("Phone keeps data on your device. Statuses are derived from Android.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(16.dp))

        Text("Permissions", style = MaterialTheme.typography.titleSmall)
        statuses.forEach { s ->
            PhonePreferenceRow(title = s.name, subtitle = if (s.granted) "Granted" else "Not granted") {}
        }
        Spacer(Modifier.height(8.dp))
        PhonePreferenceRow(title = "Default dialer", subtitle = if (DefaultDialer.isDefault(context)) "Phone is default" else "Not default") {}
        Spacer(Modifier.height(16.dp))

        Text("Data management", style = MaterialTheme.typography.titleSmall)
        PhonePrimaryButton(text = "Open app settings", onClick = {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, android.net.Uri.fromParts("package", context.packageName, null))
            context.startActivity(intent)
        })
        Spacer(Modifier.height(8.dp))
        PhonePrimaryButton(text = "Open default apps settings", onClick = {
            val intent = Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
            context.startActivity(intent)
        })
        Spacer(Modifier.height(16.dp))

        Text("Transparency", style = MaterialTheme.typography.titleSmall)
        Text(
            "Phone accesses contacts and call logs only with your permission. Data stays local. " +
                "Clearing Phone data does not delete Android's system call log or contacts. " +
                "No analytics or tracking SDKs are included.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


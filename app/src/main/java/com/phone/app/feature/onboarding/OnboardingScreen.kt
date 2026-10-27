package com.phone.app.feature.onboarding

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.phone.app.core.designsystem.components.PhonePrimaryButton
import com.phone.app.core.designsystem.components.PhoneSecondaryButton
import com.phone.app.core.util.AppPreferences
import com.phone.app.core.util.DefaultDialer

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit
) {
    val context = LocalContext.current
    var step by remember { mutableStateOf(0) }
    val totalSteps = 6

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { }
    val roleLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = when (step) {
                0 -> "Welcome to Phone Phone"
                1 -> "Privacy-first by design"
                2 -> "Make it yours"
                3 -> "Contacts access"
                4 -> "Default phone app"
                else -> "Set Phone as default"
            },
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = when (step) {
                0 -> "A premium, private phone experience built on your device."
                1 -> "No data leaves your phone. No ads. No tracking. All processing is on-device."
                2 -> "Choose themes, colors, shapes, and density to match your style."
                3 -> "Contact access is optional and only used to show names and enable quick dialing."
                4 -> "As your default dialer, Phone can place and receive calls and show call history."
                else -> "Open system settings to set Phone as the default phone app. You can skip and do this later."
            },
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(32.dp))

        if (step == 3) {
            val granted = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                PhoneSecondaryButton(text = "Grant contacts access", onClick = {
                    permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                })
                Spacer(Modifier.height(8.dp))
            }
        }

        if (step >= 4) {
            val isDefault = DefaultDialer.isDefault(context)
            Text(
                text = if (isDefault) "Phone is your default phone app." else "Phone is not the default phone app yet.",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDefault) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            if (!isDefault) {
                DefaultDialer.requestIntent(context)?.let { intent ->
                    PhoneSecondaryButton(text = "Set as default", onClick = { roleLauncher.launch(intent) })
                }
                Spacer(Modifier.height(8.dp))
            }
        }

        Spacer(Modifier.height(24.dp))
        androidx.compose.foundation.layout.Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (step < totalSteps - 1) {
                PhoneSecondaryButton(text = "Skip", onClick = {
                    AppPreferences.setOnboardingCompleted(context, true)
                    onFinish()
                })
            }
            PhonePrimaryButton(text = if (step < totalSteps - 1) "Next" else "Finish", onClick = {
                if (step < totalSteps - 1) step++ else {
                    AppPreferences.setOnboardingCompleted(context, true)
                    onFinish()
                }
            })
        }
    }
}


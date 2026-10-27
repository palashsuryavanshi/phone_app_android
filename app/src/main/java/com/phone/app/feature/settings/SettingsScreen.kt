package com.phone.app.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.phone.app.core.designsystem.components.PhonePreferenceRow
import com.phone.app.core.designsystem.components.PhonePrimaryButton
import com.phone.app.core.designsystem.components.PhoneSelectionChip
import com.phone.app.core.designsystem.theme.PhonePalette
import com.phone.app.core.designsystem.theme.PhoneThemeManager
import com.phone.app.core.util.AppPreferences
import com.phone.app.core.util.DefaultDialer

@Composable
fun SettingsScreen(onReplayOnboarding: () -> Unit, onOpenCustomization: () -> Unit = {}) {
    val context = LocalContext.current
    val config by PhoneThemeManager.config.collectAsState()
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        PhonePreferenceRow(
            title = "Default phone app",
            subtitle = if (DefaultDialer.isDefault(context)) "Phone is default" else "Not default"
        ) {}
        Spacer(Modifier.height(8.dp))
        Text("Theme preset", style = MaterialTheme.typography.titleSmall)
        Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
            PhonePalette.values().forEach { p ->
                PhoneSelectionChip(
                    label = p.name,
                    selected = config.palette == p,
                    onClick = { PhoneThemeManager.update(config.copy(palette = p)) }
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        PhonePrimaryButton(text = "Open personalization studio", onClick = onOpenCustomization)
        Spacer(Modifier.height(8.dp))
        PhonePrimaryButton(text = "Replay onboarding", onClick = {
            AppPreferences.setOnboardingCompleted(context, false)
            onReplayOnboarding()
        })
    }
}


package com.phone.app.feature.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.phone.app.core.designsystem.components.PhoneColorPicker
import com.phone.app.core.designsystem.components.PhonePrimaryButton
import com.phone.app.core.designsystem.components.PhoneSelectionChip
import com.phone.app.core.designsystem.components.PhoneSwitchRow
import com.phone.app.core.designsystem.theme.PhonePalette
import com.phone.app.core.designsystem.theme.ShapeFamily
import com.phone.app.core.designsystem.theme.ThemeConfiguration
import com.phone.app.core.designsystem.theme.ThemeSerializer
import com.phone.app.core.designsystem.theme.PhoneThemeManager

@Composable
fun CustomizationScreen() {
    val context = LocalContext.current
    val config by PhoneThemeManager.config.collectAsState()
    var importText by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var showResetDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)
    ) {
        Text("Personalization Studio", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        Text("Theme preset", style = MaterialTheme.typography.titleSmall)
        Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
            PhonePalette.values().forEach { p ->
                PhoneSelectionChip(label = p.name, selected = config.palette == p, onClick = {
                    PhoneThemeManager.update(config.copy(palette = p))
                })
            }
        }
        Spacer(Modifier.height(16.dp))

        Text("Shape family", style = MaterialTheme.typography.titleSmall)
        Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
            ShapeFamily.values().forEach { s ->
                PhoneSelectionChip(label = s.name, selected = config.shapes.family == s, onClick = {
                    PhoneThemeManager.update(config.copy(shapes = config.shapes.copy(family = s)))
                })
            }
        }
        Spacer(Modifier.height(16.dp))

        Text("Accent color", style = MaterialTheme.typography.titleSmall)
        PhoneColorPicker(
            colors = listOf(
                androidx.compose.ui.graphics.Color(0xFF7C3AED),
                androidx.compose.ui.graphics.Color(0xFF3B82F6),
                androidx.compose.ui.graphics.Color(0xFF06B6D4),
                androidx.compose.ui.graphics.Color(0xFF10B981),
                androidx.compose.ui.graphics.Color(0xFFEB4B9C),
                androidx.compose.ui.graphics.Color(0xFFF97316)
            ),
            selected = config.customSeedColor,
            onSelect = { c -> PhoneThemeManager.update(config.copy(customSeedColor = c, palette = PhonePalette.Custom)) }
        )
        Spacer(Modifier.height(16.dp))

        Text("Motion", style = MaterialTheme.typography.titleSmall)
        Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
            com.phone.app.core.designsystem.theme.MotionPreference.values().forEach { m ->
                PhoneSelectionChip(label = m.name, selected = config.motion.preference == m, onClick = {
                    PhoneThemeManager.update(config.copy(motion = config.motion.copy(preference = m)))
                })
            }
        }
        Spacer(Modifier.height(16.dp))

        Text("Haptics", style = MaterialTheme.typography.titleSmall)
        PhoneSwitchRow(
            title = "Haptic feedback",
            checked = config.hapticFeedback,
            onCheckedChange = { PhoneThemeManager.update(config.copy(hapticFeedback = it)) }
        )
        Spacer(Modifier.height(16.dp))

        Text("Export / Import", style = MaterialTheme.typography.titleSmall)
        PhonePrimaryButton(text = "Export theme", onClick = {
            val json = ThemeSerializer.export(config)
            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.setPrimaryClip(ClipData.newPlainText("Phone theme", json))
            status = "Theme copied to clipboard"
        })
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = importText,
            onValueChange = { importText = it },
            label = { Text("Paste theme JSON") },
            modifier = Modifier.fillMaxSize()
        )
        Spacer(Modifier.height(8.dp))
        PhonePrimaryButton(text = "Import theme", onClick = {
            val imported = ThemeSerializer.import(importText)
            if (imported != null) {
                PhoneThemeManager.update(imported)
                status = "Theme imported"
            } else {
                status = "Invalid theme JSON"
            }
        })
        Spacer(Modifier.height(8.dp))
        PhonePrimaryButton(text = "Reset all", onClick = { showResetDialog = true })
        Spacer(Modifier.height(8.dp))
        PhonePrimaryButton(text = "Open privacy center", onClick = {
            status = "Open privacy center"
        })
        if (status.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(status, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
        }

        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                title = { Text("Reset appearance?") },
                text = { Text("This restores all personalization settings to defaults.") },
                confirmButton = {
                    TextButton(onClick = {
                        PhoneThemeManager.update(ThemeConfiguration())
                        status = "Reset to defaults"
                        showResetDialog = false
                    }) { Text("Reset") }
                },
                dismissButton = { TextButton(onClick = { showResetDialog = false }) { Text("Cancel") } }
            )
        }
    }
}


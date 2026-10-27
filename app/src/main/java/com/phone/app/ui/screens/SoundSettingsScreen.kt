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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phone.app.ui.components.PhoneToggleRow
import com.phone.app.ui.components.PhoneTopBar
import com.phone.app.ui.theme.PhoneTheme
import com.phone.app.ui.viewmodel.PhoneViewModel

@Composable
fun SoundSettingsScreen(
    viewModel: PhoneViewModel,
    modifier: Modifier = Modifier
) {
    val colors = PhoneTheme.colors
    val shapes = MaterialTheme.shapes

    val currentRingtone by viewModel.repository.selectedRingtone.collectAsState()
    val currentVibration by viewModel.repository.selectedVibrationPattern.collectAsState()
    val dtmfEnabled by viewModel.repository.dtmfKeypadTones.collectAsState()
    val isPlayingSound by viewModel.isPlayingSoundPreview.collectAsState()

    val ringtones = listOf(
        Pair("Celestial Chime", "Atmospheric chime with layered harmonic pulses"),
        Pair("Aurora Chime", "Crystalline acoustic arpeggio"),
        Pair("Pulse Kinetic", "Modern synth wave with progressive rhythm"),
        Pair("Velvet Minimal", "Subtle organic bell tones for understated elegance")
    )

    val vibrationPatterns = listOf(
        Pair("Pulse Sync", "Standard synchronized rhythmic pulse"),
        Pair("Rapid Staccato", "Quick energetic vibrations for urgent alerts"),
        Pair("Gentle Wave", "Soft ramping vibration for quiet environments")
    )

    Column(
        modifier = modifier
            .testTag("sound_settings_screen")
            .fillMaxSize()
            .background(colors.background)
    ) {
        PhoneTopBar(
            title = "Sound & Ringtones",
            subtitle = "Ringtones, haptics & DTMF tones",
            onBackClick = { viewModel.navigateBack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            // Ringtone Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Phone Ringtone",
                    color = colors.textPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )

                // Interactive Audio Preview Play/Stop Toggle
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isPlayingSound) colors.secondary.copy(alpha = 0.2f) else colors.surfaceSecondary)
                        .border(1.dp, if (isPlayingSound) colors.secondary else colors.outline, CircleShape)
                        .clickable { viewModel.toggleSoundPreview() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isPlayingSound) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = if (isPlayingSound) "Stop" else "Preview",
                            tint = if (isPlayingSound) colors.secondary else colors.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isPlayingSound) "Playing..." else "Preview",
                            color = if (isPlayingSound) colors.secondary else colors.textPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ringtones.forEach { item ->
                    val isSelected = currentRingtone == item.first
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shapes.small)
                            .background(if (isSelected) colors.primary.copy(alpha = 0.18f) else colors.surfaceSecondary)
                            .border(
                                1.dp,
                                if (isSelected) colors.primary else colors.outline.copy(alpha = 0.4f),
                                shapes.small
                            )
                            .clickable {
                                viewModel.repository.selectedRingtone.value = item.first
                                viewModel.toggleSoundPreview()
                            }
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(colors.surfaceTertiary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = null,
                                tint = if (isSelected) colors.primary else colors.textMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.first,
                                color = if (isSelected) colors.primary else colors.textPrimary,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                            Text(
                                text = item.second,
                                color = colors.textMuted,
                                fontSize = 11.sp
                            )
                        }

                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = colors.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Vibration Patterns
            Text(
                text = "Haptic Vibration Patterns",
                color = colors.textPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                vibrationPatterns.forEach { item ->
                    val isSelected = currentVibration == item.first
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shapes.small)
                            .background(if (isSelected) colors.secondary.copy(alpha = 0.16f) else colors.surfaceSecondary)
                            .border(
                                1.dp,
                                if (isSelected) colors.secondary else colors.outline.copy(alpha = 0.4f),
                                shapes.small
                            )
                            .clickable {
                                viewModel.repository.selectedVibrationPattern.value = item.first
                                viewModel.triggerVibration(item.first)
                                viewModel.showToast("Testing ${item.first} vibration")
                            }
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(colors.surfaceTertiary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Vibration,
                                contentDescription = null,
                                tint = if (isSelected) colors.secondary else colors.textMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.first,
                                color = if (isSelected) colors.secondary else colors.textPrimary,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                            Text(
                                text = item.second,
                                color = colors.textMuted,
                                fontSize = 11.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(colors.surfaceTertiary)
                                .clickable {
                                    viewModel.triggerVibration(item.first)
                                }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "Test",
                                color = colors.secondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // DTMF & Dialpad Audio Feedback
            Text(
                text = "Keypad Audio & Feedback",
                color = colors.textPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))

            PhoneToggleRow(
                title = "Dialpad Touch Tones (DTMF)",
                description = "Play subtle harmonic tones when dialing phone numbers.",
                isChecked = dtmfEnabled,
                onCheckedChange = { viewModel.repository.dtmfKeypadTones.value = it }
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}



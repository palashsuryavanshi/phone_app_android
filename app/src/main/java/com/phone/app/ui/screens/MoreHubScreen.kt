package com.phone.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WavingHand
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phone.app.ui.components.PhoneTopBar
import com.phone.app.ui.theme.PhoneTheme
import com.phone.app.ui.viewmodel.PhoneViewModel
import com.phone.app.ui.viewmodel.SubScreen

import androidx.compose.material.icons.filled.PlayArrow

@Composable
fun SettingsScreen(
    viewModel: PhoneViewModel,
    modifier: Modifier = Modifier
) {
    val colors = PhoneTheme.colors
    val shapes = MaterialTheme.shapes

    Column(
        modifier = modifier
            .testTag("settings_screen")
            .fillMaxSize()
            .background(colors.background)
    ) {
        PhoneTopBar(
            title = "Settings",
            subtitle = "Customization, security, sound & system"
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Interactive Call Simulation Studio at top of Settings
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shapes.medium)
                        .background(colors.surfaceSecondary)
                        .border(1.dp, colors.primary.copy(alpha = 0.35f), shapes.medium)
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(colors.primary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = colors.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Call Simulation Studio",
                                color = colors.textPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Test full-screen incoming and spam call screens:",
                                color = colors.textMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(34.dp)
                                .clip(shapes.small)
                                .background(colors.primary.copy(alpha = 0.18f))
                                .border(1.dp, colors.primary.copy(alpha = 0.4f), shapes.small)
                                .clickable { viewModel.simulateIncomingCall("NORMAL") },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Normal", color = colors.primary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(34.dp)
                                .clip(shapes.small)
                                .background(colors.secondary.copy(alpha = 0.18f))
                                .border(1.dp, colors.secondary.copy(alpha = 0.4f), shapes.small)
                                .clickable { viewModel.simulateIncomingCall("UNKNOWN") },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Unknown", color = colors.secondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(34.dp)
                                .clip(shapes.small)
                                .background(colors.warning.copy(alpha = 0.18f))
                                .border(1.dp, colors.warning.copy(alpha = 0.4f), shapes.small)
                                .clickable { viewModel.simulateIncomingCall("SPAM") },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Spam Alert", color = colors.warning, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            item {
                Text(
                    text = "Personalization & Audio",
                    color = colors.textMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                )
            }

            item {
                HubRow(
                    title = "Appearance & Personalization",
                    description = "Theme, custom accents, wallpapers & control shapes",
                    icon = Icons.Default.Palette,
                    onClick = { viewModel.openSubScreen(SubScreen.APPEARANCE) },
                    testTag = "hub_appearance"
                )
            }

            item {
                HubRow(
                    title = "Sound & Ringtones",
                    description = "Ringtone picker, audio preview & vibration patterns",
                    icon = Icons.Default.MusicNote,
                    onClick = { viewModel.openSubScreen(SubScreen.SOUNDS) },
                    testTag = "hub_sounds"
                )
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Security & Calling Features",
                    color = colors.textMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                )
            }

            item {
                HubRow(
                    title = "Spam Protection Shield",
                    description = "Screened caller telemetry & community reporting",
                    icon = Icons.Default.Shield,
                    badge = "ACTIVE",
                    onClick = { viewModel.openSubScreen(SubScreen.SPAM) },
                    testTag = "hub_spam"
                )
            }

            item {
                HubRow(
                    title = "Privacy & Blocked Numbers",
                    description = "Local privacy, app lock & blocked caller registry",
                    icon = Icons.Default.Security,
                    onClick = { viewModel.openSubScreen(SubScreen.PRIVACY) },
                    testTag = "hub_privacy"
                )
            }

            item {
                HubRow(
                    title = "Call Preferences & Accounts",
                    description = "SIM management, caller ID & speed dial preferences",
                    icon = Icons.Default.Phone,
                    onClick = { viewModel.openSubScreen(SubScreen.CALL_SETTINGS) },
                    testTag = "hub_call_settings"
                )
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Design System & Platform",
                    color = colors.textMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                )
            }

            item {
                HubRow(
                    title = "Component Library Showcase",
                    description = "Interactive testbench for 30 component specifications",
                    icon = Icons.Default.Layers,
                    badge = "30 SPECS",
                    onClick = { viewModel.openSubScreen(SubScreen.COMPONENT_LIBRARY) },
                    testTag = "hub_component_library"
                )
            }

            item {
                HubRow(
                    title = "Onboarding Flow",
                    description = "Experience the 7-step welcome and setup walkthrough",
                    icon = Icons.Default.WavingHand,
                    onClick = { viewModel.openSubScreen(SubScreen.ONBOARDING) },
                    testTag = "hub_onboarding"
                )
            }

            item {
                HubRow(
                    title = "About Phone",
                    description = "System architecture, specifications & design credits",
                    icon = Icons.Default.Info,
                    onClick = { viewModel.openSubScreen(SubScreen.ABOUT) },
                    testTag = "hub_about"
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun HubRow(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badge: String? = null,
    testTag: String = "hub_row"
) {
    val colors = PhoneTheme.colors
    val shapes = MaterialTheme.shapes

    Row(
        modifier = modifier
            .testTag(testTag)
            .fillMaxWidth()
            .clip(shapes.medium)
            .background(colors.surfaceSecondary)
            .border(1.dp, colors.outline.copy(alpha = 0.45f), shapes.medium)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(colors.surfaceTertiary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colors.primary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = colors.textPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = description,
                color = colors.textMuted,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }

        if (badge != null) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(colors.primary.copy(alpha = 0.18f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = badge,
                    color = colors.primary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = colors.textMuted,
            modifier = Modifier.size(13.dp)
        )
    }
}



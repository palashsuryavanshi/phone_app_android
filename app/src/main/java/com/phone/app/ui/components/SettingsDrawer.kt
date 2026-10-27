package com.phone.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WavingHand
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phone.app.ui.theme.PhoneGradients
import com.phone.app.ui.theme.PhoneTheme
import com.phone.app.ui.viewmodel.PhoneViewModel
import com.phone.app.ui.viewmodel.SubScreen

@Composable
fun SettingsDrawerContent(
    viewModel: PhoneViewModel,
    onCloseDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = PhoneTheme.colors
    val shapes = MaterialTheme.shapes
    val spamEnabled by viewModel.repository.spamProtectionEnabled.collectAsState()

    ModalDrawerSheet(
        modifier = modifier
            .width(320.dp)
            .fillMaxHeight(),
        drawerContainerColor = colors.surface,
        drawerContentColor = colors.textPrimary
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .windowInsetsPadding(WindowInsets.statusBars)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Header with App Identity
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(PhoneGradients.VioletPulse),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = "Phone",
                        color = colors.textPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(if (spamEnabled) colors.success else colors.warning)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (spamEnabled) "Spam Shield: Active" else "Shield Paused",
                            color = colors.textMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // Call Simulation Quick Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shapes.medium)
                    .background(colors.surfaceSecondary)
                    .border(1.dp, colors.primary.copy(alpha = 0.35f), shapes.medium)
                    .padding(12.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = colors.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Test Incoming Call",
                            color = colors.textPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        DrawerSimButton("Normal", colors.primary, Modifier.weight(1f)) {
                            onCloseDrawer()
                            viewModel.simulateIncomingCall("NORMAL")
                        }
                        DrawerSimButton("Unknown", colors.secondary, Modifier.weight(1f)) {
                            onCloseDrawer()
                            viewModel.simulateIncomingCall("UNKNOWN")
                        }
                        DrawerSimButton("Spam", colors.warning, Modifier.weight(1f)) {
                            onCloseDrawer()
                            viewModel.simulateIncomingCall("SPAM")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = colors.outline.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "SETTINGS & PREFERENCES",
                color = colors.textMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                modifier = Modifier.padding(start = 6.dp, bottom = 6.dp)
            )

            DrawerItem(
                title = "Appearance & Theme",
                subtitle = "Dark, Light, AMOLED, Accents",
                icon = Icons.Default.Palette,
                onClick = {
                    onCloseDrawer()
                    viewModel.openSubScreen(SubScreen.APPEARANCE)
                },
                testTag = "drawer_appearance"
            )

            DrawerItem(
                title = "Sound & Ringtones",
                subtitle = "Chimes, Haptics & Vibration",
                icon = Icons.Default.MusicNote,
                onClick = {
                    onCloseDrawer()
                    viewModel.openSubScreen(SubScreen.SOUNDS)
                },
                testTag = "drawer_sounds"
            )

            DrawerItem(
                title = "Spam Protection Shield",
                subtitle = "Screening & Threat Blocking",
                icon = Icons.Default.Shield,
                badge = if (spamEnabled) "ACTIVE" else null,
                badgeColor = colors.success,
                onClick = {
                    onCloseDrawer()
                    viewModel.openSubScreen(SubScreen.SPAM)
                },
                testTag = "drawer_spam"
            )

            DrawerItem(
                title = "Privacy & Blocked Numbers",
                subtitle = "Local Storage & Call Blocking",
                icon = Icons.Default.Security,
                onClick = {
                    onCloseDrawer()
                    viewModel.openSubScreen(SubScreen.PRIVACY)
                },
                testTag = "drawer_privacy"
            )

            DrawerItem(
                title = "Calling Accounts & SIM",
                subtitle = "Dual SIM, Speed Dial & Display",
                icon = Icons.Default.Phone,
                onClick = {
                    onCloseDrawer()
                    viewModel.openSubScreen(SubScreen.CALL_SETTINGS)
                },
                testTag = "drawer_call_settings"
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = colors.outline.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "SYSTEM & TOOLS",
                color = colors.textMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                modifier = Modifier.padding(start = 6.dp, bottom = 6.dp)
            )

            DrawerItem(
                title = "Component Showcase",
                subtitle = "30 M3 Component Specifications",
                icon = Icons.Default.Layers,
                onClick = {
                    onCloseDrawer()
                    viewModel.openSubScreen(SubScreen.COMPONENT_LIBRARY)
                },
                testTag = "drawer_components"
            )

            DrawerItem(
                title = "Welcome Walkthrough",
                subtitle = "Review 7-Step Onboarding",
                icon = Icons.Default.WavingHand,
                onClick = {
                    onCloseDrawer()
                    viewModel.openSubScreen(SubScreen.ONBOARDING)
                },
                testTag = "drawer_onboarding"
            )

            DrawerItem(
                title = "About Phone",
                subtitle = "Version 2.4.0 • Architecture",
                icon = Icons.Default.Info,
                onClick = {
                    onCloseDrawer()
                    viewModel.openSubScreen(SubScreen.ABOUT)
                },
                testTag = "drawer_about"
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DrawerItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badge: String? = null,
    badgeColor: Color = Color(0xFF7565FF),
    testTag: String = "drawer_item"
) {
    val colors = PhoneTheme.colors
    val shapes = MaterialTheme.shapes

    Row(
        modifier = modifier
            .testTag(testTag)
            .fillMaxWidth()
            .clip(shapes.small)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(colors.surfaceSecondary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colors.primary,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = colors.textPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                color = colors.textMuted,
                fontSize = 11.sp,
                maxLines = 1
            )
        }

        if (badge != null) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(badgeColor.copy(alpha = 0.2f))
                    .padding(horizontal = 7.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badge,
                    color = badgeColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun DrawerSimButton(
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val shapes = MaterialTheme.shapes

    Box(
        modifier = modifier
            .height(28.dp)
            .clip(shapes.small)
            .background(color.copy(alpha = 0.16f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}



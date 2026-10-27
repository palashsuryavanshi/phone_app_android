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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.phone.app.ui.theme.PhoneGradients
import com.phone.app.ui.theme.PhoneTheme
import com.phone.app.ui.viewmodel.PhoneViewModel

@Composable
fun CallSettingsScreen(
    viewModel: PhoneViewModel,
    modifier: Modifier = Modifier
) {
    val colors = PhoneTheme.colors
    val shapes = MaterialTheme.shapes

    val defaultSim by viewModel.simSlot.collectAsState()
    var autoRedial by remember { mutableStateOf(false) }
    var callWaiting by remember { mutableStateOf(true) }
    var highContrast by remember { mutableStateOf(false) }
    var flashOnCall by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .testTag("call_settings_screen")
            .fillMaxSize()
            .background(colors.background)
    ) {
        PhoneTopBar(
            title = "Call Settings",
            subtitle = "SIM routing, accounts & accessibility",
            onBackClick = { viewModel.navigateBack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            // SIM Selection
            Text(
                text = "Calling Accounts & Dual SIM",
                color = colors.textPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shapes.medium)
                    .background(colors.surfaceSecondary)
                    .border(1.dp, colors.outline.copy(alpha = 0.5f), shapes.medium)
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(colors.primary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SimCard,
                            contentDescription = null,
                            tint = colors.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Default Outgoing SIM",
                            color = colors.textPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Currently SIM $defaultSim (Active)",
                            color = colors.textMuted,
                            fontSize = 12.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(colors.surfaceTertiary)
                        .clickable { viewModel.toggleSimSlot() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Switch",
                        color = colors.primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Calling Behavior
            Text(
                text = "Call Management",
                color = colors.textPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))

            PhoneToggleRow(
                title = "Call Waiting Alerts",
                description = "Notify with quiet tone when a second incoming call arrives.",
                isChecked = callWaiting,
                onCheckedChange = { callWaiting = it }
            )

            Spacer(modifier = Modifier.height(10.dp))

            PhoneToggleRow(
                title = "Auto Redial on Busy",
                description = "Automatically retry connecting when number is engaged.",
                isChecked = autoRedial,
                onCheckedChange = { autoRedial = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Accessibility Options
            Text(
                text = "Accessibility & Visibility",
                color = colors.textPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))

            PhoneToggleRow(
                title = "High Contrast Dialer",
                description = "Increases stroke definition and contrast on all keypad controls.",
                isChecked = highContrast,
                onCheckedChange = { highContrast = it }
            )

            Spacer(modifier = Modifier.height(10.dp))

            PhoneToggleRow(
                title = "Flash Notification on Call",
                description = "Blink camera torch LED when receiving incoming calls.",
                isChecked = flashOnCall,
                onCheckedChange = { flashOnCall = it }
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun AboutPhoneScreen(
    viewModel: PhoneViewModel,
    modifier: Modifier = Modifier
) {
    val colors = PhoneTheme.colors
    val shapes = MaterialTheme.shapes

    Column(
        modifier = modifier
            .testTag("about_phone_screen")
            .fillMaxSize()
            .background(colors.background)
    ) {
        PhoneTopBar(
            title = "About Phone",
            subtitle = "Product architecture & version details",
            onBackClick = { viewModel.navigateBack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Branding Badge
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(PhoneGradients.VioletPulse),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Phone",
                color = colors.textPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Version 2.4.0 (2026.10-release)",
                color = colors.secondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Specs Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shapes.large)
                    .background(colors.surfaceSecondary)
                    .border(1.dp, colors.outline.copy(alpha = 0.5f), shapes.large)
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Product Specifications",
                    color = colors.textPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                SpecRow("Design System", "Material 3 Expressive Tokens")
                SpecRow("Canvas Identity", "Premium Dark / AMOLED / Light")
                SpecRow("Audio Waveform", "Continuous Live FFT Synthesis")
                SpecRow("Spam Shield", "Real-time Telemetry & Screen Drop")
                SpecRow("Dual SIM", "SIM 1 & SIM 2 Hot Routing")
                SpecRow("Framework", "Jetpack Compose + Kotlin Coroutines")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Design Philosophy Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shapes.large)
                    .background(colors.surfaceSecondary)
                    .border(1.dp, colors.outline.copy(alpha = 0.5f), shapes.large)
                    .padding(18.dp)
            ) {
                Text(
                    text = "Design Philosophy",
                    color = colors.textPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Phone is built with deliberate restraint and deep visual richness. Designed without generic enterprise dashboard cliches, it couples human-centric accessibility with modern atmospheric elegance.",
                    color = colors.textSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun SpecRow(label: String, value: String) {
    val colors = PhoneTheme.colors

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = colors.textMuted, fontSize = 12.sp)
        Text(text = value, color = colors.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}



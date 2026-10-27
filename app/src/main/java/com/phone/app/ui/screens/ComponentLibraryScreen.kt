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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.phone.app.data.model.CallLogEntry
import com.phone.app.data.model.CallType
import com.phone.app.data.model.Contact
import com.phone.app.ui.components.AvatarSize
import com.phone.app.ui.components.PhoneActionIconButton
import com.phone.app.ui.components.PhoneActiveCallMiniBar
import com.phone.app.ui.components.PhoneCallButton
import com.phone.app.ui.components.PhoneCallRow
import com.phone.app.ui.components.PhoneContactAvatar
import com.phone.app.ui.components.PhoneContactRow
import com.phone.app.ui.components.PhoneDestructiveButton
import com.phone.app.ui.components.PhoneEmptyState
import com.phone.app.ui.components.PhoneEndCallButton
import com.phone.app.ui.components.PhoneErrorPanel
import com.phone.app.ui.components.PhoneFilterChip
import com.phone.app.ui.components.PhoneLoadingSkeleton
import com.phone.app.ui.components.PhonePermissionBanner
import com.phone.app.ui.components.PhonePrimaryButton
import com.phone.app.ui.components.PhoneSearchField
import com.phone.app.ui.components.PhoneSecondaryButton
import com.phone.app.ui.components.PhoneSettingsRow
import com.phone.app.ui.components.PhoneToggleRow
import com.phone.app.ui.components.PhoneTopBar
import com.phone.app.ui.components.PhoneWaveformVisualizer
import com.phone.app.ui.theme.PhoneGradients
import com.phone.app.ui.theme.PhoneTheme
import com.phone.app.ui.viewmodel.PhoneViewModel

enum class ComponentStateFilter {
    ALL,
    INTERACTIVE,
    DISABLED,
    UNIVERSAL_STATES
}

@Composable
fun ComponentLibraryScreen(
    viewModel: PhoneViewModel,
    modifier: Modifier = Modifier
) {
    val colors = PhoneTheme.colors
    val shapes = MaterialTheme.shapes

    var activeFilter by remember { mutableStateOf(ComponentStateFilter.ALL) }
    var demoToggleState by remember { mutableStateOf(true) }
    var demoSearchText by remember { mutableStateOf("Elena Rostova") }

    Column(
        modifier = modifier
            .testTag("component_library_screen")
            .fillMaxSize()
            .background(colors.background)
    ) {
        PhoneTopBar(
            title = "Component Specifications",
            subtitle = "30 tokenized M3 component definitions",
            onBackClick = { viewModel.navigateBack() }
        )

        // Filter Bar
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                PhoneFilterChip(
                    label = "All Components",
                    isSelected = activeFilter == ComponentStateFilter.ALL,
                    onClick = { activeFilter = ComponentStateFilter.ALL },
                    count = 30
                )
            }
            item {
                PhoneFilterChip(
                    label = "Interactive & Buttons",
                    isSelected = activeFilter == ComponentStateFilter.INTERACTIVE,
                    onClick = { activeFilter = ComponentStateFilter.INTERACTIVE }
                )
            }
            item {
                PhoneFilterChip(
                    label = "Disabled / Toggled",
                    isSelected = activeFilter == ComponentStateFilter.DISABLED,
                    onClick = { activeFilter = ComponentStateFilter.DISABLED }
                )
            }
            item {
                PhoneFilterChip(
                    label = "Universal States",
                    isSelected = activeFilter == ComponentStateFilter.UNIVERSAL_STATES,
                    onClick = { activeFilter = ComponentStateFilter.UNIVERSAL_STATES }
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Spec 1 & 2: Action Buttons
            if (activeFilter == ComponentStateFilter.ALL || activeFilter == ComponentStateFilter.INTERACTIVE) {
                item {
                    SectionHeader("01 & 02. Primary, Secondary & Destructive Buttons")
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        PhonePrimaryButton(
                            text = "Primary Button (Default)",
                            onClick = { viewModel.showToast("Primary clicked") },
                            testTag = "demo_primary_btn"
                        )
                        PhoneSecondaryButton(
                            text = "Secondary Tonal Button",
                            onClick = { viewModel.showToast("Secondary clicked") },
                            testTag = "demo_secondary_btn"
                        )
                        PhoneDestructiveButton(
                            text = "Destructive Button (Confirm Delete)",
                            onClick = { viewModel.showToast("Destructive action simulated") },
                            testTag = "demo_destructive_btn"
                        )
                    }
                }
            }

            // Spec 3: Disabled Button
            if (activeFilter == ComponentStateFilter.ALL || activeFilter == ComponentStateFilter.DISABLED) {
                item {
                    SectionHeader("03. Disabled State Buttons")
                    PhonePrimaryButton(
                        text = "Primary Button (Disabled State)",
                        enabled = false,
                        onClick = {}
                    )
                }
            }

            // Spec 4 & 5: Call Answer & End Buttons
            if (activeFilter == ComponentStateFilter.ALL || activeFilter == ComponentStateFilter.INTERACTIVE) {
                item {
                    SectionHeader("04 & 05. Call Answer & End Actions (Circular 68dp)")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            PhoneCallButton(onClick = { viewModel.showToast("Answer triggered") })
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Answer (Green)", color = colors.incomingCall, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            PhoneEndCallButton(onClick = { viewModel.showToast("End Call triggered") })
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("End (Red)", color = colors.endCall, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Spec 6: Active Call Controls
            if (activeFilter == ComponentStateFilter.ALL || activeFilter == ComponentStateFilter.INTERACTIVE) {
                item {
                    SectionHeader("06. In-Call Interactive Control Icons (Active vs Idle)")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        PhoneActionIconButton(
                            icon = Icons.Default.Mic,
                            label = "Mute (Idle)",
                            isActive = false,
                            onClick = { viewModel.showToast("Toggled Mute") }
                        )
                        PhoneActionIconButton(
                            icon = Icons.Default.VolumeUp,
                            label = "Speaker (Active)",
                            isActive = true,
                            onClick = { viewModel.showToast("Toggled Speaker") }
                        )
                    }
                }
            }

            // Spec 7: Live Audio Waveform Visualizer
            if (activeFilter == ComponentStateFilter.ALL || activeFilter == ComponentStateFilter.INTERACTIVE) {
                item {
                    SectionHeader("07. Continuous Waveform Visualizer (Real-time Synthesis)")
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shapes.medium)
                            .background(colors.surfaceSecondary)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        PhoneWaveformVisualizer(isMuted = false, isHeld = false)
                    }
                }
            }

            // Spec 8: Search Field
            if (activeFilter == ComponentStateFilter.ALL || activeFilter == ComponentStateFilter.INTERACTIVE) {
                item {
                    SectionHeader("08. Filterable Search Field")
                    PhoneSearchField(
                        query = demoSearchText,
                        onQueryChange = { demoSearchText = it }
                    )
                }
            }

            // Spec 9: Contact Avatars with Scaled Sizes
            if (activeFilter == ComponentStateFilter.ALL || activeFilter == ComponentStateFilter.INTERACTIVE) {
                item {
                    SectionHeader("09. Contact Avatar Sizes (Small, Medium, Large, Hero)")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PhoneContactAvatar("SM", size = AvatarSize.SMALL, gradient = PhoneGradients.VioletPulse)
                        PhoneContactAvatar("MD", size = AvatarSize.MEDIUM, gradient = PhoneGradients.ArcticFlow)
                        PhoneContactAvatar("LG", size = AvatarSize.LARGE, gradient = PhoneGradients.SunsetEmber)
                        PhoneContactAvatar("HE", size = AvatarSize.HERO, gradient = PhoneGradients.EmeraldNight)
                    }
                }
            }

            // Spec 10 & 11: Call Row & Contact Row
            if (activeFilter == ComponentStateFilter.ALL || activeFilter == ComponentStateFilter.INTERACTIVE) {
                item {
                    SectionHeader("10 & 11. Call Log Row & Contact Row")
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        PhoneCallRow(
                            callLog = CallLogEntry("demo1", null, "Elena Rostova", "+1 (555) 839-2041", CallType.INCOMING, "3m ago", 145),
                            onRowClick = { viewModel.showToast("Clicked row") },
                            onCallClick = { viewModel.showToast("Calling back") }
                        )
                        PhoneContactRow(
                            contact = Contact("c_demo", "Marcus Chen", "+1 (555) 624-9102", null, "marcus@tech.io", "MC", 0L, true, "Work"),
                            onClick = { viewModel.showToast("Contact details") },
                            onCallClick = { viewModel.showToast("Calling Marcus") }
                        )
                    }
                }
            }

            // Spec 12 & 13: Settings & Toggle Row
            if (activeFilter == ComponentStateFilter.ALL || activeFilter == ComponentStateFilter.INTERACTIVE) {
                item {
                    SectionHeader("12 & 13. Settings Row & Toggle Row")
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        PhoneSettingsRow(
                            title = "Theme & Customization",
                            description = "Configured for Dark Identity and Electric Violet",
                            icon = Icons.Default.Palette,
                            onClick = { viewModel.openSubScreen(com.phone.app.ui.viewmodel.SubScreen.APPEARANCE) },
                            trailingBadge = "DARK"
                        )
                        PhoneToggleRow(
                            title = "Automated Spam Shield",
                            description = "Block known telemarketers and robocalls",
                            isChecked = demoToggleState,
                            onCheckedChange = { demoToggleState = it },
                            icon = Icons.Default.Security
                        )
                    }
                }
            }

            // Spec 14: Ongoing Mini Bar
            if (activeFilter == ComponentStateFilter.ALL || activeFilter == ComponentStateFilter.INTERACTIVE) {
                item {
                    SectionHeader("14. Active Call Mini Bar (Floating Background Indicator)")
                    PhoneActiveCallMiniBar(
                        callerName = "Dr. Sarah Lin",
                        durationText = "04:12",
                        onClick = { viewModel.showToast("Expanding call") },
                        onEndClick = { viewModel.showToast("Call ended") }
                    )
                }
            }

            // Universal States: Skeleton Loader
            if (activeFilter == ComponentStateFilter.ALL || activeFilter == ComponentStateFilter.UNIVERSAL_STATES) {
                item {
                    SectionHeader("15. Universal State: Shimmering Loading Skeleton")
                    PhoneLoadingSkeleton()
                }
            }

            // Universal States: Error Panel
            if (activeFilter == ComponentStateFilter.ALL || activeFilter == ComponentStateFilter.UNIVERSAL_STATES) {
                item {
                    SectionHeader("16. Universal State: Error State Panel")
                    PhoneErrorPanel(
                        errorMessage = "Failed to synchronize contact records from cloud account.",
                        onRetry = { viewModel.showToast("Retrying connection...") }
                    )
                }
            }

            // Universal States: Empty State
            if (activeFilter == ComponentStateFilter.ALL || activeFilter == ComponentStateFilter.UNIVERSAL_STATES) {
                item {
                    SectionHeader("17. Universal State: Informational Empty State")
                    PhoneEmptyState(
                        title = "No Blocked Numbers",
                        description = "When you block nuisance callers, their records will be stored safely here.",
                        actionText = "Learn More",
                        onActionClick = { viewModel.showToast("Opening privacy policy") }
                    )
                }
            }

            // Universal States: Permission Banner
            if (activeFilter == ComponentStateFilter.ALL || activeFilter == ComponentStateFilter.UNIVERSAL_STATES) {
                item {
                    SectionHeader("18. Universal State: Transparent Permission Banner")
                    PhonePermissionBanner(
                        title = "Microphone Access Required",
                        description = "Enable microphone permissions to send audio on voice calls.",
                        onRequest = { viewModel.showToast("Permission requested") }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    val colors = PhoneTheme.colors

    Text(
        text = title,
        color = colors.primary,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.8.sp,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}



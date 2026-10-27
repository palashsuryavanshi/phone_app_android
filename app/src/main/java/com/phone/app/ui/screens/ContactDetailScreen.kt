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
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phone.app.data.model.Contact
import com.phone.app.ui.components.AvatarSize
import com.phone.app.ui.components.PhoneCallRow
import com.phone.app.ui.components.PhoneContactAvatar
import com.phone.app.ui.components.PhoneDestructiveButton
import com.phone.app.ui.components.PhoneTopBar
import com.phone.app.ui.theme.PhoneGradients
import com.phone.app.ui.theme.PhoneTheme
import com.phone.app.ui.viewmodel.PhoneViewModel
import com.phone.app.ui.viewmodel.SubScreen

@Composable
fun ContactDetailScreen(
    contact: Contact,
    viewModel: PhoneViewModel,
    modifier: Modifier = Modifier
) {
    val colors = PhoneTheme.colors
    val shapes = MaterialTheme.shapes
    val allLogs by viewModel.repository.callLogs.collectAsState()

    val contactLogs = allLogs.filter { it.phoneNumber == contact.phoneNumber }

    var showBlockDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .testTag("contact_detail_screen")
            .fillMaxSize()
            .background(colors.background)
    ) {
        PhoneTopBar(
            title = "",
            onBackClick = { viewModel.navigateBack() },
            actions = {
                IconButton(
                    onClick = {
                        viewModel.repository.toggleFavorite(contact.id)
                        viewModel.showToast(if (!contact.isFavorite) "Added to Favorites" else "Removed from Favorites")
                    },
                    modifier = Modifier.testTag("favorite_toggle_button")
                ) {
                    Icon(
                        imageVector = if (contact.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = "Favorite",
                        tint = if (contact.isFavorite) colors.warning else colors.textMuted
                    )
                }

                IconButton(
                    onClick = { viewModel.openSubScreen(SubScreen.EDIT_CONTACT, contact) },
                    modifier = Modifier.testTag("edit_contact_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = colors.textPrimary
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Hero Avatar with Ambient Glow
            PhoneContactAvatar(
                initials = contact.avatarInitials,
                size = AvatarSize.HERO,
                gradient = if (contact.isFavorite) PhoneGradients.SunsetEmber else PhoneGradients.VioletPulse
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = contact.name,
                color = colors.textPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(colors.primary.copy(alpha = 0.18f))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = contact.group,
                    color = colors.primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons Row: Call, Message, Video
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ContactActionTile(
                    label = "Call",
                    icon = Icons.Default.Call,
                    color = colors.incomingCall,
                    onClick = { viewModel.startOutgoingCall(contact.phoneNumber, contact.name) },
                    testTag = "contact_action_call"
                )
                ContactActionTile(
                    label = "Message",
                    icon = Icons.Default.Message,
                    color = colors.secondary,
                    onClick = { viewModel.showToast("Opening messaging for ${contact.phoneNumber}") },
                    testTag = "contact_action_message"
                )
                ContactActionTile(
                    label = "Video",
                    icon = Icons.Default.Videocam,
                    color = colors.primary,
                    onClick = { viewModel.showToast("HD Video calling unavailable in this build") },
                    testTag = "contact_action_video"
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Phone Numbers Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shapes.large)
                    .background(colors.surfaceSecondary)
                    .border(1.dp, colors.outline.copy(alpha = 0.4f), shapes.large)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Phone Number",
                    color = colors.textMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = contact.phoneNumber,
                            color = colors.textPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Mobile • Primary",
                            color = colors.secondary,
                            fontSize = 12.sp
                        )
                    }

                    IconButton(
                        onClick = { viewModel.startOutgoingCall(contact.phoneNumber, contact.name) },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(colors.surfaceTertiary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call",
                            tint = colors.incomingCall,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                if (contact.secondaryNumber != null) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Work Phone",
                        color = colors.textMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = contact.secondaryNumber,
                        color = colors.textPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (contact.email != null) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Email Address",
                        color = colors.textMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = contact.email,
                        color = colors.textPrimary,
                        fontSize = 15.sp
                    )
                }
            }

            // Recent Activity Section
            if (contactLogs.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Call History with ${contact.name.split(" ").first()}",
                        color = colors.textPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                contactLogs.forEach { log ->
                    PhoneCallRow(
                        callLog = log,
                        onRowClick = { viewModel.startOutgoingCall(log.phoneNumber, log.name) },
                        onCallClick = { viewModel.startOutgoingCall(log.phoneNumber, log.name) },
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Actions: Block and Delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .testTag("block_contact_button")
                        .weight(1f)
                        .height(48.dp)
                        .clip(shapes.small)
                        .background(colors.surfaceSecondary)
                        .border(1.dp, colors.outline.copy(alpha = 0.5f), shapes.small)
                        .clickable { showBlockDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Block,
                            contentDescription = null,
                            tint = colors.warning,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Block Number",
                            color = colors.warning,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .testTag("delete_contact_button")
                        .weight(1f)
                        .height(48.dp)
                        .clip(shapes.small)
                        .background(colors.error.copy(alpha = 0.12f))
                        .border(1.dp, colors.error.copy(alpha = 0.35f), shapes.small)
                        .clickable { showDeleteDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = colors.error,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Delete",
                            color = colors.error,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    if (showBlockDialog) {
        AlertDialog(
            onDismissRequest = { showBlockDialog = false },
            title = {
                Text("Block ${contact.name}?", color = colors.textPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "You will no longer receive calls or notifications from ${contact.phoneNumber}. Calls will be screened and dropped silently.",
                    color = colors.textSecondary,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.repository.blockNumber(contact.phoneNumber, "Blocked from Contact Profile")
                        showBlockDialog = false
                        viewModel.showToast("${contact.name} has been blocked")
                    }
                ) {
                    Text("Block", color = colors.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showBlockDialog = false }) {
                    Text("Cancel", color = colors.textMuted)
                }
            },
            containerColor = colors.surfaceSecondary
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text("Delete Contact?", color = colors.textPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "Are you sure you want to delete ${contact.name}? This will remove their record from Phone.",
                    color = colors.textSecondary,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.repository.deleteContact(contact.id)
                        showDeleteDialog = false
                        viewModel.navigateBack()
                        viewModel.showToast("Contact deleted")
                    }
                ) {
                    Text("Delete", color = colors.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = colors.textMuted)
                }
            },
            containerColor = colors.surfaceSecondary
        )
    }
}

@Composable
private fun ContactActionTile(
    label: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "contact_action_tile"
) {
    val colors = PhoneTheme.colors

    Column(
        modifier = modifier
            .testTag(testTag)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(colors.surfaceSecondary)
                .border(1.dp, colors.outline.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            color = colors.textSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}



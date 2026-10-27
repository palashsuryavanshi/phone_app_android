package com.phone.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phone.app.data.model.Contact
import com.phone.app.ui.components.AvatarSize
import com.phone.app.ui.components.PhoneCallButton
import com.phone.app.ui.components.PhoneContactAvatar
import com.phone.app.ui.components.PhoneTopBar
import com.phone.app.ui.theme.PhoneGradients
import com.phone.app.ui.theme.PhoneSpecialTypography
import com.phone.app.ui.theme.PhoneTheme
import com.phone.app.ui.viewmodel.PhoneViewModel
import com.phone.app.ui.viewmodel.SubScreen

data class DialerKey(val digit: Char, val subLetters: String)

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun DialerScreen(
    viewModel: PhoneViewModel,
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {}
) {
    val colors = PhoneTheme.colors

    val dialerInput by viewModel.dialerInput.collectAsState()
    val simSlot by viewModel.simSlot.collectAsState()
    val matchingContacts by viewModel.dialerMatchingContacts.collectAsState()

    val keypadRows = listOf(
        listOf(DialerKey('1', ""), DialerKey('2', "A B C"), DialerKey('3', "D E F")),
        listOf(DialerKey('4', "G H I"), DialerKey('5', "J K L"), DialerKey('6', "M N O")),
        listOf(DialerKey('7', "P Q R S"), DialerKey('8', "T U V"), DialerKey('9', "W X Y Z")),
        listOf(DialerKey('*', ""), DialerKey('0', "+"), DialerKey('#', ""))
    )

    Column(
        modifier = modifier
            .testTag("dialer_screen")
            .fillMaxSize()
            .background(colors.background)
    ) {
        // Refined Top Bar with Hamburger Menu
        PhoneTopBar(
            title = "Phone",
            subtitle = "Keypad",
            onMenuClick = onMenuClick,
            actions = {
                // SIM Selector in top bar
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(colors.surfaceSecondary)
                        .border(1.dp, colors.outline.copy(alpha = 0.5f), CircleShape)
                        .clickable { viewModel.toggleSimSlot() }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SimCard,
                            contentDescription = "Switch SIM",
                            tint = colors.primary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "SIM $simSlot",
                            color = colors.textPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.width(6.dp))
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Upper Section: T9 Matches & Number Display
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Predictive Contact Suggestions Banner
                if (matchingContacts.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        items(matchingContacts.take(3)) { contact ->
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .clip(CircleShape)
                                    .background(colors.surfaceSecondary)
                                    .border(1.dp, colors.primary.copy(alpha = 0.4f), CircleShape)
                                    .clickable {
                                        viewModel.startOutgoingCall(contact.phoneNumber, contact.name)
                                    }
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                PhoneContactAvatar(
                                    initials = contact.avatarInitials,
                                    size = AvatarSize.SMALL,
                                    gradient = PhoneGradients.ArcticFlow
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = contact.name,
                                        color = colors.textPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = contact.phoneNumber,
                                        color = colors.secondary,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                } else if (dialerInput.length >= 3) {
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(colors.surfaceSecondary)
                            .clickable {
                                viewModel.openSubScreen(
                                    SubScreen.ADD_CONTACT,
                                    Contact("new", "", dialerInput, null, null, "P")
                                )
                            }
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = null,
                            tint = colors.secondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Add to Contacts",
                            color = colors.secondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(28.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Formatted Digits Display
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dialerInput.ifEmpty { "Enter Number" },
                        color = if (dialerInput.isEmpty()) colors.textMuted else colors.textPrimary,
                        style = PhoneSpecialTypography.DialerDigit,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier.testTag("dialer_display_text")
                    )
                }
            }

            // Tactile Keypad
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                keypadRows.forEach { rowKeys ->
                    Row(
                        modifier = Modifier.fillMaxWidth(0.92f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        rowKeys.forEach { key ->
                            DialerKeyButton(
                                digit = key.digit,
                                subLetters = key.subLetters,
                                onClick = { viewModel.appendDialerDigit(key.digit) },
                                onLongClick = {
                                    if (key.digit == '0') {
                                        viewModel.appendDialerDigit('+')
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Bottom row: Call Button and Backspace
                Row(
                    modifier = Modifier.fillMaxWidth(0.92f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(72.dp))

                    PhoneCallButton(
                        onClick = {
                            if (dialerInput.isNotEmpty()) {
                                viewModel.startOutgoingCall(dialerInput)
                            } else {
                                viewModel.showToast("Enter a number to call")
                            }
                        },
                        size = 72.dp,
                        testTag = "dialer_call_button"
                    )

                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .combinedClickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = true),
                                onClick = { viewModel.deleteDialerDigit() },
                                onLongClick = { viewModel.clearDialerDigits() }
                            )
                            .testTag("dialer_backspace_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        if (dialerInput.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Backspace,
                                contentDescription = "Backspace",
                                tint = colors.textPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
private fun DialerKeyButton(
    digit: Char,
    subLetters: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {}
) {
    val colors = PhoneTheme.colors

    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(colors.surfaceSecondary)
            .border(1.dp, colors.outline.copy(alpha = 0.5f), CircleShape)
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true),
                onClick = onClick,
                onLongClick = onLongClick
            )
            .testTag("dialer_key_$digit")
            .semantics { contentDescription = "Dial digit $digit" },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = digit.toString(),
                color = colors.textPrimary,
                fontSize = 26.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 28.sp
            )
            if (subLetters.isNotEmpty()) {
                Text(
                    text = subLetters,
                    color = colors.textMuted,
                    style = PhoneSpecialTypography.DialerSubLetters
                )
            }
        }
    }
}



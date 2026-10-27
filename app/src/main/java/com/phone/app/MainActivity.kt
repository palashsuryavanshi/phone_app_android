package com.phone.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phone.app.data.repository.PhoneRepository
import com.phone.app.ui.components.SettingsDrawerContent
import com.phone.app.ui.screens.AddEditContactScreen
import com.phone.app.ui.screens.FullScreenCallContainer
import com.phone.app.ui.screens.ContactDetailScreen
import com.phone.app.ui.screens.ContactsScreen
import com.phone.app.ui.screens.DialerScreen
import com.phone.app.ui.screens.SettingsScreen
import com.phone.app.ui.screens.OnboardingScreen
import com.phone.app.ui.screens.PersonalizationScreen
import com.phone.app.ui.screens.PrivacySecurityScreen
import com.phone.app.ui.screens.RecentsScreen
import com.phone.app.ui.screens.SoundSettingsScreen
import com.phone.app.ui.screens.SpamProtectionScreen
import com.phone.app.ui.theme.PhonePhoneTheme
import com.phone.app.ui.theme.PhoneTheme
import com.phone.app.ui.viewmodel.MainTab
import com.phone.app.ui.viewmodel.PhoneViewModel
import com.phone.app.ui.viewmodel.SubScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val phoneViewModel: PhoneViewModel = viewModel()
            PhoneApp(viewModel = phoneViewModel)
        }
    }
}

@Composable
fun PhoneApp(viewModel: PhoneViewModel) {
    val themeMode by viewModel.repository.themeMode.collectAsState()
    val accentPalette by viewModel.repository.accentPalette.collectAsState()
    val shapePreset by viewModel.repository.shapePreset.collectAsState()

    val currentTab by viewModel.currentTab.collectAsState()
    val currentSubScreen by viewModel.currentSubScreen.collectAsState()
    val selectedContact by viewModel.selectedContact.collectAsState()
    val activeCall by viewModel.activeCall.collectAsState()
    val toastMessage by viewModel.transientMessage.collectAsState()

    val drawerState = rememberDrawerState(initialValue = androidx.compose.material3.DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    PhonePhoneTheme(
        themeMode = themeMode,
        accent = accentPalette,
        shapePreset = shapePreset
    ) {
        val colors = PhoneTheme.colors

        androidx.compose.material3.ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = currentSubScreen == SubScreen.NONE,
            drawerContent = {
                SettingsDrawerContent(
                    viewModel = viewModel,
                    onCloseDrawer = { coroutineScope.launch { drawerState.close() } }
                )
            }
        ) {
            androidx.compose.material3.Scaffold(
                containerColor = colors.background,
                bottomBar = {
                    if (currentSubScreen == SubScreen.NONE) {
                        com.phone.app.ui.components.PhoneBottomNavigationBar(
                            currentTab = currentTab,
                            onTabSelected = { viewModel.setTab(it) }
                        )
                    }
                }
            ) { innerPadding ->
                androidx.compose.foundation.layout.Box(
                    modifier = androidx.compose.ui.Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (currentSubScreen) {
                        SubScreen.CONTACT_DETAIL -> {
                            if (selectedContact != null) {
                                com.phone.app.ui.screens.ContactDetailScreen(contact = selectedContact!!, viewModel = viewModel)
                            } else {
                                com.phone.app.ui.screens.ContactsScreen(
                                    viewModel = viewModel
                                )
                            }
                        }
                        SubScreen.ADD_CONTACT -> {
                            com.phone.app.ui.screens.AddEditContactScreen(contactToEdit = null, viewModel = viewModel)
                        }
                        SubScreen.EDIT_CONTACT -> {
                            com.phone.app.ui.screens.AddEditContactScreen(contactToEdit = selectedContact, viewModel = viewModel)
                        }
                        SubScreen.SETTINGS -> {
                            com.phone.app.ui.screens.SettingsScreen(viewModel = viewModel)
                        }
                        SubScreen.APPEARANCE -> {
                            com.phone.app.ui.screens.PersonalizationScreen(viewModel = viewModel)
                        }
                        SubScreen.PRIVACY -> {
                            com.phone.app.ui.screens.PrivacySecurityScreen(viewModel = viewModel)
                        }
                        SubScreen.SPAM -> {
                            com.phone.app.ui.screens.SpamProtectionScreen(viewModel = viewModel)
                        }
                        SubScreen.SOUNDS -> {
                            com.phone.app.ui.screens.SoundSettingsScreen(viewModel = viewModel)
                        }
                        SubScreen.CALL_SETTINGS -> {
                            com.phone.app.ui.screens.CallSettingsScreen(viewModel = viewModel)
                        }
                        SubScreen.ABOUT -> {
                            com.phone.app.ui.screens.AboutPhoneScreen(viewModel = viewModel)
                        }
                        SubScreen.ONBOARDING -> {
                            com.phone.app.ui.screens.OnboardingScreen(viewModel = viewModel)
                        }
                        SubScreen.COMPONENT_LIBRARY -> {
                            com.phone.app.ui.screens.ComponentLibraryScreen(viewModel = viewModel)
                        }
                        SubScreen.NONE -> {
                            when (currentTab) {
                                MainTab.KEYPAD -> DialerScreen(
                                    viewModel = viewModel
                                )
                                MainTab.RECENTS -> RecentsScreen(
                                    viewModel = viewModel
                                )
                                MainTab.CONTACTS -> ContactsScreen(
                                    viewModel = viewModel
                                )
                            }
                        }
                    }
                }
            }

            // Transient Toast / Snackbar Feedback
            androidx.compose.animation.AnimatedVisibility(
                visible = toastMessage != null,
                enter = androidx.compose.animation.slideInVertically { it } + androidx.compose.animation.fadeIn(),
                exit = androidx.compose.animation.slideOutVertically { it } + androidx.compose.animation.fadeOut(),
                modifier = androidx.compose.ui.Modifier
                    .padding(bottom = 76.dp)
            ) {
                if (toastMessage != null) {
                    androidx.compose.foundation.layout.Box(
                        modifier = androidx.compose.ui.Modifier
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(colors.surfaceTertiary)
                            .border(1.dp, colors.primary.copy(alpha = 0.6f), androidx.compose.foundation.shape.CircleShape)
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        androidx.compose.material3.Text(
                            text = toastMessage!!,
                            color = colors.textPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

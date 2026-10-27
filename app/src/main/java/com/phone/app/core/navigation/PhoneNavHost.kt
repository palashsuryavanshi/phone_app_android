package com.phone.app.core.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.phone.app.core.util.AppPreferences
import com.phone.app.feature.contacts.ContactDetailScreen
import com.phone.app.feature.contacts.ContactEditorScreen
import com.phone.app.feature.contacts.ContactsScreen
import com.phone.app.feature.dialer.DialerScreen
import com.phone.app.feature.favorites.FavoritesScreen
import com.phone.app.feature.onboarding.OnboardingScreen
import com.phone.app.feature.privacycenter.PrivacyCenterScreen
import com.phone.app.feature.recents.RecentsScreen
import com.phone.app.feature.settings.CustomizationScreen
import com.phone.app.feature.settings.SettingsScreen
import com.phone.app.feature.spam.SpamSettingsScreen

@Composable
fun PhoneNavHost() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val startDestination = if (AppPreferences.isOnboardingCompleted(context)) Screen.Dialer.route else "onboarding"

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBars = currentRoute in listOf(Screen.Dialer.route, Screen.Recents.route, Screen.Contacts.route, Screen.Favorites.route, Screen.Settings.route)

    val compact = LocalConfiguration.current.screenWidthDp < 600

    if (compact) {
        Scaffold(
            bottomBar = {
                if (showBars) {
                    NavigationBar {
                        bottomItems.forEach { item ->
                            NavigationBarItem(
                                selected = currentRoute == item.route,
                                onClick = { navController.navigate(item.route) { launchSingleTop = true; restoreState = true } },
                                icon = { Text(item.label.take(1)) },
                                label = { Text(item.label) }
                            )
                        }
                    }
                }
            }
        ) { padding ->
            NavHostContainer(navController, startDestination, padding.calculateBottomPadding().value, onReplayOnboarding = {
                navController.navigate("onboarding") { popUpTo(0) }
            })
        }
    } else {
        Row {
            if (showBars) {
                NavigationRail {
                    bottomItems.forEach { item ->
                        NavigationRailItem(
                            selected = currentRoute == item.route,
                            onClick = { navController.navigate(item.route) { launchSingleTop = true; restoreState = true } },
                            icon = { Text(item.label.take(1)) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
            NavHostContainer(navController, startDestination, 0f, onReplayOnboarding = {
                navController.navigate("onboarding") { popUpTo(0) }
            })
        }
    }
}

private val bottomItems = listOf(
    BottomItem(Screen.Dialer.route, "Dialer"),
    BottomItem(Screen.Recents.route, "Recents"),
    BottomItem(Screen.Contacts.route, "Contacts"),
    BottomItem(Screen.Favorites.route, "Favorites"),
    BottomItem(Screen.Settings.route, "Settings")
)

private data class BottomItem(val route: String, val label: String)

@Composable
private fun NavHostContainer(
    navController: NavHostController,
    startDestination: String,
    bottomPadding: Float,
    onReplayOnboarding: () -> Unit
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable("onboarding") {
            OnboardingScreen(onFinish = {
                navController.navigate(Screen.Dialer.route) { popUpTo("onboarding") { inclusive = true } }
            })
        }
        composable(Screen.Dialer.route) { DialerScreen() }
        composable(Screen.Recents.route) { RecentsScreen() }
        composable(Screen.Contacts.route) {
            ContactsScreen(
                onContactClick = { id -> navController.navigate(Screen.ContactDetail.createRoute(id)) },
                onCallClick = { number -> /* call handled in viewModel */ }
            )
        }
        composable(Screen.ContactDetail.route) { backStack ->
            val id = backStack.arguments?.getString("contactId") ?: ""
            ContactDetailScreen(
                contactId = id,
                onBack = { navController.popBackStack() },
                onEditClick = { navController.navigate(Screen.ContactEditor.createRoute(id)) },
                onDeleteClick = {
                    // Delete handled via viewModel - navigate back after delete
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.ContactEditor.route) { backStack ->
            val id = backStack.arguments?.getString("contactId")
            ContactEditorScreen(
                contactId = id,
                onContactSaved = {
                    navController.popBackStack()
                },
                onContactDeleted = {
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.Favorites.route) { FavoritesScreen() }
        composable(Screen.Settings.route) { SettingsScreen(onReplayOnboarding = onReplayOnboarding, onOpenCustomization = { navController.navigate(Screen.Customization.route) }) }
        composable(Screen.Customization.route) { CustomizationScreen() }
        composable(Screen.PrivacyCenter.route) { PrivacyCenterScreen() }
        composable(Screen.SpamProtection.route) { SpamSettingsScreen() }
    }
}

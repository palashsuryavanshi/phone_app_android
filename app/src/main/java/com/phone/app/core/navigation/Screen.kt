package com.phone.app.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector? = null
) {
    // Primary destinations
    object Dialer : Screen("dialer", "Dialer", Icons.Default.Phone)
    object Recents : Screen("recents", "Recents", Icons.Default.History)
    object Contacts : Screen("contacts", "Contacts", Icons.Default.Contacts)
    object Favorites : Screen("favorites", "Favorites", Icons.Default.ContactPhone)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)

    // Detail screens
    object ContactDetail : Screen("contact/{contactId}", "Contact Details") {
        fun createRoute(contactId: String) = "contact/$contactId"
    }

    object CallScreen : Screen("call/{phoneNumber}", "Call") {
        fun createRoute(phoneNumber: String) = "call/${phoneNumber.replace("*", "%2A").replace("#", "%23")}"
    }

    object IncomingCall : Screen("incoming/{phoneNumber}/{name}", "Incoming Call") {
        fun createRoute(phoneNumber: String, name: String = "") = "incoming/${phoneNumber.replace("*", "%2A")}/$name"
    }

    // Settings sub-screens
    object Customization : Screen("settings/customization", "Customization")
    object PrivacyCenter : Screen("settings/privacy", "Privacy Center")
    object SpamProtection : Screen("settings/spam", "Spam Protection")

    // Contact management
    object ContactEditor : Screen("contact/editor", "Contact Editor") {
        fun createRoute(contactId: String? = null) = "contact/editor${contactId?.let { "/$it" } ?: ""}"
    }

    // About screen
    object About : Screen("settings/about", "About")

    // All routes as a list
    val bottomNavItems: List<Screen>
        get() = listOf(Dialer, Recents, Contacts, Favorites, Settings)
}

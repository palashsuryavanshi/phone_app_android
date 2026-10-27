package com.phone.app.core.designsystem.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.phone.app.core.designsystem.components.PhonePrimaryButton
import com.phone.app.core.designsystem.components.PhoneSearchField
import com.phone.app.core.designsystem.components.ContactRow
import com.phone.app.core.designsystem.components.EmptyState
import com.phone.app.core.designsystem.theme.PhoneTheme
import com.phone.app.core.designsystem.theme.ThemeConfiguration

@Preview(showBackground = true)
@Composable
fun PhoneComponentsPreview() {
    PhoneTheme(config = ThemeConfiguration()) {
        ColumnPreview()
    }
}

@Composable
private fun ColumnPreview() {
    androidx.compose.foundation.layout.Column {
        PhonePrimaryButton(text = "Primary", onClick = {})
        PhoneSearchField(query = "", onQueryChange = {})
        ContactRow(name = "Alice", subtitle = "+1 555 0100", avatarInitials = "A")
        EmptyState(title = "No recents", subtitle = "Your call history will appear here")
    }
}


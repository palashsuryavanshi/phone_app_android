package com.phone.app.feature.contacts

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.phone.app.core.designsystem.theme.PhoneTheme
import com.phone.app.core.platform.contacts.ContactDetail
import com.phone.app.core.platform.contacts.ContactsRepository
import com.phone.app.core.platform.contacts.FavoritesRepository

@Composable
fun ContactDetailScreen(
    contactId: String,
    onBack: () -> Unit = {},
    onEditClick: (() -> Unit)? = null,
    onDeleteClick: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val repo = remember { ContactsRepository(context) }
    val favRepo = remember { FavoritesRepository(context) }
    var detail by remember { mutableStateOf<ContactDetail?>(null) }
    var isFavorite by remember { mutableStateOf(favRepo.isFavorite(contactId)) }

    LaunchedEffect(contactId) {
        detail = repo.getDetail(contactId)
    }

    PhoneTheme {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = detail?.displayName ?: "Contact",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(Modifier.height(16.dp))
            detail?.let { d ->
                Text(d.displayName, style = MaterialTheme.typography.titleLarge)
                d.organization?.let { Text(it, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                Spacer(Modifier.height(16.dp))
                d.phoneNumbers.forEach { num ->
                    TextButton(onClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$num"))
                        context.startActivity(intent)
                    }) { Text("Call $num") }
                }
                d.emails.forEach { email ->
                    TextButton(onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
                        context.startActivity(intent)
                    }) { Text("Email $email") }
                }
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = {
                    isFavorite = favRepo.toggle(contactId)
                }) { Text(if (isFavorite) "Remove favorite" else "Add favorite") }
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.withAppendedPath(android.provider.ContactsContract.Contacts.CONTENT_URI, contactId))
                    context.startActivity(intent)
                }) { Text("Open in Contacts") }
            } ?: Text("Loading…", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

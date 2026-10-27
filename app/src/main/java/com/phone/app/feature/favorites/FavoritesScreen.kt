package com.phone.app.feature.favorites

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.phone.app.core.platform.contacts.ContactSummary
import com.phone.app.core.platform.contacts.ContactsRepository
import com.phone.app.core.platform.contacts.FavoritesRepository
import com.phone.app.core.designsystem.components.ContactRow
import com.phone.app.core.designsystem.components.EmptyState

@Composable
fun FavoritesScreen() {
    val context = LocalContext.current
    val repo = remember { ContactsRepository(context) }
    val favRepo = remember { FavoritesRepository(context) }
    var favorites by remember { mutableStateOf<List<ContactSummary>>(emptyList()) }

    LaunchedEffect(Unit) {
        val all = repo.getAll()
        val ids = favRepo.allIds()
        favorites = all.filter { ids.contains(it.id) }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text("Favorites", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))
        if (favorites.isEmpty()) {
            EmptyState(title = "No favorites", subtitle = "Star contacts to see them here.")
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(favorites, key = { it.id }) { c ->
                    ContactRow(
                        name = c.displayName,
                        subtitle = c.primaryNumber,
                        avatarInitials = c.displayName.take(2).uppercase(),
                        onClick = {
                            c.primaryNumber?.let { num ->
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$num"))
                                context.startActivity(intent)
                            }
                        }
                    )
                    TextButton(onClick = {
                        favRepo.setFavorite(c.id, false)
                        favorites = favorites.filterNot { it.id == c.id }
                    }) { Text("Remove") }
                }
            }
        }
    }
}

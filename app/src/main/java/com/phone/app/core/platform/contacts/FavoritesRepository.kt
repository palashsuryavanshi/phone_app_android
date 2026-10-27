package com.phone.app.core.platform.contacts

import android.content.Context

class FavoritesRepository(private val context: Context) {
    private val prefs = context.getSharedPreferences("Phone_favorites", Context.MODE_PRIVATE)

    fun isFavorite(id: String): Boolean = prefs.getBoolean(id, false)

    fun setFavorite(id: String, value: Boolean) {
        prefs.edit().putBoolean(id, value).apply()
    }

    fun toggle(id: String): Boolean {
        val next = !isFavorite(id)
        setFavorite(id, next)
        return next
    }

    fun allIds(): Set<String> = prefs.all.keys
}


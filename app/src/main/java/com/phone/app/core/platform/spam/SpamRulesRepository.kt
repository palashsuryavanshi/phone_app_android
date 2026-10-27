package com.phone.app.core.platform.spam

import android.content.Context

class SpamRulesRepository(private val context: Context) {
    private val prefs = context.getSharedPreferences("Phone_spam", Context.MODE_PRIVATE)

    fun isSpamFeatureEnabled(): Boolean = prefs.getBoolean("enabled", false)
    fun setSpamFeatureEnabled(v: Boolean) = prefs.edit().putBoolean("enabled", v).apply()

    fun isBlocked(number: String): Boolean = prefs.getStringSet("blocked", emptySet())?.contains(normalize(number)) == true
    fun block(number: String) {
        val set = prefs.getStringSet("blocked", emptySet())?.toMutableSet() ?: mutableSetOf()
        set.add(normalize(number))
        prefs.edit().putStringSet("blocked", set).apply()
    }
    fun unblock(number: String) {
        val set = prefs.getStringSet("blocked", emptySet())?.toMutableSet() ?: mutableSetOf()
        set.remove(normalize(number))
        prefs.edit().putStringSet("blocked", set).apply()
    }
    fun blockedNumbers(): Set<String> = prefs.getStringSet("blocked", emptySet()) ?: emptySet()

    fun addRule(pattern: String) {
        val set = prefs.getStringSet("rules", emptySet())?.toMutableSet() ?: mutableSetOf()
        set.add(pattern)
        prefs.edit().putStringSet("rules", set).apply()
    }
    fun rules(): Set<String> = prefs.getStringSet("rules", emptySet()) ?: emptySet()
    fun removeRule(pattern: String) {
        val set = prefs.getStringSet("rules", emptySet())?.toMutableSet() ?: mutableSetOf()
        set.remove(pattern)
        prefs.edit().putStringSet("rules", set).apply()
    }

    private fun normalize(n: String): String = n.filter { it.isDigit() }
}


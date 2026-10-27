package com.phone.app.core.util

import android.content.Context

object AppPreferences {
    private const val PREFS = "Phone_prefs"
    private const val KEY_ONBOARDING = "onboarding_completed"

    fun isOnboardingCompleted(context: Context): Boolean =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getBoolean(KEY_ONBOARDING, false)

    fun setOnboardingCompleted(context: Context, value: Boolean) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY_ONBOARDING, value).apply()
    }
}


package com.phone.app

import android.app.Application
import com.phone.app.core.designsystem.theme.PhoneThemeManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PhoneApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PhoneThemeManager.init(this)
    }
}


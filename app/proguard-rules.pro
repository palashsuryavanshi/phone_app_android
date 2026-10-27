# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.

# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ComponentSupplier { *; }
-keep class * implements dagger.hilt.internal.GeneratedComponent { *; }

# Keep Room entities
-keep class com.phone.app.core.database.entity.** { *; }

# Keep Compose
-keep class androidx.compose.** { *; }

# Keep data classes used with serialization
-keep class com.phone.app.feature.dialer.** { *; }
-keep class com.phone.app.feature.contacts.** { *; }
-keep class com.phone.app.feature.recents.** { *; }
-keep class com.phone.app.feature.favorites.** { *; }
-keep class com.phone.app.feature.calling.** { *; }
-keep class com.phone.app.feature.settings.** { *; }
-keep class com.phone.app.core.navigation.** { *; }
-keep class com.phone.app.core.designsystem.** { *; }

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# AndroidX
-keep class androidx.lifecycle.** { *; }
-keep class androidx.navigation.** { *; }

# Keep ViewBinding if used
-keep class * implements androidx.viewbinding.ViewBinding {
    public static ** bind(android.view.View);
}

# Don't warn about missing classes for features not used
-dontwarn androidx.**
-dontwarn dagger.**
-dontwarn kotlinx.**

# R8 full mode
-assumevalues class androidx.compose.ui.Modifier {
    public static androidx.compose.ui.Modifier compositionLocalOf(...);
}

package com.phone.app.core.designsystem.theme

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import androidx.compose.ui.graphics.Color

private val Context.PhoneThemeDataStore by preferencesDataStore(name = "Phone_theme")

object PhoneThemeManager {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var initialized = false
    private lateinit var appContext: Context
    private val _config = MutableStateFlow(ThemeConfiguration())
    val config: StateFlow<ThemeConfiguration> = _config.asStateFlow()

    fun init(context: Context) {
        if (initialized) return
        appContext = context.applicationContext
        _config.value = runBlocking { load() }
        initialized = true
    }

    private suspend fun load(): ThemeConfiguration {
        val prefs = appContext.PhoneThemeDataStore.data.first()
        return ThemeConfiguration(
            version = prefs[intPreferencesKey("version")] ?: 1,
            appearance = PhoneAppearance.values().getOrElse(prefs[intPreferencesKey("appearance")] ?: 0) { PhoneAppearance.System },
            dynamicColor = prefs[booleanPreferencesKey("dynamicColor")] ?: true,
            palette = PhonePalette.values().getOrElse(prefs[intPreferencesKey("palette")] ?: 0) { PhonePalette.DefaultPhone },
            customSeedColor = Color(prefs[longPreferencesKey("customSeed")] ?: 0xFF6750A4L),
            gradient = GradientConfig(
                enabled = prefs[booleanPreferencesKey("gradientEnabled")] ?: false,
                startColor = Color(prefs[longPreferencesKey("gradientStart")] ?: 0xFF6750A4L),
                endColor = Color(prefs[longPreferencesKey("gradientEnd")] ?: 0xFFD0BCFFL),
                direction = GradientDirection.values().getOrElse(prefs[intPreferencesKey("gradientDir")] ?: 0) { GradientDirection.TopBottom }
            ),
            typography = TypographyConfig(
                scale = PhoneTypographyScale.values().getOrElse(prefs[intPreferencesKey("typographyScale")] ?: 0) { PhoneTypographyScale.Default },
                fontFamily = prefs[stringPreferencesKey("fontFamily")] ?: "default",
                numberSpacing = prefs[floatPreferencesKey("numberSpacing")] ?: 0f
            ),
            shapes = ShapeConfig(
                family = ShapeFamily.values().getOrElse(prefs[intPreferencesKey("shapeFamily")] ?: 3) { ShapeFamily.StandardRounded },
                cornerRadiusScale = prefs[floatPreferencesKey("cornerRadiusScale")] ?: 1f
            ),
            dialer = DialerConfig(
                keyShape = ShapeFamily.values().getOrElse(prefs[intPreferencesKey("dialerKeyShape")] ?: 1) { ShapeFamily.RoundedSquare },
                keySizeScale = prefs[floatPreferencesKey("dialerKeySize")] ?: 1f,
                keySpacingScale = prefs[floatPreferencesKey("dialerKeySpacing")] ?: 1f,
                letterLabels = prefs[booleanPreferencesKey("dialerLetters")] ?: true,
                numberTypography = prefs[stringPreferencesKey("dialerNumberTypo")] ?: "default"
            ),
            contacts = ContactsConfig(
                density = PhoneDensity.values().getOrElse(prefs[intPreferencesKey("contactsDensity")] ?: 1) { PhoneDensity.Comfortable },
                avatarSizeScale = prefs[floatPreferencesKey("contactsAvatarSize")] ?: 1f,
                metadataVisible = prefs[booleanPreferencesKey("contactsMetadata")] ?: true
            ),
            calls = CallScreenConfig(
                backgroundPalette = prefs[stringPreferencesKey("callBg")] ?: "default",
                avatarStyle = prefs[stringPreferencesKey("callAvatar")] ?: "circle",
                controlShapes = ShapeFamily.values().getOrElse(prefs[intPreferencesKey("callControlShape")] ?: 3) { ShapeFamily.StandardRounded },
                motionIntensity = prefs[floatPreferencesKey("callMotion")] ?: 1f
            ),
            navigation = NavConfig(appearance = prefs[stringPreferencesKey("navAppearance")] ?: "default"),
            motion = MotionConfig(preference = MotionPreference.values().getOrElse(prefs[intPreferencesKey("motion")] ?: 3) { MotionPreference.System }),
            hapticFeedback = prefs[booleanPreferencesKey("haptic")] ?: true,
            density = PhoneDensity.values().getOrElse(prefs[intPreferencesKey("density")] ?: 1) { PhoneDensity.Comfortable }
        )
    }

    fun update(newConfig: ThemeConfiguration) {
        _config.value = newConfig
        scope.launch { save(newConfig) }
    }

    private suspend fun save(c: ThemeConfiguration) {
        appContext.PhoneThemeDataStore.edit { prefs ->
            prefs[intPreferencesKey("version")] = c.version
            prefs[intPreferencesKey("appearance")] = c.appearance.ordinal
            prefs[booleanPreferencesKey("dynamicColor")] = c.dynamicColor
            prefs[intPreferencesKey("palette")] = c.palette.ordinal
            prefs[longPreferencesKey("customSeed")] = c.customSeedColor.value.toLong()
            prefs[booleanPreferencesKey("gradientEnabled")] = c.gradient.enabled
            prefs[longPreferencesKey("gradientStart")] = c.gradient.startColor.value.toLong()
            prefs[longPreferencesKey("gradientEnd")] = c.gradient.endColor.value.toLong()
            prefs[intPreferencesKey("gradientDir")] = c.gradient.direction.ordinal
            prefs[intPreferencesKey("typographyScale")] = c.typography.scale.ordinal
            prefs[stringPreferencesKey("fontFamily")] = c.typography.fontFamily
            prefs[floatPreferencesKey("numberSpacing")] = c.typography.numberSpacing
            prefs[intPreferencesKey("shapeFamily")] = c.shapes.family.ordinal
            prefs[floatPreferencesKey("cornerRadiusScale")] = c.shapes.cornerRadiusScale
            prefs[intPreferencesKey("dialerKeyShape")] = c.dialer.keyShape.ordinal
            prefs[floatPreferencesKey("dialerKeySize")] = c.dialer.keySizeScale
            prefs[floatPreferencesKey("dialerKeySpacing")] = c.dialer.keySpacingScale
            prefs[booleanPreferencesKey("dialerLetters")] = c.dialer.letterLabels
            prefs[stringPreferencesKey("dialerNumberTypo")] = c.dialer.numberTypography
            prefs[intPreferencesKey("contactsDensity")] = c.contacts.density.ordinal
            prefs[floatPreferencesKey("contactsAvatarSize")] = c.contacts.avatarSizeScale
            prefs[booleanPreferencesKey("contactsMetadata")] = c.contacts.metadataVisible
            prefs[stringPreferencesKey("callBg")] = c.calls.backgroundPalette
            prefs[stringPreferencesKey("callAvatar")] = c.calls.avatarStyle
            prefs[intPreferencesKey("callControlShape")] = c.calls.controlShapes.ordinal
            prefs[floatPreferencesKey("callMotion")] = c.calls.motionIntensity
            prefs[stringPreferencesKey("navAppearance")] = c.navigation.appearance
            prefs[intPreferencesKey("motion")] = c.motion.preference.ordinal
            prefs[booleanPreferencesKey("haptic")] = c.hapticFeedback
            prefs[intPreferencesKey("density")] = c.density.ordinal
        }
    }
}

private fun stringPreferencesKey(name: String) = androidx.datastore.preferences.core.stringPreferencesKey(name)


package com.phone.app.core.designsystem.theme

import androidx.compose.ui.graphics.Color
import org.json.JSONObject

object ThemeSerializer {
    const val CURRENT_VERSION = 1

    fun export(config: ThemeConfiguration): String {
        val o = JSONObject()
        o.put("version", config.version)
        o.put("appearance", config.appearance.name)
        o.put("dynamicColor", config.dynamicColor)
        o.put("palette", config.palette.name)
        o.put("customSeed", config.customSeedColor.value.toLong())
        o.put("haptic", config.hapticFeedback)
        o.put("density", config.density.name)
        o.put("motion", config.motion.preference.name)
        o.put("shapeFamily", config.shapes.family.name)
        o.put("cornerRadiusScale", config.shapes.cornerRadiusScale.toDouble())
        o.put("gradientEnabled", config.gradient.enabled)
        return o.toString()
    }

    fun import(json: String): ThemeConfiguration? {
        return try {
            val o = JSONObject(json)
            val version = o.optInt("version", 0)
            if (version != CURRENT_VERSION) return null
            ThemeConfiguration(
                version = version,
                appearance = PhoneAppearance.valueOf(o.optString("appearance", "System")),
                dynamicColor = o.optBoolean("dynamicColor", true),
                palette = PhonePalette.valueOf(o.optString("palette", "DefaultPhone")),
                customSeedColor = Color(o.optLong("customSeed", 0xFF6750A4L)),
                hapticFeedback = o.optBoolean("haptic", true),
                density = PhoneDensity.valueOf(o.optString("density", "Comfortable")),
                motion = MotionConfig(MotionPreference.valueOf(o.optString("motion", "System"))),
                shapes = ShapeConfig(
                    family = ShapeFamily.valueOf(o.optString("shapeFamily", "StandardRounded")),
                    cornerRadiusScale = o.optDouble("cornerRadiusScale", 1.0).toFloat()
                ),
                gradient = GradientConfig(enabled = o.optBoolean("gradientEnabled", false))
            )
        } catch (_: Exception) {
            null
        }
    }
}


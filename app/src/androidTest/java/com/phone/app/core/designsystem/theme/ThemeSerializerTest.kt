package com.phone.app.core.designsystem.theme

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ThemeSerializerTest {
    @Test
    fun exportImportRoundTrip() {
        val cfg = ThemeConfiguration(version = 1, hapticFeedback = false)
        val json = ThemeSerializer.export(cfg)
        val imported = ThemeSerializer.import(json)
        assertEquals(cfg.hapticFeedback, imported?.hapticFeedback)
    }

    @Test
    fun importInvalidVersionReturnsNull() {
        val json = """{"version":999}"""
        assertNull(ThemeSerializer.import(json))
    }

    @Test
    fun importMalformedReturnsNull() {
        assertNull(ThemeSerializer.import("not json"))
    }
}

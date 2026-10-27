package com.phone.app.core.util

import org.junit.Assert.assertEquals
import org.junit.Test

class PhoneNumberFormatterTest {
    @Test
    fun formatForDisplay_groupsDigits() {
        assertEquals("123 456 789", PhoneNumberFormatter.formatForDisplay("123456789"))
    }

    @Test
    fun formatForDisplay_preservesPlus() {
        assertEquals("+123 456", PhoneNumberFormatter.formatForDisplay("+123456"))
    }

    @Test
    fun sanitize_keepsSpecialChars() {
        assertEquals("+1 (234) 567-890*#", PhoneNumberFormatter.sanitize("+1 (234) 567-890*#"))
    }

    @Test
    fun toDialUri_removesFormatting() {
        assertEquals("+1234567890", PhoneNumberFormatter.toDialUri("+1 (234) 567-890"))
    }

    @Test
    fun isValid_nonEmpty() {
        assertEquals(true, PhoneNumberFormatter.isValid("123"))
        assertEquals(false, PhoneNumberFormatter.isValid(""))
    }
}

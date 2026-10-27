package com.phone.app.core.util

object PhoneNumberFormatter {
    fun formatForDisplay(input: String): String {
        if (input.isEmpty()) return ""
        val prefix = if (input.startsWith("+")) "+" else ""
        val body = if (prefix.isNotEmpty()) input.substring(1) else input
        val sb = StringBuilder(prefix)
        var digitCount = 0
        for (ch in body) {
            sb.append(ch)
            if (ch.isDigit()) {
                digitCount++
                if (digitCount % 3 == 0 && digitCount < body.count { it.isDigit() }) {
                    sb.append(' ')
                }
            }
        }
        return sb.toString().trimEnd()
    }

    fun sanitize(input: String): String {
        // Keep digits, +, *, #, spaces, parentheses, hyphens
        return input.filter { it.isDigit() || it == '+' || it == '*' || it == '#' || it == ' ' || it == '(' || it == ')' || it == '-' }
    }

    fun toDialUri(input: String): String {
        // Remove display-only characters for dialing
        return input.replace(" ", "").replace("(", "").replace(")", "").replace("-", "")
    }

    fun isValid(input: String): Boolean {
        return input.isNotBlank()
    }
}

package com.phone.app.core.platform.spam

interface SpamClassifier {
    fun classify(number: String): Classification
}

data class Classification(val isSpam: Boolean, val source: String, val confidence: Float = 0f)

class RulesBasedClassifier(private val repo: SpamRulesRepository) : SpamClassifier {
    override fun classify(number: String): Classification {
        if (!repo.isSpamFeatureEnabled()) return Classification(false, "disabled")
        if (repo.isBlocked(number)) return Classification(true, "blocklist", 1f)
        for (rule in repo.rules()) {
            if (number.contains(rule)) return Classification(true, "user-rule", 0.8f)
        }
        return Classification(false, "none")
    }
}

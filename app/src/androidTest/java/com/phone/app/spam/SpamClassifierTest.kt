package com.phone.app.spam

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.phone.app.core.platform.spam.RulesBasedClassifier
import com.phone.app.core.platform.spam.SpamRulesRepository
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SpamClassifierTest {
    @Test
    fun blocklistClassifiesSpam() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val repo = SpamRulesRepository(ctx)
        repo.setSpamFeatureEnabled(true)
        repo.block("12345")
        val classifier = RulesBasedClassifier(repo)
        assertTrue(classifier.classify("12345").isSpam)
        repo.unblock("12345")
    }

    @Test
    fun disabledFeatureNotSpam() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val repo = SpamRulesRepository(ctx)
        repo.setSpamFeatureEnabled(false)
        val classifier = RulesBasedClassifier(repo)
        assertFalse(classifier.classify("99999").isSpam)
    }
}

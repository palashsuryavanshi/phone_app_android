package com.phone.app.core.platform.telecom

import android.telecom.Call
import android.telecom.CallScreeningService
import com.phone.app.core.platform.spam.SpamRulesRepository
import com.phone.app.core.platform.spam.RulesBasedClassifier

class PhoneCallScreeningService : CallScreeningService() {
    override fun onScreenCall(callDetails: Call.Details) {
        val number = callDetails.handle?.schemeSpecificPart ?: ""
        val repo = SpamRulesRepository(applicationContext)
        val classifier = RulesBasedClassifier(repo)
        val result = classifier.classify(number)
        val response = CallResponse.Builder()
            .setDisallowCall(result.isSpam)
            .setRejectCall(result.isSpam)
            .setSkipCallLog(false)
            .setSkipNotification(false)
            .build()
        respondToCall(callDetails, response)
    }
}


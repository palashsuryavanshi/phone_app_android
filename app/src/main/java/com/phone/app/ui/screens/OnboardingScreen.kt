package com.phone.app.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phone.app.ui.components.PhonePrimaryButton
import com.phone.app.ui.components.PhoneSecondaryButton
import com.phone.app.ui.theme.PhoneGradients
import com.phone.app.ui.theme.PhoneTheme
import com.phone.app.ui.viewmodel.MainTab
import com.phone.app.ui.viewmodel.PhoneViewModel

data class OnboardingStep(
    val stepIndex: Int,
    val headline: String,
    val subtitle: String,
    val description: String,
    val icon: ImageVector,
    val gradient: Brush
)

@Composable
fun OnboardingScreen(
    viewModel: PhoneViewModel,
    modifier: Modifier = Modifier
) {
    val colors = PhoneTheme.colors

    var currentStep by remember { mutableIntStateOf(0) }

    val steps = listOf(
        OnboardingStep(
            stepIndex = 0,
            headline = "The Next Generation\nPhone Experience",
            subtitle = "INTELLIGENT • CALM • SECURE",
            description = "Welcome to Phone. Engineered for pristine audio clarity, expressive Material 3 visual identity, and zero-compromise privacy.",
            icon = Icons.Default.Call,
            gradient = PhoneGradients.VioletPulse
        ),
        OnboardingStep(
            stepIndex = 1,
            headline = "Effortless Voice\n& Smart Calling",
            subtitle = "CONTINUOUS WAVEFORMS & AUDIO ROUTING",
            description = "Experience immersive active-call interfaces with real-time waveform visualization, multi-route Bluetooth switching, and smart DTMF keypads.",
            icon = Icons.Default.PhoneAndroid,
            gradient = PhoneGradients.ArcticFlow
        ),
        OnboardingStep(
            stepIndex = 2,
            headline = "Privacy First\nSpam Shielding",
            subtitle = "LOCAL-FIRST CALL SCREENING",
            description = "Intelligent spam filtering silences aggressive telemarketers and robocalls before your device rings. No data leaves your phone.",
            icon = Icons.Default.Shield,
            gradient = PhoneGradients.EmeraldNight
        ),
        OnboardingStep(
            stepIndex = 3,
            headline = "Deep Visual\nPersonalization",
            subtitle = "DARK • LIGHT • AMOLED • 5 ACCENTS",
            description = "Tailor every curve, accent hue, and wallpaper to match your mood with true blacks and expressive Material You tokens.",
            icon = Icons.Default.Palette,
            gradient = PhoneGradients.SunsetEmber
        ),
        OnboardingStep(
            stepIndex = 4,
            headline = "Transparent\nPermission Control",
            subtitle = "CLEAR SYSTEM GOVERNANCE",
            description = "Phone requests Phone, Contacts, and Microphone access exclusively for core dialing functionality. Granted on your terms.",
            icon = Icons.Default.Security,
            gradient = PhoneGradients.VioletPulse
        ),
        OnboardingStep(
            stepIndex = 5,
            headline = "Default Phone\nApplication",
            subtitle = "SYSTEM INTEGRATION",
            description = "Set as your primary dialer for full-screen incoming call overlays, lock-screen call controls, and instant quick-replies.",
            icon = Icons.Default.CheckCircle,
            gradient = PhoneGradients.ArcticFlow
        ),
        OnboardingStep(
            stepIndex = 6,
            headline = "You're All Set\nReady to Connect",
            subtitle = "SETUP COMPLETE",
            description = "Your next-generation communication hub is ready. Experience modern calling, refined aesthetics, and intelligent spam protection.",
            icon = Icons.Default.Check,
            gradient = PhoneGradients.VioletPulse
        )
    )

    val step = steps[currentStep]

    Column(
        modifier = modifier
            .testTag("onboarding_screen")
            .fillMaxSize()
            .background(colors.background)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top skip bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "STEP ${currentStep + 1} OF ${steps.size}",
                color = colors.textMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )

            if (currentStep < steps.size - 1) {
                TextButton(
                    onClick = {
                        viewModel.navigateBack()
                        viewModel.setTab(MainTab.KEYPAD)
                    }
                ) {
                    Text("Skip", color = colors.primary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }
        }

        // Center Animated Content
        AnimatedContent(
            targetState = step,
            transitionSpec = {
                (slideInHorizontally { width -> width } + fadeIn())
                    .togetherWith(slideOutHorizontally { width -> -width } + fadeOut())
            },
            label = "onboarding_anim"
        ) { targetStep ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Artwork Container with Organic Gradient
                Box(
                    modifier = Modifier
                        .size(170.dp)
                        .clip(CircleShape)
                        .background(targetStep.gradient)
                        .border(2.dp, colors.outline.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = targetStep.icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(68.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = targetStep.subtitle,
                    color = colors.secondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = targetStep.headline,
                    color = colors.textPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 34.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = targetStep.description,
                    color = colors.textSecondary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }

        // Bottom Navigation & Page Indicators
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Page Dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                steps.indices.forEach { idx ->
                    val isCurrent = idx == currentStep
                    Box(
                        modifier = Modifier
                            .size(if (isCurrent) 22.dp else 8.dp, 8.dp)
                            .clip(CircleShape)
                            .background(if (isCurrent) colors.primary else colors.surfaceTertiary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            if (currentStep < steps.size - 1) {
                PhonePrimaryButton(
                    text = if (currentStep == 0) "Get Started" else "Next Step",
                    onClick = { currentStep++ },
                    testTag = "onboarding_next_button"
                )
            } else {
                PhonePrimaryButton(
                    text = "Enter Phone",
                    onClick = {
                        viewModel.navigateBack()
                        viewModel.setTab(MainTab.KEYPAD)
                        viewModel.showToast("Welcome to Phone!")
                    },
                    testTag = "onboarding_finish_button"
                )
            }
        }
    }
}



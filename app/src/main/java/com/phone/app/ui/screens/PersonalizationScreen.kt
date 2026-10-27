package com.phone.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phone.app.ui.components.PhonePrimaryButton
import com.phone.app.ui.components.PhoneTopBar
import com.phone.app.ui.theme.AccentPalette
import com.phone.app.ui.theme.PhoneGradients
import com.phone.app.ui.theme.PhoneTheme
import com.phone.app.ui.theme.ShapePreset
import com.phone.app.ui.theme.ThemeMode
import com.phone.app.ui.viewmodel.PhoneViewModel

@Composable
fun PersonalizationScreen(
    viewModel: PhoneViewModel,
    modifier: Modifier = Modifier
) {
    val colors = PhoneTheme.colors
    val shapes = MaterialTheme.shapes

    val currentThemeMode by viewModel.repository.themeMode.collectAsState()
    val currentAccent by viewModel.repository.accentPalette.collectAsState()
    val currentShape by viewModel.repository.shapePreset.collectAsState()
    val currentWallpaperIdx by viewModel.repository.selectedWallpaperIndex.collectAsState()

    val wallpapers = listOf(
        Pair("Aurora Night", PhoneGradients.AuroraNight),
        Pair("Violet Pulse", PhoneGradients.VioletPulse),
        Pair("Arctic Flow", PhoneGradients.ArcticFlow),
        Pair("Sunset Ember", PhoneGradients.SunsetEmber),
        Pair("Emerald Night", PhoneGradients.EmeraldNight),
        Pair("Monochrome", PhoneGradients.Monochrome)
    )

    Column(
        modifier = modifier
            .testTag("personalization_screen")
            .fillMaxSize()
            .background(colors.background)
    ) {
        PhoneTopBar(
            title = "Appearance Hub",
            subtitle = "Theme, accents, wallpapers & shapes",
            onBackClick = { viewModel.navigateBack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            // Live Preview Card
            Text(
                text = "Live Appearance Preview",
                color = colors.textPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(shapes.large)
                    .background(wallpapers[currentWallpaperIdx].second)
                    .border(1.5.dp, colors.outline, shapes.large)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PHONE",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(currentAccent.primary)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = currentThemeMode.title,
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shapes.medium)
                            .background(Color.Black.copy(alpha = 0.45f))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(currentAccent.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Elena Rostova",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Connected • 02:45",
                                color = currentAccent.secondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Theme Mode Selector
            Text(
                text = "Color Theme",
                color = colors.textPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ThemeMode.values().forEach { mode ->
                    val isSelected = currentThemeMode == mode
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shapes.small)
                            .background(if (isSelected) colors.primary.copy(alpha = 0.18f) else colors.surfaceSecondary)
                            .border(
                                1.dp,
                                if (isSelected) colors.primary else colors.outline.copy(alpha = 0.4f),
                                shapes.small
                            )
                            .clickable { viewModel.repository.themeMode.value = mode }
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = mode.title,
                            color = if (isSelected) colors.primary else colors.textPrimary,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = colors.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Accent Color Palette
            Text(
                text = "Accent Color",
                color = colors.textPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                itemsIndexed(AccentPalette.values()) { _, palette ->
                    val isSelected = currentAccent == palette
                    Column(
                        modifier = Modifier
                            .clickable { viewModel.repository.accentPalette.value = palette }
                            .padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(palette.primary)
                                .border(
                                    if (isSelected) 3.dp else 1.dp,
                                    if (isSelected) Color.White else colors.outline,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = palette.label.split(" ").last(),
                            color = if (isSelected) colors.textPrimary else colors.textMuted,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Shape Scale
            Text(
                text = "Control Shapes & Radius",
                color = colors.textPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ShapePreset.values().forEach { preset ->
                    val isSelected = currentShape == preset
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(shapes.small)
                            .background(if (isSelected) colors.primary else colors.surfaceSecondary)
                            .border(1.dp, if (isSelected) colors.primary else colors.outline, shapes.small)
                            .clickable { viewModel.repository.shapePreset.value = preset },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = preset.displayName.split(" ").first(),
                            color = if (isSelected) Color.White else colors.textPrimary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Wallpaper Gallery
            Text(
                text = "Atmospheric Wallpapers",
                color = colors.textPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                itemsIndexed(wallpapers) { idx, item ->
                    val isSelected = currentWallpaperIdx == idx
                    Column(
                        modifier = Modifier
                            .clickable { viewModel.repository.selectedWallpaperIndex.value = idx }
                            .padding(2.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 72.dp, height = 96.dp)
                                .clip(shapes.medium)
                                .background(item.second)
                                .border(
                                    if (isSelected) 2.5.dp else 1.dp,
                                    if (isSelected) colors.primary else colors.outline,
                                    shapes.medium
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(colors.primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.first.split(" ").first(),
                            color = if (isSelected) colors.primary else colors.textMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            PhonePrimaryButton(
                text = "Apply Appearance",
                onClick = {
                    viewModel.showToast("Appearance settings saved")
                    viewModel.navigateBack()
                },
                testTag = "save_appearance_button"
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}



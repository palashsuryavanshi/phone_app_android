package com.phone.app.core.designsystem.shape

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

// Default corner radii
val CornerRadiusXxs = 4.dp
val CornerRadiusXs = 8.dp
val CornerRadiusSm = 12.dp
val CornerRadiusMd = 16.dp
val CornerRadiusLg = 24.dp
val CornerRadiusXl = 32.dp
val CornerRadiusXxl = 48.dp
val CornerRadiusPill = 100.dp

// Default shapes for components
val ShapeExtraSmall = RoundedCornerShape(CornerRadiusXxs)
val ShapeSmall = RoundedCornerShape(CornerRadiusXs)
val ShapeMedium = RoundedCornerShape(CornerRadiusSm)
val ShapeLarge = RoundedCornerShape(CornerRadiusMd)
val ShapeXLarge = RoundedCornerShape(CornerRadiusLg)
val ShapeXxLarge = RoundedCornerShape(CornerRadiusXl)
val ShapePill = RoundedCornerShape(CornerRadiusPill)

// Dialpad button shapes
val DialpadButtonShape = RoundedCornerShape(CornerRadiusLg)

// Contact card shapes
val ContactCardShape = RoundedCornerShape(CornerRadiusLg)

// Navigation shapes
val NavBarShape = RoundedCornerShape(topStart = CornerRadiusSm, topEnd = CornerRadiusSm)
val NavRailShape = RoundedCornerShape(CornerRadiusMd)

// Button shapes
val ButtonShape = RoundedCornerShape(CornerRadiusMd)
val FabShape = RoundedCornerShape(CornerRadiusXl)

// Surface shapes
val SurfaceShape = RoundedCornerShape(CornerRadiusMd)
val ElevatedSurfaceShape = RoundedCornerShape(CornerRadiusMd)

// Avatar shapes
val AvatarCircle = RoundedCornerShape(100.dp)
val AvatarRounded = RoundedCornerShape(CornerRadiusMd)
val AvatarSoft = RoundedCornerShape(CornerRadiusSm)

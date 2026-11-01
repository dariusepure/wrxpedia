package com.dariusepure.wrxpedia.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GenerationFrontIllustration(
    iconName: String,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = this.size.width
        val h = this.size.height

        when (iconName.lowercase()) {
            "classic" -> drawClassicFrontFascia(w, h)
            "bugeye" -> drawBugeyeFrontFascia(w, h)
            "blobeye" -> drawBlobeyeFrontFascia(w, h)
            "hawkeye" -> drawHawkeyeFrontFascia(w, h)
            "hatchback", "sedan" -> drawStinkeyeFrontFascia(w, h)
            "va", "raptoreye" -> drawRaptorEyeFrontFascia(w, h)
            "vb" -> drawVbFrontFascia(w, h)
            else -> drawRaptorEyeFrontFascia(w, h)
        }
    }
}

@Composable
fun GenerationSilhouetteBadge(
    iconName: String,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E293B),
                        Color(0xFF0F172A)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        GenerationFrontIllustration(
            iconName = iconName,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(size * 0.65f)
        )
    }
}

// 1. Meaneye / Classic (1993-2000 GC8) - Wide Aspect Ratio Front View
private fun DrawScope.drawClassicFrontFascia(w: Float, h: Float) {
    val bodyBlue = Color(0xFF003399)

    // Windshield & Roof
    val roof = Path().apply {
        moveTo(w * 0.28f, h * 0.32f)
        lineTo(w * 0.38f, h * 0.10f)
        lineTo(w * 0.62f, h * 0.10f)
        lineTo(w * 0.72f, h * 0.32f)
        close()
    }
    drawPath(roof, Color(0xFF80D8FF).copy(alpha = 0.7f))

    // Car Body Base
    val body = Path().apply {
        moveTo(w * 0.10f, h * 0.32f)
        lineTo(w * 0.26f, h * 0.18f)
        lineTo(w * 0.74f, h * 0.18f)
        lineTo(w * 0.90f, h * 0.32f)
        lineTo(w * 0.94f, h * 0.85f)
        lineTo(w * 0.06f, h * 0.85f)
        close()
    }
    drawPath(body, bodyBlue)

    // Hood Scoop
    val scoop = Path().apply {
        moveTo(w * 0.38f, h * 0.18f)
        lineTo(w * 0.62f, h * 0.18f)
        lineTo(w * 0.58f, h * 0.06f)
        lineTo(w * 0.42f, h * 0.06f)
        close()
    }
    drawPath(scoop, Color.Black.copy(alpha = 0.85f))

    // Rectangular Glass Headlights
    drawRoundRect(color = Color.White, topLeft = Offset(w * 0.12f, h * 0.35f), size = Size(w * 0.24f, h * 0.16f), cornerRadius = CornerRadius(2f, 2f))
    drawRoundRect(color = Color(0xFFFF9800), topLeft = Offset(w * 0.12f, h * 0.35f), size = Size(w * 0.05f, h * 0.16f), cornerRadius = CornerRadius(2f, 2f))

    drawRoundRect(color = Color.White, topLeft = Offset(w * 0.64f, h * 0.35f), size = Size(w * 0.24f, h * 0.16f), cornerRadius = CornerRadius(2f, 2f))
    drawRoundRect(color = Color(0xFFFF9800), topLeft = Offset(w * 0.83f, h * 0.35f), size = Size(w * 0.05f, h * 0.16f), cornerRadius = CornerRadius(2f, 2f))

    // Central Grille & Badge
    drawRoundRect(color = Color.Black.copy(alpha = 0.9f), topLeft = Offset(w * 0.38f, h * 0.37f), size = Size(w * 0.24f, h * 0.12f), cornerRadius = CornerRadius(2f, 2f))
    drawCircle(color = Color(0xFF003399), radius = w * 0.025f, center = Offset(w * 0.50f, h * 0.43f))

    // Iconic Large Round STI Fog Lamp Covers
    drawCircle(color = Color.White, radius = w * 0.10f, center = Offset(w * 0.22f, h * 0.64f))
    drawRect(color = Color(0xFFE91E63), topLeft = Offset(w * 0.16f, h * 0.62f), size = Size(w * 0.12f, h * 0.04f))

    drawCircle(color = Color.White, radius = w * 0.10f, center = Offset(w * 0.78f, h * 0.64f))
    drawRect(color = Color(0xFFE91E63), topLeft = Offset(w * 0.72f, h * 0.62f), size = Size(w * 0.12f, h * 0.04f))

    // Bumper Lower Intake
    drawRoundRect(color = Color.Black.copy(alpha = 0.7f), topLeft = Offset(w * 0.36f, h * 0.66f), size = Size(w * 0.28f, h * 0.10f), cornerRadius = CornerRadius(2f, 2f))
}

// 2. Bugeye (2001-2002 GD) - Wide Aspect Ratio
private fun DrawScope.drawBugeyeFrontFascia(w: Float, h: Float) {
    val bodySilver = Color(0xFFB0BEC5)

    // Windshield
    val roof = Path().apply {
        moveTo(w * 0.28f, h * 0.32f)
        lineTo(w * 0.38f, h * 0.10f)
        lineTo(w * 0.62f, h * 0.10f)
        lineTo(w * 0.72f, h * 0.32f)
        close()
    }
    drawPath(roof, Color(0xFF80D8FF).copy(alpha = 0.75f))

    // Body
    val body = Path().apply {
        moveTo(w * 0.08f, h * 0.34f)
        cubicTo(w * 0.18f, h * 0.18f, w * 0.35f, h * 0.16f, w * 0.50f, h * 0.16f)
        cubicTo(w * 0.65f, h * 0.16f, w * 0.82f, h * 0.18f, w * 0.92f, h * 0.34f)
        lineTo(w * 0.95f, h * 0.85f)
        lineTo(w * 0.05f, h * 0.85f)
        close()
    }
    drawPath(body, bodySilver)

    // Tall Bugeye STI Hood Scoop
    val scoop = Path().apply {
        moveTo(w * 0.35f, h * 0.16f)
        lineTo(w * 0.65f, h * 0.16f)
        lineTo(w * 0.60f, h * 0.02f)
        lineTo(w * 0.40f, h * 0.02f)
        close()
    }
    drawPath(scoop, Color.Black.copy(alpha = 0.85f))

    // Round Bug Headlights (Left & Right)
    drawCircle(color = Color.Black, radius = w * 0.12f, center = Offset(w * 0.24f, h * 0.42f))
    drawCircle(color = Color.White, radius = w * 0.09f, center = Offset(w * 0.24f, h * 0.42f))
    drawCircle(color = Color.Black, radius = w * 0.04f, center = Offset(w * 0.24f, h * 0.42f))
    drawCircle(color = Color(0xFFFF9800), radius = w * 0.03f, center = Offset(w * 0.14f, h * 0.42f))

    drawCircle(color = Color.Black, radius = w * 0.12f, center = Offset(w * 0.76f, h * 0.42f))
    drawCircle(color = Color.White, radius = w * 0.09f, center = Offset(w * 0.76f, h * 0.42f))
    drawCircle(color = Color.Black, radius = w * 0.04f, center = Offset(w * 0.76f, h * 0.42f))
    drawCircle(color = Color(0xFFFF9800), radius = w * 0.03f, center = Offset(w * 0.86f, h * 0.42f))

    // Center Oval Grille
    val grille = Path().apply {
        moveTo(w * 0.38f, h * 0.40f)
        lineTo(w * 0.62f, h * 0.40f)
        lineTo(w * 0.58f, h * 0.56f)
        lineTo(w * 0.42f, h * 0.56f)
        close()
    }
    drawPath(grille, Color.Black.copy(alpha = 0.85f))
    drawCircle(color = Color(0xFF003399), radius = w * 0.03f, center = Offset(w * 0.50f, h * 0.48f))

    // Bumper Side Covers
    drawRoundRect(color = Color.White.copy(alpha = 0.85f), topLeft = Offset(w * 0.14f, h * 0.62f), size = Size(w * 0.16f, h * 0.12f), cornerRadius = CornerRadius(2f, 2f))
    drawRoundRect(color = Color.White.copy(alpha = 0.85f), topLeft = Offset(w * 0.70f, h * 0.62f), size = Size(w * 0.16f, h * 0.12f), cornerRadius = CornerRadius(2f, 2f))

    // Lower Bumper Intake
    drawRoundRect(color = Color.Black.copy(alpha = 0.7f), topLeft = Offset(w * 0.32f, h * 0.64f), size = Size(w * 0.36f, h * 0.12f), cornerRadius = CornerRadius(3f, 3f))
}

// 3. Blobeye (2003-2005 GD) - Wide Aspect Ratio
private fun DrawScope.drawBlobeyeFrontFascia(w: Float, h: Float) {
    val bodyWhite = Color(0xFFECEFF1)

    val roof = Path().apply {
        moveTo(w * 0.28f, h * 0.32f)
        lineTo(w * 0.38f, h * 0.10f)
        lineTo(w * 0.62f, h * 0.10f)
        lineTo(w * 0.72f, h * 0.32f)
        close()
    }
    drawPath(roof, Color(0xFF80D8FF).copy(alpha = 0.75f))

    val body = Path().apply {
        moveTo(w * 0.08f, h * 0.34f)
        cubicTo(w * 0.18f, h * 0.18f, w * 0.35f, h * 0.16f, w * 0.50f, h * 0.16f)
        cubicTo(w * 0.65f, h * 0.16f, w * 0.82f, h * 0.18f, w * 0.92f, h * 0.34f)
        lineTo(w * 0.95f, h * 0.85f)
        lineTo(w * 0.05f, h * 0.85f)
        close()
    }
    drawPath(body, bodyWhite)

    // High STI Hood Scoop
    val scoop = Path().apply {
        moveTo(w * 0.35f, h * 0.16f)
        lineTo(w * 0.65f, h * 0.16f)
        lineTo(w * 0.60f, h * 0.03f)
        lineTo(w * 0.40f, h * 0.03f)
        close()
    }
    drawPath(scoop, Color.Black.copy(alpha = 0.85f))

    // Teardrop / Blob Headlights
    val leftBlob = Path().apply {
        moveTo(w * 0.12f, h * 0.36f)
        cubicTo(w * 0.26f, h * 0.34f, w * 0.36f, h * 0.42f, w * 0.34f, h * 0.56f)
        cubicTo(w * 0.24f, h * 0.58f, w * 0.12f, h * 0.52f, w * 0.12f, h * 0.36f)
        close()
    }
    drawPath(leftBlob, Color.Black)
    val leftBlobLens = Path().apply {
        moveTo(w * 0.14f, h * 0.38f)
        cubicTo(w * 0.25f, h * 0.36f, w * 0.34f, h * 0.43f, w * 0.32f, h * 0.54f)
        cubicTo(w * 0.23f, h * 0.56f, w * 0.14f, h * 0.48f, w * 0.14f, h * 0.38f)
        close()
    }
    drawPath(leftBlobLens, Color.White)

    val rightBlob = Path().apply {
        moveTo(w * 0.88f, h * 0.36f)
        cubicTo(w * 0.74f, h * 0.34f, w * 0.64f, h * 0.42f, w * 0.66f, h * 0.56f)
        cubicTo(w * 0.76f, h * 0.58f, w * 0.88f, h * 0.52f, w * 0.88f, h * 0.36f)
        close()
    }
    drawPath(rightBlob, Color.Black)
    val rightBlobLens = Path().apply {
        moveTo(w * 0.86f, h * 0.38f)
        cubicTo(w * 0.75f, h * 0.36f, w * 0.66f, h * 0.43f, w * 0.68f, h * 0.54f)
        cubicTo(w * 0.77f, h * 0.56f, w * 0.86f, h * 0.48f, w * 0.86f, h * 0.38f)
        close()
    }
    drawPath(rightBlobLens, Color.White)

    // Center Grille
    drawRoundRect(color = Color.Black.copy(alpha = 0.85f), topLeft = Offset(w * 0.36f, h * 0.38f), size = Size(w * 0.28f, h * 0.16f), cornerRadius = CornerRadius(3f, 3f))
    drawCircle(color = Color(0xFF003399), radius = w * 0.03f, center = Offset(w * 0.50f, h * 0.46f))

    // Bumper Side Covers
    drawCircle(color = Color.White, radius = w * 0.07f, center = Offset(w * 0.18f, h * 0.68f))
    drawCircle(color = Color.White, radius = w * 0.07f, center = Offset(w * 0.82f, h * 0.68f))
}

// 4. Hawkeye (2006-2007 GD) - Wide Aspect Ratio
private fun DrawScope.drawHawkeyeFrontFascia(w: Float, h: Float) {
    val bodyGrey = Color(0xFFB0BEC5)

    val roof = Path().apply {
        moveTo(w * 0.28f, h * 0.32f)
        lineTo(w * 0.38f, h * 0.10f)
        lineTo(w * 0.62f, h * 0.10f)
        lineTo(w * 0.72f, h * 0.32f)
        close()
    }
    drawPath(roof, Color(0xFF80D8FF).copy(alpha = 0.75f))

    val body = Path().apply {
        moveTo(w * 0.08f, h * 0.34f)
        lineTo(w * 0.22f, h * 0.18f)
        lineTo(w * 0.78f, h * 0.18f)
        lineTo(w * 0.92f, h * 0.34f)
        lineTo(w * 0.95f, h * 0.85f)
        lineTo(w * 0.05f, h * 0.85f)
        close()
    }
    drawPath(body, bodyGrey)

    // Hood scoop
    val scoop = Path().apply {
        moveTo(w * 0.34f, h * 0.18f)
        lineTo(w * 0.66f, h * 0.18f)
        lineTo(w * 0.61f, h * 0.08f)
        lineTo(w * 0.39f, h * 0.08f)
        close()
    }
    drawPath(scoop, Color.Black.copy(alpha = 0.85f))

    // Sharp Hawk Headlights
    val leftHawk = Path().apply {
        moveTo(w * 0.10f, h * 0.36f)
        lineTo(w * 0.32f, h * 0.34f)
        lineTo(w * 0.28f, h * 0.52f)
        lineTo(w * 0.14f, h * 0.50f)
        close()
    }
    drawPath(leftHawk, Color.White, style = Fill)

    val rightHawk = Path().apply {
        moveTo(w * 0.90f, h * 0.36f)
        lineTo(w * 0.68f, h * 0.34f)
        lineTo(w * 0.72f, h * 0.52f)
        lineTo(w * 0.86f, h * 0.50f)
        close()
    }
    drawPath(rightHawk, Color.White, style = Fill)

    // 3-Piece Spread Wings Center Grille
    val centerIntake = Path().apply {
        moveTo(w * 0.40f, h * 0.36f)
        lineTo(w * 0.60f, h * 0.36f)
        lineTo(w * 0.55f, h * 0.56f)
        lineTo(w * 0.45f, h * 0.56f)
        close()
    }
    drawPath(centerIntake, Color.Black.copy(alpha = 0.85f), style = Fill)

    // Side Wing Grilles
    drawRect(color = Color.Black.copy(alpha = 0.7f), topLeft = Offset(w * 0.33f, h * 0.38f), size = Size(w * 0.06f, h * 0.12f))
    drawRect(color = Color.Black.copy(alpha = 0.7f), topLeft = Offset(w * 0.61f, h * 0.38f), size = Size(w * 0.06f, h * 0.12f))

    // Bumper Lower Intake
    drawRoundRect(color = Color.Black.copy(alpha = 0.6f), topLeft = Offset(w * 0.28f, h * 0.64f), size = Size(w * 0.44f, h * 0.12f), cornerRadius = CornerRadius(2f, 2f))
}

// 5. Stinkeye (2008-2014 GR/GV) - Wide Aspect Ratio
private fun DrawScope.drawStinkeyeFrontFascia(w: Float, h: Float) {
    val bodyDark = Color(0xFF263238)

    val roof = Path().apply {
        moveTo(w * 0.28f, h * 0.32f)
        lineTo(w * 0.38f, h * 0.10f)
        lineTo(w * 0.62f, h * 0.10f)
        lineTo(w * 0.72f, h * 0.32f)
        close()
    }
    drawPath(roof, Color(0xFF80D8FF).copy(alpha = 0.75f))

    // Body with Widebody Fenders
    val body = Path().apply {
        moveTo(w * 0.02f, h * 0.44f)
        lineTo(w * 0.14f, h * 0.32f)
        lineTo(w * 0.28f, h * 0.18f)
        lineTo(w * 0.72f, h * 0.18f)
        lineTo(w * 0.86f, h * 0.32f)
        lineTo(w * 0.98f, h * 0.44f)
        lineTo(w * 0.98f, h * 0.85f)
        lineTo(w * 0.02f, h * 0.85f)
        close()
    }
    drawPath(body, bodyDark)

    // Wide low-profile Hood Scoop
    val scoop = Path().apply {
        moveTo(w * 0.30f, h * 0.18f)
        lineTo(w * 0.70f, h * 0.18f)
        lineTo(w * 0.64f, h * 0.08f)
        lineTo(w * 0.36f, h * 0.08f)
        close()
    }
    drawPath(scoop, Color.Black.copy(alpha = 0.85f))

    // Slanted Headlights
    val leftHeadlight = Path().apply {
        moveTo(w * 0.11f, h * 0.34f)
        lineTo(w * 0.34f, h * 0.32f)
        lineTo(w * 0.30f, h * 0.50f)
        lineTo(w * 0.15f, h * 0.50f)
        close()
    }
    drawPath(leftHeadlight, Color.White)

    val rightHeadlight = Path().apply {
        moveTo(w * 0.89f, h * 0.34f)
        lineTo(w * 0.66f, h * 0.32f)
        lineTo(w * 0.70f, h * 0.50f)
        lineTo(w * 0.85f, h * 0.50f)
        close()
    }
    drawPath(rightHeadlight, Color.White)

    // Mesh Upper Grille with Top Bar
    drawRoundRect(color = Color.Black.copy(alpha = 0.9f), topLeft = Offset(w * 0.35f, h * 0.34f), size = Size(w * 0.30f, h * 0.16f), cornerRadius = CornerRadius(2f, 2f))
    drawRect(color = Color.White.copy(alpha = 0.75f), topLeft = Offset(w * 0.35f, h * 0.34f), size = Size(w * 0.30f, h * 0.04f))

    // Lower Bumper Intake
    drawRoundRect(color = Color.Black.copy(alpha = 0.7f), topLeft = Offset(w * 0.26f, h * 0.62f), size = Size(w * 0.48f, h * 0.12f), cornerRadius = CornerRadius(3f, 3f))
}

// 6. Raptor Eye / Evo Eye (2015-2021 VA) - Wide Aspect Ratio
private fun DrawScope.drawRaptorEyeFrontFascia(w: Float, h: Float) {
    val bodyBlue = Color(0xFF003399)

    // Windshield
    val roof = Path().apply {
        moveTo(w * 0.28f, h * 0.32f)
        lineTo(w * 0.38f, h * 0.10f)
        lineTo(w * 0.62f, h * 0.10f)
        lineTo(w * 0.72f, h * 0.32f)
        close()
    }
    drawPath(roof, Color(0xFF80D8FF).copy(alpha = 0.75f))

    // Car Body
    val body = Path().apply {
        moveTo(w * 0.05f, h * 0.38f)
        lineTo(w * 0.18f, h * 0.18f)
        lineTo(w * 0.82f, h * 0.18f)
        lineTo(w * 0.95f, h * 0.38f)
        lineTo(w * 0.97f, h * 0.85f)
        lineTo(w * 0.03f, h * 0.85f)
        close()
    }
    drawPath(body, bodyBlue)

    // Low-profile integrated Hood Scoop
    val scoop = Path().apply {
        moveTo(w * 0.32f, h * 0.18f)
        lineTo(w * 0.68f, h * 0.18f)
        lineTo(w * 0.63f, h * 0.10f)
        lineTo(w * 0.37f, h * 0.10f)
        close()
    }
    drawPath(scoop, Color.Black.copy(alpha = 0.85f))

    // RAPTOR EYE C-LIGHT HEADLIGHTS (Left Lens)
    val leftHead = Path().apply {
        moveTo(w * 0.10f, h * 0.34f)
        lineTo(w * 0.33f, h * 0.32f)
        lineTo(w * 0.30f, h * 0.50f)
        lineTo(w * 0.14f, h * 0.48f)
        close()
    }
    drawPath(leftHead, Color.White)

    // C-Light LED DRL Ring (Left Glow)
    val leftCLight = Path().apply {
        moveTo(w * 0.30f, h * 0.34f)
        lineTo(w * 0.14f, h * 0.36f)
        lineTo(w * 0.16f, h * 0.46f)
        lineTo(w * 0.28f, h * 0.46f)
    }
    drawPath(leftCLight, Color(0xFF00E5FF), style = Stroke(width = 3.5f))

    // RAPTOR EYE C-LIGHT HEADLIGHTS (Right Lens)
    val rightHead = Path().apply {
        moveTo(w * 0.90f, h * 0.34f)
        lineTo(w * 0.67f, h * 0.32f)
        lineTo(w * 0.70f, h * 0.50f)
        lineTo(w * 0.86f, h * 0.48f)
        close()
    }
    drawPath(rightHead, Color.White)

    // C-Light LED DRL Ring (Right Glow)
    val rightCLight = Path().apply {
        moveTo(w * 0.70f, h * 0.34f)
        lineTo(w * 0.86f, h * 0.36f)
        lineTo(w * 0.84f, h * 0.46f)
        lineTo(w * 0.72f, h * 0.46f)
    }
    drawPath(rightCLight, Color(0xFF00E5FF), style = Stroke(width = 3.5f))

    // Hexagonal Raptor Grille
    val hexGrille = Path().apply {
        moveTo(w * 0.35f, h * 0.32f)
        lineTo(w * 0.65f, h * 0.32f)
        lineTo(w * 0.60f, h * 0.56f)
        lineTo(w * 0.40f, h * 0.56f)
        close()
    }
    drawPath(hexGrille, Color.Black.copy(alpha = 0.9f))
    drawCircle(color = Color(0xFF003399), radius = w * 0.03f, center = Offset(w * 0.50f, h * 0.42f))

    // Lower Bumper Intake
    drawRoundRect(color = Color.Black.copy(alpha = 0.7f), topLeft = Offset(w * 0.25f, h * 0.62f), size = Size(w * 0.50f, h * 0.12f), cornerRadius = CornerRadius(2f, 2f))
}

// 7. VB Generation (2022-Present VB) - Wide Aspect Ratio
private fun DrawScope.drawVbFrontFascia(w: Float, h: Float) {
    val bodyOrange = Color(0xFFE65100)

    val roof = Path().apply {
        moveTo(w * 0.28f, h * 0.32f)
        lineTo(w * 0.38f, h * 0.10f)
        lineTo(w * 0.62f, h * 0.10f)
        lineTo(w * 0.72f, h * 0.32f)
        close()
    }
    drawPath(roof, Color(0xFF80D8FF).copy(alpha = 0.75f))

    // Body with Outer Fender Cladding
    val body = Path().apply {
        moveTo(w * 0.04f, h * 0.42f)
        lineTo(w * 0.18f, h * 0.18f)
        lineTo(w * 0.82f, h * 0.18f)
        lineTo(w * 0.96f, h * 0.42f)
        lineTo(w * 0.98f, h * 0.85f)
        lineTo(w * 0.02f, h * 0.85f)
        close()
    }
    drawPath(body, bodyOrange)

    // Unpainted Fender Arch Plastic Cladding (Black outer sides)
    drawRect(color = Color.Black.copy(alpha = 0.7f), topLeft = Offset(w * 0.02f, h * 0.42f), size = Size(w * 0.08f, h * 0.38f))
    drawRect(color = Color.Black.copy(alpha = 0.7f), topLeft = Offset(w * 0.90f, h * 0.42f), size = Size(w * 0.08f, h * 0.38f))

    // Low flush hood scoop
    val scoop = Path().apply {
        moveTo(w * 0.35f, h * 0.18f)
        lineTo(w * 0.65f, h * 0.18f)
        lineTo(w * 0.60f, h * 0.10f)
        lineTo(w * 0.40f, h * 0.10f)
        close()
    }
    drawPath(scoop, Color.Black.copy(alpha = 0.85f))

    // Sharp Angular C-LED Headlights
    val leftHead = Path().apply {
        moveTo(w * 0.13f, h * 0.34f)
        lineTo(w * 0.33f, h * 0.32f)
        lineTo(w * 0.30f, h * 0.48f)
        lineTo(w * 0.16f, h * 0.46f)
        close()
    }
    drawPath(leftHead, Color.White)

    val rightHead = Path().apply {
        moveTo(w * 0.87f, h * 0.34f)
        lineTo(w * 0.67f, h * 0.32f)
        lineTo(w * 0.70f, h * 0.48f)
        lineTo(w * 0.84f, h * 0.46f)
        close()
    }
    drawPath(rightHead, Color.White)

    // Bold 3D Hexagonal Grille
    val hexGrille = Path().apply {
        moveTo(w * 0.35f, h * 0.32f)
        lineTo(w * 0.65f, h * 0.32f)
        lineTo(w * 0.58f, h * 0.60f)
        lineTo(w * 0.42f, h * 0.60f)
        close()
    }
    drawPath(hexGrille, Color.Black.copy(alpha = 0.9f))
    drawCircle(color = Color(0xFF003399), radius = w * 0.03f, center = Offset(w * 0.50f, h * 0.42f))

    // Lower Bumper Intake
    drawRoundRect(color = Color.Black.copy(alpha = 0.7f), topLeft = Offset(w * 0.28f, h * 0.64f), size = Size(w * 0.44f, h * 0.10f), cornerRadius = CornerRadius(2f, 2f))
}

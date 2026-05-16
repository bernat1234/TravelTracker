package com.example.traveltracker

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class CurvaNavigationBar : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        val path = Path().apply {
            val width = size.width
            val height = size.height

            moveTo(0f, 0f)

            lineTo(width * 0.35f, 0f)

            cubicTo(
                width * 0.40f, 0f,
                width * 0.40f, height * 0.35f,
                width * 0.50f, height * 0.35f
            )

            cubicTo(
                width * 0.60f, height * 0.35f,
                width * 0.60f, 0f,
                width * 0.65f, 0f
            )

            lineTo(width, 0f)
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }

        return Outline.Generic(path)
    }
}
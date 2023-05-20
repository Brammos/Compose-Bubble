package com.smarttoolfactory.speechbubble

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

/**
 * Retrieve rectangle for measuring for space to be used content other than arrow itself.
 *
 * @param bubbleState state that contains bubble properties
 * @param rectContent we store position and dimensions for space required for
 * drawing rounded rectangle without arrow
 * @param width is the total width reserved for content and arrow if available in horizontal position
 * @param height is the total height reserved for content and arrow if available in vertical position
 */
internal fun setContentRect(
    bubbleState: BubbleState,
    rectContent: BubbleRect,
    width: Int,
    height: Int,
    density: Float
) {

    val isHorizontalRightAligned = bubbleState.isHorizontalRightAligned()
    val isHorizontalLeftAligned = bubbleState.isHorizontalLeftAligned()
    val isVerticalBottomAligned = bubbleState.isVerticalBottomAligned()

    val arrowWidth = bubbleState.arrowWidth.value * density
    val arrowHeight = bubbleState.arrowHeight.value * density

    when {
        isHorizontalLeftAligned -> {
            rectContent.set(
                left = arrowWidth,
                top = 0f,
                right = width.toFloat(),
                bottom = height.toFloat()
            )

        }

        isHorizontalRightAligned -> {
            rectContent.set(
                0f,
                0f,
                width.toFloat() - arrowWidth,
                height.toFloat()
            )

        }

        isVerticalBottomAligned -> {
            rectContent.set(
                0f,
                0f,
                width.toFloat(),
                height.toFloat() - arrowHeight
            )
        }

        else -> {
            rectContent.set(
                0f,
                0f,
                width.toFloat(),
                height.toFloat()
            )
        }
    }
}

internal fun Path.addRoundedRect(
    radiusTopLeft: Float,
    radiusTopRight: Float,
    radiusBottomRight: Float,
    radiusBottomLeft: Float,
    topLeft: Offset,
    size: Size
) {
    val topLeftRadius = radiusTopLeft * 2
    val topRightRadius = radiusTopRight * 2
    val bottomRightRadius = radiusBottomRight * 2
    val bottomLeftRadius = radiusBottomLeft * 2

    val width = size.width
    val height = size.height

    // Top left arc
    arcTo(
        rect = Rect(
            left = topLeft.x,
            top = topLeft.y,
            right = topLeft.x + topLeftRadius,
            bottom = topLeft.y + topLeftRadius
        ),
        startAngleDegrees = 180.0f,
        sweepAngleDegrees = 90.0f,
        forceMoveTo = false
    )

    lineTo(x = topLeft.x + width - topRightRadius, y = topLeft.y)

    // Top right arc
    arcTo(
        rect = Rect(
            left = topLeft.x + width - topRightRadius,
            top = topLeft.y,
            right = topLeft.x + width,
            bottom = topLeft.y + topRightRadius
        ),
        startAngleDegrees = -90.0f,
        sweepAngleDegrees = 90.0f,
        forceMoveTo = false
    )

    lineTo(x = topLeft.x + width, y = topLeft.y + height - bottomRightRadius)

    // Bottom right arc
    arcTo(
        rect = Rect(
            left = topLeft.x + width - bottomRightRadius,
            top = topLeft.y + height - bottomRightRadius,
            right = topLeft.x + width,
            bottom = topLeft.y + height
        ),
        startAngleDegrees = 0f,
        sweepAngleDegrees = 90.0f,
        forceMoveTo = false
    )

    lineTo(x = topLeft.x + bottomLeftRadius, y = topLeft.y + height)

    // Bottom left arc
    arcTo(
        rect = Rect(
            left = topLeft.x,
            top = topLeft.y + height - bottomLeftRadius,
            right = topLeft.x + bottomLeftRadius,
            bottom = topLeft.y + height
        ),
        startAngleDegrees = 90.0f,
        sweepAngleDegrees = 90.0f,
        forceMoveTo = false
    )

    lineTo(x = topLeft.x, y = topLeft.y + topLeftRadius)
    close()
}

fun Color.darkenColor(factor: Float): Color {

    val colorFactor = factor.coerceAtMost(1f)
    return copy(
        alpha,
        red * colorFactor,
        green * colorFactor,
        blue * colorFactor
    )
}
package com.doool.exclude_font_padding

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.AndroidPaint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.resolveDefaults
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.core.content.res.ResourcesCompat

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    includeFontPadding: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    Text(
        text,
        if (!includeFontPadding) {
            val mergedStyle = resolveDefaults(
                style.merge(
                    TextStyle(
                        fontSize = fontSize,
                        fontWeight = fontWeight,
                        fontFamily = fontFamily,
                        fontStyle = fontStyle,
                    )
                ), LocalLayoutDirection.current
            )
            modifier.excludeFontPadding(mergedStyle)
        } else modifier,
        color,
        fontSize,
        fontStyle,
        fontWeight,
        fontFamily,
        letterSpacing,
        textDecoration,
        textAlign,
        lineHeight,
        overflow,
        softWrap,
        maxLines,
        onTextLayout,
        style
    )
}

fun Modifier.excludeFontPadding(style: TextStyle): Modifier = composed(debugInspectorInfo {
    name = "removeFontPadding"
    properties["style"] = style
}) {
    val fontFamily = style.fontFamily

    val context = LocalContext.current
    val density = LocalDensity.current

    val (topPadding, bottomPadding) = remember {
        val fontResId = if (fontFamily is FontListFontFamily) {
            (fontFamily.fonts.firstOrNull { it.weight == style.fontWeight } as? ResourceFont)?.resId
        } else null

        AndroidPaint().asFrameworkPaint().run {
            if (fontResId != null) typeface = ResourcesCompat.getFont(context, fontResId)
            textSize = density.run { style.fontSize.toPx() }
            Pair(fontMetrics.ascent - fontMetrics.top, fontMetrics.bottom - fontMetrics.descent)
        }
    }

    Modifier
        .clip(RectangleShape)
        .layout { measurable, constraints ->
            val maxHeight = constraints.maxHeight + topPadding.toInt() + bottomPadding.toInt()
            val placeable =
                measurable.measure(constraints = constraints.copy(maxHeight = maxHeight))

            layout(
                placeable.width,
                placeable.height - topPadding.toInt() - bottomPadding.toInt()
            ) {
                placeable.place(0, -topPadding.toInt())
            }
        }
}
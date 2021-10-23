package com.doool.exclude_font_padding

import android.graphics.Typeface
import android.text.TextPaint
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontLoader
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontListFontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.resolveDefaults
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.TextUnit

@Composable
fun Text(
	text: AnnotatedString,
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
	inlineContent: Map<String, InlineTextContent> = mapOf(),
	onTextLayout: (TextLayoutResult) -> Unit = {},
	style: TextStyle = LocalTextStyle.current
) {
	var isOverflow by remember { mutableStateOf(false) }

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
			modifier.excludeFontPadding(mergedStyle, isOverflow)
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
		inlineContent,
		{
			isOverflow = overflow != TextOverflow.Ellipsis && it.didOverflowHeight
			onTextLayout(it)
		},
		style
	)
}

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
		AnnotatedString(text),
		modifier,
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
		includeFontPadding,
		maxLines,
		emptyMap(),
		onTextLayout,
		style
	)
}


fun Modifier.excludeFontPadding(style: TextStyle, isOverflow: Boolean): Modifier = composed(debugInspectorInfo {
	name = "removeFontPadding"
	properties["style"] = style
}) {
	val density = LocalDensity.current
	val fontLoader = LocalFontLoader.current

	val (topPadding, bottomPadding) = remember(style) {
		val fontFamily = style.fontFamily
		if (fontFamily !is FontListFontFamily) Pair(0, 0)
		else {
			val font = fontFamily.fonts.firstOrNull { it.weight == style.fontWeight }
			val typeface = if (font != null) fontLoader.load(font) as? Typeface else null

			TextPaint().run {
				this.typeface = typeface
				textSize = density.run { style.fontSize.toPx() }
				Pair(fontMetrics.ascent - fontMetrics.top, fontMetrics.bottom - fontMetrics.descent)
			}
		}
	}

	Modifier
		.clip(RectangleShape)
		.layout { measurable, constraints ->
			val maxHeight =
				if (constraints.maxHeight == Constraints.Infinity) constraints.maxHeight
				else constraints.maxHeight + topPadding.toInt() + if(!isOverflow) bottomPadding.toInt() else 0

			val placeable =
				measurable.measure(constraints = constraints.copy(maxHeight = maxHeight))

			layout(
				placeable.width,
				placeable.height - topPadding.toInt() - if(!isOverflow) bottomPadding.toInt() else 0
			) {
				placeable.place(0, -topPadding.toInt())
			}
		}
}
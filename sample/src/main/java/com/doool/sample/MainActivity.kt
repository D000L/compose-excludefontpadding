package com.doool.sample

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.doool.exclude_font_padding.Text
import com.doool.sample.ui.theme.ExampleTheme
import com.doool.sample.ui.theme.Noto

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExampleTheme {
                Surface(color = MaterialTheme.colors.background) {
                    TextList()
                }
            }
        }
    }
}

@Composable
fun TextList(){
    val testMessage = "Test Message"
    Row() {
        Column(Modifier.weight(1f)) {
            Box(Modifier.height(80.dp), contentAlignment = Alignment.Center) {
                Text("Default Compose Text", fontSize = 12.sp)
            }

            DefaultText(testMessage, 30.sp)
            DefaultText(testMessage, 20.sp)
            DefaultText(testMessage, 16.sp)
            DefaultText(testMessage, 14.sp)
            DefaultText(testMessage, 12.sp)
            DefaultText(testMessage, 30.sp)
            DefaultText(testMessage, 20.sp)
            DefaultText(testMessage, 16.sp)
            DefaultText(testMessage, 14.sp)
            DefaultText(testMessage, 12.sp)
        }
        Column(Modifier.weight(1f)) {
            Box(Modifier.height(80.dp), contentAlignment = Alignment.Center) {
                Text("Exclude Font Padding Text", fontSize = 12.sp)
            }

            ExcludeFontPaddingText(testMessage, 30.sp)
            ExcludeFontPaddingText(testMessage, 20.sp)
            ExcludeFontPaddingText(testMessage, 16.sp)
            ExcludeFontPaddingText(testMessage, 14.sp)
            ExcludeFontPaddingText(testMessage, 12.sp)
            ExcludeFontPaddingText(testMessage, 30.sp)
            ExcludeFontPaddingText(testMessage, 20.sp)
            ExcludeFontPaddingText(testMessage, 16.sp)
            ExcludeFontPaddingText(testMessage, 14.sp)
            ExcludeFontPaddingText(testMessage, 12.sp)
            ExcludeFontPaddingText(testMessage, 30.sp)
            ExcludeFontPaddingText(testMessage, 20.sp)
            ExcludeFontPaddingText(testMessage, 16.sp)
            ExcludeFontPaddingText(testMessage, 14.sp)
            ExcludeFontPaddingText(testMessage, 12.sp)
        }
        Column(Modifier.weight(1f)) {
            Box(Modifier.height(80.dp), contentAlignment = Alignment.Center) {
                Text(
                    "Android TextView\nwith includeFontPadding = \"false\"",
                    fontSize = 12.sp
                )
            }

            ExcludeFontPaddingAndroidText(testMessage, 30.sp)
            ExcludeFontPaddingAndroidText(testMessage, 20.sp)
            ExcludeFontPaddingAndroidText(testMessage, 16.sp)
            ExcludeFontPaddingAndroidText(testMessage, 14.sp)
            ExcludeFontPaddingAndroidText(testMessage, 12.sp)
        }
    }
}


@Preview
@Composable
fun Test(){
    ExampleTheme {
        Surface(color = MaterialTheme.colors.background) {
            TextList()
        }
    }
}



@Composable
fun DefaultText(text: String, fontSize: TextUnit) {
    Text(
        modifier = Modifier.background(Color.Yellow),
        text = text,
        fontSize = fontSize,
    )
}

@Composable
fun ExcludeFontPaddingText(text: String, fontSize: TextUnit) {
    Text(
        modifier = Modifier.background(Color.Yellow),
        text = text,
        includeFontPadding = false,
        fontSize = fontSize
    )
}

@Composable
fun ExcludeFontPaddingAndroidText(text: String, fontSize: TextUnit) {
    AndroidView( factory = {
        TextView(it).apply {
            includeFontPadding = true
            typeface =
                ResourcesCompat.getFont(context, R.font.noto_sans_cjk_kr_regular)
            this.text = text
            textSize = fontSize.value
        }
    })
}

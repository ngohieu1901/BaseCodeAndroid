package com.metaldetector.detectorapp.detectorapp.ui.feature.splash

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.metaldetector.detectorapp.detectorapp.R
import com.metaldetector.detectorapp.detectorapp.theme.BaseProjectOriginalTheme

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(true) {
        Handler(Looper.getMainLooper()).postDelayed({
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        }, 3000)
    }

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize()
            .background(Color.White),
    ) {
        ImageNormal(ContentScale.Fit)
        Spacer(modifier = Modifier.height(12.dp))
        TextNormal()
        Spacer(modifier = Modifier.height(12.dp))
        TextMultipleStyle()
        Spacer(modifier = Modifier.height(12.dp))
        ImageCircle()
        Spacer(modifier = Modifier.height(12.dp))
        ButtonNormal()
    }
}

@Composable
fun TextNormal() {
    Text(
        "Ngo Trung Hieu 19012004, Ha Nam, Viet Nam",
        color = Color.Magenta,
        fontSize = 24.sp,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.ExtraBold,
        textAlign = TextAlign.Start,
        textDecoration = TextDecoration.Underline,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = {},
                onDoubleTap = {},
                onPress = {},
                onLongPress = {}
            )
        }
    )
}

@Composable
fun TextMultipleStyle() {
    Text(text = buildAnnotatedString {
        withStyle(style = ParagraphStyle(textAlign = TextAlign.End)) { //ParagraphStyle ap dung cho toan bo text con ben trong
            withStyle(style = SpanStyle(color = Color.Red)) {
                append("H")
            }
            withStyle(
                style = SpanStyle(
                    color = Color.Green,
                    textDecoration = TextDecoration.LineThrough
                )
            ) {
                append("ieu")
            }
        }

    })
}

@Composable
fun ImageNormal(contentScale: ContentScale) {
    Surface(
        modifier = Modifier
            .padding(4.dp)
            .height(150.dp)
            .shadow(elevation = 3.dp, shape = RoundedCornerShape(size = 16.dp))
    ) {
        Image(
            painterResource(R.drawable.img_intro_1),
            contentDescription = "imageResource",
            contentScale = contentScale,
        )
    }

    Image(imageVector = Icons.Filled.Person, contentDescription = "imageVector")
}

@Composable
fun ImageCircle() {
    Surface(
        modifier = Modifier
            .border(2.dp, color = Color.DarkGray, shape = CircleShape)
            .clip(shape = CircleShape)
    ) {
        Image(
            painterResource(R.drawable.ic_star_0),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun ButtonNormal() {
    Button(
        onClick = {},
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Red,
            containerColor = Color.Green,
            disabledContentColor = Color.LightGray,
            disabledContainerColor = Color.Gray
        ),
        enabled = true,
        shape = RoundedCornerShape(
            topEnd = 24.dp,
            bottomStart = 24.dp
        ),
        border = BorderStroke(2.dp, color = Color.Red)
    ) {
        Image(painterResource(R.drawable.flag_en), contentDescription = null, modifier = Modifier.padding(end = 8.dp).size(24.dp))
        Text("Click me")
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    BaseProjectOriginalTheme {
        SplashScreen(navController = rememberNavController())
    }
}
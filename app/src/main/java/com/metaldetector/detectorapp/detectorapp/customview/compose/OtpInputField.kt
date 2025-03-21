package com.metaldetector.detectorapp.detectorapp.customview.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.metaldetector.detectorapp.detectorapp.R
import com.metaldetector.detectorapp.detectorapp.theme.BaseProjectOriginalTheme
import com.metaldetector.detectorapp.detectorapp.ui.feature.splash.SplashScreen
import com.metaldetector.detectorapp.detectorapp.utils.SystemUtils.isOnlyNumbers

@Preview
@Composable
fun OtpInputFieldPreview() {
    BaseProjectOriginalTheme {
        OtpInputField(otpText = "", onOtpTextChange = { _, _ ->

        }, enabled = true)
    }
}

@Composable
fun OtpInputField(
    modifier: Modifier = Modifier,
    otpText: String = "",
    otpCount: Int = 4,
    isTextVisible: Boolean = true,
    enabled: Boolean = true,
    onOtpTextChange: (String, Boolean) -> Unit,
) {
    LaunchedEffect(Unit) {
        if (otpText.length > otpCount) {
            throw IllegalArgumentException("Otp text value must not have more than otpCount: $otpCount characters")
        }
    }

    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(otpText, selection = TextRange(otpText.length))
        )
    }

    val focusManager = LocalFocusManager.current

    val focusRequester: FocusRequester = remember {
        FocusRequester()
    }

    BasicTextField(
        modifier = modifier,
        value = textFieldValue,
        enabled = enabled,
        onValueChange = {
            if (!enabled) {
                return@BasicTextField
            }
            if (it.text.length <= otpCount && isOnlyNumbers(it.text)) {
                textFieldValue = it
                onOtpTextChange.invoke(it.text, it.text.length == otpCount)

                if (it.text.length == otpCount) {
                    focusManager.clearFocus()
                }
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = {
            Row(
                modifier = Modifier.focusRequester(focusRequester),
                horizontalArrangement = Arrangement.spacedBy(13.dp, Alignment.CenterHorizontally)
            ) {
                repeat(otpCount) { index ->
                    CharView(
                        index = index,
                        text = textFieldValue.text,
                        isTextVisible = isTextVisible,
                    )
                }
            }
        },
        )
}

@Composable
private fun RowScope.CharView(
    index: Int,
    text: String,
    isTextVisible: Boolean
) {
    val isFocused = text.length == index
    val char = when {
        index == text.length -> ""
        index > text.length -> ""
        else -> text[index].toString()
    }

    val backgroundColor = if (isFocused) {
        colorResource(id = R.color.color_E06038).copy(0.2f)
    } else {
        colorResource(id = R.color.color_F3F4F6)
    }

    Box(
        modifier = Modifier
            .weight(1f)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12))
            .background(backgroundColor)
    ) {
        if (char.isNotEmpty() && !isTextVisible) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(100))
                    .background(Color.Black)
            )
        } else {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = char,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )
        }
    }
}
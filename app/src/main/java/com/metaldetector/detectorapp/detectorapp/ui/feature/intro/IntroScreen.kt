package com.metaldetector.detectorapp.detectorapp.ui.feature.intro

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.metaldetector.detectorapp.detectorapp.customview.compose.OtpInputField

@Preview
@Composable
fun IntroScreen() {
    OtpInputField(otpText = "", onOtpTextChange = { _, _ ->

    }, enabled = true)
}
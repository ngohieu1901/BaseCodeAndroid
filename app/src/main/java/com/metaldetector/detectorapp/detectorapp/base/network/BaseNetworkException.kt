package com.metaldetector.detectorapp.detectorapp.base.network

class BaseNetworkException (
    val responseMessage: String? = null,
    val responseCode: Int = -1
) : Exception() {

    var mainMessage = ""

    fun parseFromString(errorBody: String) {

    }
}
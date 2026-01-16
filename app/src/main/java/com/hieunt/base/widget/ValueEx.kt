package com.hieunt.base.widget

import android.annotation.SuppressLint
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.time.times

@SuppressLint("SimpleDateFormat")
fun Long.toDate(): String {
    val date = Date(this)
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    return sdf.format(date)
}

@SuppressLint("SimpleDateFormat")
fun Long.toDateTime(): String {
    val date = Date(this)
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return sdf.format(date)
}

fun Float.dpToPx(context: Context): Float {
    return this * context.resources.displayMetrics.density
}

fun Float.toRadian(): Float {
    return this * (PI / 180).toFloat()
}

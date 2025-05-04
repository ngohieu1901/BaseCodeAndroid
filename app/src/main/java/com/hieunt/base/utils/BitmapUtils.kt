package com.hieunt.base.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore

object BitmapUtils {
    fun getRotatedBitmap(context: Context, uri: Uri): Bitmap {
        val rotationDegrees = getRotationFromUri(context, uri)

        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        if (rotationDegrees == 0) return bitmap

        return rotateBitmap(bitmap, rotationDegrees.toFloat())
    }

    fun getRotationFromUri(context: Context, uri: Uri): Int {
        val inputStream = context.contentResolver.openInputStream(uri)
        val exif = inputStream?.use { ExifInterface(it) }
        val orientation = exif?.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        ) ?: ExifInterface.ORIENTATION_NORMAL

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}
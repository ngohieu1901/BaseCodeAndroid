package com.hieunt.base.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.hieunt.base.R
import com.hieunt.base.data.result.AppResult
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FilesUtils {
    val filesExtensions = setOf(
        "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx"
    )

    val imageExtensions = setOf("jpg", "png")

    fun convertArabicNumbers(input: String): String {
        val arabicDigits = "٠١٢٣٤٥٦٧٨٩"
        val arabicToWestern = "0123456789"

        val map = arabicDigits.zip(arabicToWestern).toMap()
        return input.map { char ->
            if (char in arabicDigits) {
                map[char] ?: char
            } else {
                char
            }
        }.joinToString("")
    }

    fun getMimeType(extension: String): String {
        return when (extension) {
            "pdf" -> "application/pdf"
            "doc", "docx" -> "application/msword"
            "xls", "xlsx" -> "application/vnd.ms-excel"
            "ppt", "pptx" -> "application/vnd.ms-powerpoint"
            else -> "*/*"
        }
    }

    fun parseFileSize(size: String): Long {
        val sizeRegex = """(\d+)\s*(Bytes|KB|MB)""".toRegex()
        val matchResult = sizeRegex.find(size) ?: return 0

        val value = matchResult.groupValues[1].toLongOrNull() ?: return 0
        return when (matchResult.groupValues[2]) {
            "Bytes" -> value
            "KB" -> value * 1024
            "MB" -> value * 1048576
            else -> value
        }
    }


    fun getFileSizeFormat(file: File): String {
        val fileSize = file.length()
        if (fileSize < 1024) {
            return "$fileSize Bytes"
        } else if (fileSize < 1048576) {
            val kilobytes = fileSize / 1024
            return "$kilobytes KB"
        } else {
            val kilobytes = fileSize / 1048576
            return "$kilobytes MB"
        }
    }

    fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun shareFileWithPath(context: Context, path: String): AppResult<Boolean> {
        try {
            val file = File(path)
            if (!file.exists()) {
                return AppResult.Error(Exception("File not found"))
            }

            val extension = file.extension.lowercase()
            val mimeType = when {
                filesExtensions.contains(extension) -> getMimeType(extension)
                else -> "*/*"
            }

            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

            val intentShare = Intent(Intent.ACTION_SEND).apply {
                type = mimeType
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(Intent.createChooser(intentShare, context.getString(R.string.share_file)))
            return AppResult.Success(true)
        } catch (e: Exception) {
            return AppResult.Error(Exception(e.message))
        }
    }

    fun createImageCrop(context: Context): File {
        val picturesDirectory = File(context.getExternalFilesDir(null), "ImageCrop")
        if (!picturesDirectory.exists()) {
            picturesDirectory.mkdirs()
        }

        val fileName = "image_crop_${System.currentTimeMillis()}_${(1000..9999).random()}.png"
        val file = File(picturesDirectory, fileName)

        return file
    }

    fun getUriFromFilePath(context: Context, filePath: String): Uri {
        val file = File(filePath)
        return FileProvider.getUriForFile(context, context.packageName + ".provider", file)
    }
}
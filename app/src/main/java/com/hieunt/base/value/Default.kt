package com.hieunt.base.value

import android.Manifest
import android.content.res.Resources
import android.os.Environment

object Default {
    const val PRIVACY_POLICY =
        "https://amazic.net/Privacy-Policy-270pdf.html"

    const val BASE_URL = ""

    object IntentKeys {
        const val SCREEN = "SCREEN"
        const val SPLASH_ACTIVITY = "SplashActivity"
        const val FILES_MODEL = "FilesModel"
    }

    object Screen {
        val width: Int
            get() = Resources.getSystem().displayMetrics.widthPixels

        val height: Int
            get() = Resources.getSystem().displayMetrics.heightPixels
    }

    val STORAGE_PERMISSION_API_SMALLER_30 = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    const val NEW_TO_OLD = "newToOld"
    const val OLD_TO_NEW = "oldToNew"
    const val A_TO_Z = "aToZ"
    const val Z_TO_A = "zToA"

    const val SELECT_MULTIPLE_ITEM = "select_multiple_item"
    const val SELECT_ONE_ITEM = "select_one_item"
    const val NORMAL_ITEM = "normal_item"

    const val MERGE_PDF = "merge_pdf"
    const val CONVERT_WORD_TO_PDF = "convert_word_to_pdf"
    const val CONVERT_EXCEL_TO_PDF = "convert_excel_to_pdf"
    const val CONVERT_IMAGE_TO_PDF = "convert_image_to_pdf"

    const val FILES_FRAGMENT = "files_fragment"
    const val FAVORITE_FRAGMENT = "favorite_fragment"
    const val TOOLS_FRAGMENT = "tools_fragment"
    const val SETTING_FRAGMENT = "settings_fragment"

    const val NEW_PATH = "newPath"
    const val ID_IMAGE = "idImage"
    const val KEY_REFRESH = "keyRefresh"
    const val REQUEST_KEY_CROP_RESULT = "requestKeyCropResult"
    const val REQUEST_KEY_REFRESH_IMAGE = "requestKeyRefreshImage"
}
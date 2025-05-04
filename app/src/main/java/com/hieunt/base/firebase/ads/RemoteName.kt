package com.hieunt.base.firebase.ads

object RemoteName {
    const val BANNER_SPLASH = "banner_splash"
    const val OPEN_SPLASH = "open_splash"
    const val INTER_SPLASH = "inter_splash"
    const val NATIVE_LANG = "native_language"
    const val NATIVE_LANG_2 = "native_language_2"
    const val NATIVE_INTRO = "native_intro"
    const val NATIVE_INTRO_2 = "native_intro_2"
    const val INTER_INTRO = "inter_intro"
    const val NATIVE_INTRO_FULL = "native_intro_full"
    const val NATIVE_PERMISSION = "native_permission"
    const val NATIVE_WB = "native_wb"
    const val NATIVE_ALL = "native_all"
    const val BANNER_ALL = "banner_all"
    const val INTER_FILE_BACK = "inter_file_back"
    const val RESUME_WB = "resume_wb"
    const val CONVERT_FILE = "convert_file"
    const val BANNER_SETTING = "banner_setting"
    const val TEST_FLOW = "test_flow"

    const val INTERVAL_RELOAD_NATIVE  = "interval_reload_native"

    val LIST_DOUBLE_NATIVE = mutableListOf(
        NATIVE_INTRO_FULL,
        NATIVE_PERMISSION,
        NATIVE_WB,
        NATIVE_ALL
    )

    val TURN_OFF_CONFIGS = mutableListOf(
        BANNER_SPLASH,
        NATIVE_INTRO,
        NATIVE_INTRO_2,
        NATIVE_INTRO_FULL,
        NATIVE_PERMISSION,
        NATIVE_LANG,
        NATIVE_LANG_2,
        NATIVE_ALL,
        INTER_FILE_BACK,
        RESUME_WB,
        CONVERT_FILE,
        NATIVE_WB
    )
}
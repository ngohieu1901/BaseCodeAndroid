package com.metaldetector.detectorapp.detectorapp.firebase.ads

object RemoteName {
    const val BANNER_SPLASH = "banner_splash"
    const val OPEN_SPLASH = "open_splash"
    const val INTER_SPLASH = "inter_splash"
    const val NATIVE_LANG = "native_language"
    const val NATIVE_INTRO = "native_intro"
    const val INTER_INTRO = "inter_intro"
    const val NATIVE_INTRO_FULL = "native_intro_fullscreen"
    const val NATIVE_PERMISSION = "native_per"
    const val NATIVE_HOME_PERMISSION = "native_home_permission"
    const val NATIVE_HOME = "native_home"
    const val NATIVE_WB = "native_wb"
    const val COLLAPSE_BANNER_HOME = "collapse_banner_home"
    const val BANNER_ALL = "banner_all"
    const val APP_OPEN_RESUME = "open_resume"
    const val COLLAPSE_BANNER = "collapse_banner"
    const val INTER_MORE = "inter_more"
    const val INTER_DETAIL = "inter_detail"
    const val NATIVE_DETAIL = "native_detail"
    const val NATIVE_SEARCH = "native_search"
    const val INTER_DETAIL_LIVE = "inter_detail_live"
    const val NATIVE_MATCHES = "native_matches"
    const val NATIVE_FAVORITE = "native_favorite"
    const val INTER_BACK = "inter_back"

    const val SHOW_ALL_ADS  = "show_all_ads"
    const val INTERVAL_RELOAD_NATIVE  = "interval_reload_native"

    val TURN_OFF_CONFIGS = mutableListOf(
        BANNER_SPLASH,
        NATIVE_INTRO,
        NATIVE_INTRO_FULL,
        NATIVE_PERMISSION,
        NATIVE_HOME_PERMISSION,
        NATIVE_HOME,
        APP_OPEN_RESUME,
        INTER_MORE,
        INTER_DETAIL,
        NATIVE_DETAIL,
        NATIVE_SEARCH,
        INTER_DETAIL_LIVE,
        NATIVE_MATCHES,
        NATIVE_FAVORITE,
        INTER_BACK
    )
}
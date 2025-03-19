package com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.welcome_back.app_open;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.LoadAdError;

public class Constant {
    public static final LoadAdError NO_INTERNET_ERROR = new LoadAdError(-100, "No Internet", "local", null, null);
    public static final AdError AD_NOT_AVAILABLE_ERROR = new AdError(-200, "Ad Not Available", "local");
}

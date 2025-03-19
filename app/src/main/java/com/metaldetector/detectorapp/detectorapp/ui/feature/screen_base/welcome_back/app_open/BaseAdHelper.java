package com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.welcome_back.app_open;

import androidx.annotation.IntDef;

import com.google.android.gms.ads.admanager.AdManagerAdRequest;

public class BaseAdHelper {
    @Status
    protected int status = Status.IDLE;

    protected AdManagerAdRequest getNewRequest() {
        return new AdManagerAdRequest.Builder().build();
    }

    @IntDef({Status.IDLE, Status.ON_LOADING, Status.ON_LOADED, Status.ON_SHOWING, Status.ON_DISMISS})
    public @interface Status {
        int IDLE = 0;
        int ON_LOADING = 1;
        int ON_LOADED = 2;
        int ON_DISMISS = 3;
        int ON_SHOWING = 4;
    }
}

package com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.welcome_back.app_open;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.amazic.library.Utils.NetworkUtil;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

import java.util.Date;

public class AppOpenHelper extends BaseAdHelper {

    private long loadTime = 0;
    private AppOpenAd appOpenAd = null;

    public void loadAd(Context context, @NonNull String adUnitId, LoadAppOpenCallback callback) {
        if (NetworkUtil.isNetworkActive(context) && callback != null) {
            if (status == Status.ON_LOADING)
                return;
            status = Status.ON_LOADING;
            appOpenAd = null;
            AppOpenAd.load(context, adUnitId, getNewRequest(), new AppOpenAd.AppOpenAdLoadCallback() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    status = Status.ON_LOADED;
                    callback.onAdFailedToLoad(loadAdError);
                    callback.onAdLoaded();
                }

                @Override
                public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                    super.onAdLoaded(appOpenAd);
                    status = Status.ON_LOADED;
                    callback.onAdLoadedSuccessfully();
                    callback.onAdLoaded();
                    setAppOpenAd(appOpenAd);
                    loadTime = System.currentTimeMillis();
                }
            });
        } else if (callback == null) {
            Toast.makeText(context, "Callback cannot null!", Toast.LENGTH_SHORT).show();
        } else {
            callback.onAdFailedToLoad(Constant.NO_INTERNET_ERROR);
        }
    }

    public void showAd(Activity activity, ShowAppOpenCallback callback) {
        if (status != Status.ON_SHOWING && isAdAvailable() && callback != null) {
            FullScreenContentCallback fullScreenContentCallback = setShowCallback(callback);
            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            appOpenAd.show(activity);
        } else if (callback == null) {
            Toast.makeText(activity, "Callback cannot null!", Toast.LENGTH_SHORT).show();
        } else {
            if (isAdAvailable())
                callback.onAdFailedToShow(Constant.AD_NOT_AVAILABLE_ERROR);
            callback.onNextAction();
        }
    }

    @NonNull
    private FullScreenContentCallback setShowCallback(ShowAppOpenCallback callback) {
        return new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
                callback.onAdClicked();
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                status = Status.ON_DISMISS;
                callback.onAdDismissed();
                callback.onNextAction();
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                status = Status.ON_DISMISS;
                callback.onAdFailedToShow(adError);
                callback.onNextAction();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                callback.onAdImpression();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
                status = Status.ON_SHOWING;
                callback.onAdShowed();
            }
        };
    }

    private boolean wasLoadTimeLessThanNHoursAgo() {
        long dateDifference = (new Date()).getTime() - this.loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * (long) 4));
    }

    public boolean isAdAvailable() {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo();
    }

    public void setAppOpenAd(AppOpenAd appOpenAd) {
        this.appOpenAd = appOpenAd;
    }

}

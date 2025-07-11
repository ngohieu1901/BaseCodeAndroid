package com.hieunt.base.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.amazic.library.Utils.RemoteConfigHelper
import com.amazic.library.ads.admob.AdmobApi
import com.amazic.library.ads.banner_ads.BannerBuilder
import com.amazic.library.ads.banner_ads.BannerManager
import com.amazic.library.ads.callback.InterCallback
import com.amazic.library.ads.callback.RewardedCallback
import com.amazic.library.ads.inter_ads.InterManager
import com.amazic.library.ads.native_ads.NativeBuilder
import com.amazic.library.ads.native_ads.NativeManager
import com.amazic.library.ads.reward_ads.RewardManager
import com.hieunt.base.R
import com.hieunt.base.base.network.NetworkCallbackHandler
import com.hieunt.base.firebase.ads.AdsHelper
import com.hieunt.base.firebase.ads.RemoteName
import com.hieunt.base.presentations.components.dialogs.LoadingDialog
import com.hieunt.base.utils.PermissionUtils
import com.hieunt.base.utils.SystemUtils.setLocale
import com.hieunt.base.widget.hideNavigation
import com.hieunt.base.widget.hideStatusBar
import com.hieunt.base.widget.toast
import kotlinx.coroutines.CoroutineExceptionHandler

abstract class BaseActivity<VB : ViewBinding>() : AppCompatActivity() {
    protected lateinit var binding: VB
    private var isRegistered = false
    private var networkCallback: NetworkCallbackHandler? = null

    protected val permissionUtils by lazy { PermissionUtils(this) }

    protected val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, exception ->
            Log.e("CoroutineExceptionHandler1901", "${this::class.java.name}: ${exception.message}")
        }
    }

    protected abstract fun setViewBinding(): VB
    private val loadingDialog by lazy { LoadingDialog(this) }
    protected abstract fun initView()
    protected abstract fun dataCollect()

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        window.hideNavigation()
        window.hideStatusBar()
        super.onCreate(savedInstanceState)
        binding = setViewBinding()
        //onBackPress
        onBackPressedDispatcher.addCallback(this) {
            isEnabled = false
            onBackPressedSystem()
            isEnabled = true
        }
        setContentView(binding.root)
        //view_model
        //internet
//        networkCallback = NetworkCallbackHandler {
//            if (!it) {
//                if (this !is NoInternetActivity) {
//                    launchActivity(NoInternetActivity::class.java)
//                }
//            } else {
//                if (this is NoInternetActivity && this.currentBundle()
//                        ?.getString(SCREEN) != SPLASH_ACTIVITY
//                ) {
//                    finish()
//                } else if (this is NoInternetActivity && this.currentBundle()
//                        ?.getString(SCREEN) == SPLASH_ACTIVITY
//                ) {
//                    val myIntent = Intent(this, SplashActivity::class.java)
//                    myIntent.flags =
//                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                    startActivity(myIntent)
//                    finishAffinity()
//                }
//            }
//        }
//        networkCallback?.register(this)
        initView()
        dataCollect()
    }

    override fun onResume() {
        super.onResume()
        window.hideStatusBar()
        window.hideNavigation()
        AdsHelper.enableResume(this)
    }

    override fun onDestroy() {
        super.onDestroy()
//        networkCallback?.unregister()
    }

    open fun onBackPressedSystem() {
        onBackPressedDispatcher.onBackPressed()
    }

    protected fun showPopupWindow(view: View, popupWindow: PopupWindow) {
        val location = IntArray(2)
        view.getLocationInWindow(location)
        val positionOfIcon = location[1]

        val displayMetrics = resources.displayMetrics
        val height = displayMetrics.heightPixels * 2 / 3

        if (positionOfIcon > height) {
            popupWindow.showAsDropDown(view, -22, -(view.height * 7), Gravity.BOTTOM or Gravity.END)
        } else {
            popupWindow.showAsDropDown(view, -22, 0, Gravity.TOP or Gravity.END)
        }
    }

    fun showLoading() {
        if (loadingDialog.isShowing.not())
            loadingDialog.show()
    }

    fun dismissLoading() {
        if (loadingDialog.isShowing) loadingDialog.dismiss()
    }

    fun renderStateLoading(isShowLoading: Boolean) {
        if (isShowLoading) showLoading() else dismissLoading()
    }

    fun renderStateError(error: Throwable) {
        toast(error.message.toString())
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { setLocale(it) })
    }

    protected fun loadBanner() {
        val frBanner = findViewById<FrameLayout>(R.id.fr_banner)
        if (frBanner != null) {
            val bannerManager = BannerManager(
                this,
                frBanner,
                this,
                BannerBuilder().isIdApi,
                RemoteName.BANNER_ALL
            )
            bannerManager.setAlwaysReloadOnResume(true)
        }
    }

    protected fun loadNative(
        adsKey: String,
        idLayoutShimmer: Int,
        idLayoutNative: Int,
    ) {
        val frAds = findViewById<FrameLayout>(R.id.fr_ads)
        if (frAds != null) {
            val nativeBuilder =
                NativeBuilder(this, frAds, idLayoutShimmer, idLayoutNative, idLayoutNative, true)
            nativeBuilder.listIdAdMain = AdmobApi.getInstance().getListIDByName(adsKey)
            if (adsKey in RemoteName.LIST_DOUBLE_NATIVE) {
                nativeBuilder.listIdAdSecondary = AdmobApi.getInstance().getListIDByName(adsKey)
            }
            val nativeManager = NativeManager(this, this, nativeBuilder, adsKey)
            nativeManager.setIntervalReloadNative(
                RemoteConfigHelper.getInstance()
                    .get_config_long(this, RemoteName.INTERVAL_RELOAD_NATIVE) * 1000
            )
        }
    }

    fun loadDoubleNative(
        remoteKey: String,
        adsKey1: String,
        adsKey2: String,
        idLayoutNative: Int,
        idLayoutShimmer: Int,
    ) {
        val frAds = findViewById<FrameLayout>(R.id.fr_ads)
        if (frAds != null) {
            val nativeBuilder =
                NativeBuilder(this, frAds, idLayoutShimmer, idLayoutNative, idLayoutNative, true)
            nativeBuilder.listIdAdMain = AdmobApi.getInstance().getListIDByName(adsKey1)
            nativeBuilder.listIdAdSecondary = AdmobApi.getInstance().getListIDByName(adsKey2)
            val nativeManager = NativeManager(this, this, nativeBuilder, remoteKey)
            nativeManager.setIntervalReloadNative(
                RemoteConfigHelper.getInstance()
                    .get_config_long(this@BaseActivity, RemoteName.INTERVAL_RELOAD_NATIVE) * 1000
            )
            nativeManager.setAlwaysReloadOnResume(true)
        }
    }

    protected fun loadInter(adsKey: String?, remoteKey: String?) {
        InterManager.loadInterAds(this, adsKey, remoteKey)
    }

    protected fun loadInter(adsKey: String?) {
        InterManager.loadInterAds(this, adsKey, adsKey)
    }

    protected fun showInter(
        adsKey: String?,
        remoteKey: String?,
        isReloadAds: Boolean,
        onNextAction: () -> Unit,
    ) {
        InterManager.showInterAds(this, adsKey, remoteKey, object : InterCallback() {
            override fun onNextAction() {
                super.onNextAction()
                onNextAction()
            }
        }, isReloadAds)
    }

    protected fun showInter(adsKey: String?, isReloadAds: Boolean, onNextAction: () -> Unit) {
        InterManager.showInterAds(this, adsKey, adsKey, object : InterCallback() {
            override fun onNextAction() {
                super.onNextAction()
                onNextAction()
            }
        }, isReloadAds)
    }

    protected fun loadReward(adsKey: String?) {
        RewardManager.loadRewardAds(this, adsKey, adsKey)
    }

    protected fun loadReward(adsKey: String?, remoteKey: String?) {
        RewardManager.loadRewardAds(this, adsKey, remoteKey)
    }

    protected fun showReward(adsKey: String?, isReloadAds: Boolean, onNextAction: () -> Unit) {
        RewardManager.showRewardAds(this, adsKey, adsKey, object : RewardedCallback() {
            override fun onNextAction() {
                super.onNextAction()
                onNextAction()
            }
        }, isReloadAds)
    }

    protected fun showReward(
        adsKey: String?,
        remoteKey: String?,
        isReloadAds: Boolean,
        onNextAction: () -> Unit,
    ) {
        RewardManager.showRewardAds(this, adsKey, remoteKey, object : RewardedCallback() {
            override fun onNextAction() {
                super.onNextAction()
                onNextAction()
            }
        }, isReloadAds)
    }
}
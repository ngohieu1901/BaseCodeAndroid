package com.metaldetector.detectorapp.detectorapp.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
import com.metaldetector.detectorapp.detectorapp.R
import com.metaldetector.detectorapp.detectorapp.base.network.NetworkCallbackHandler
import com.metaldetector.detectorapp.detectorapp.firebase.ads.AdsHelper
import com.metaldetector.detectorapp.detectorapp.firebase.ads.RemoteName
import com.metaldetector.detectorapp.detectorapp.ui.dialog.LoadingDialog
import com.metaldetector.detectorapp.detectorapp.utils.PermissionUtils
import com.metaldetector.detectorapp.detectorapp.utils.SharePrefUtils
import com.metaldetector.detectorapp.detectorapp.utils.SystemUtils
import com.metaldetector.detectorapp.detectorapp.widget.hideNavigation
import com.metaldetector.detectorapp.detectorapp.widget.hideStatusBar
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

abstract class BaseActivity<VB : ViewBinding, VM : ViewModel> : AppCompatActivity() {
    lateinit var binding: VB
    lateinit var viewModel: VM
    private var isRegistered = false
    private var networkCallback: NetworkCallbackHandler? = null

    @Inject
    lateinit var permissionUtils: PermissionUtils

    @Inject
    lateinit var sharePref: SharePrefUtils

    val exceptionHandler: CoroutineExceptionHandler by lazy { CoroutineExceptionHandler { _, exception ->
        Log.e("coroutineException1901", "${exception.message}")
    } }

    protected abstract fun setViewBinding(): VB
    private val loadingDialog by lazy { LoadingDialog(this) }
    protected abstract fun initViewModel(): Class<VM>
    protected abstract fun initView()
    protected abstract fun dataCollect()

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        SystemUtils.setLocale(this)
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
        viewModel = ViewModelProvider(this)[initViewModel()]
        //internet
        networkCallback = NetworkCallbackHandler {

        }
        networkCallback?.register(this)
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
        networkCallback?.unregister()
    }

    open fun onBackPressedSystem() {
        onBackPressedDispatcher.onBackPressed()
    }

    fun showPopupWindow(view: View, popupWindow: PopupWindow) {
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


    suspend fun showLoading() {
        withContext(Dispatchers.Main) {
            if (loadingDialog.isShowing.not())
                loadingDialog.show()
        }
    }

    suspend fun dismissLoading() {
        withContext(Dispatchers.Main) {
            if (loadingDialog.isShowing) loadingDialog.dismiss()
        }
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
        adsKey: String?,
        idLayoutShimmer: Int,
        idLayoutNative: Int
    ) {
        val frAds = findViewById<FrameLayout>(R.id.fr_ads)
        if (frAds != null) {
            val nativeBuilder = NativeBuilder(this, frAds, idLayoutShimmer, idLayoutNative, idLayoutNative)
            nativeBuilder.listIdAd = AdmobApi.getInstance().getListIDByName(adsKey)
            val nativeManager = NativeManager(this, this, nativeBuilder, adsKey)
            nativeManager.setIntervalReloadNative(
                RemoteConfigHelper.getInstance().get_config_long(this, RemoteName.INTERVAL_RELOAD_NATIVE) * 1000
            )
        }
    }

    protected fun loadInter(adsKey: String?) {
        InterManager.loadInterAds(this, adsKey)
    }

    protected fun showInter(adsKey: String?, isReloadAds: Boolean, onNextAction: () -> Unit) {
        InterManager.showInterAds(this, adsKey, object : InterCallback() {
            override fun onNextAction() {
                super.onNextAction()
                onNextAction()
            }
        }, isReloadAds)
    }

    protected fun loadReward(adsKey: String?) {
        RewardManager.loadRewardAds(this, adsKey)
    }

    protected fun showReward(adsKey: String?, isReloadAds: Boolean, onNextAction: () -> Unit) {
        RewardManager.showRewardAds(this, adsKey, object : RewardedCallback(){
            override fun onNextAction() {
                super.onNextAction()
                onNextAction()
            }
        }, isReloadAds)
    }
}
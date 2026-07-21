package com.hieunt.base.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.google.android.play.core.install.model.InstallStatus
import com.hieunt.base.base.network.NetworkCallbackHandler
import com.hieunt.base.constants.Constants.IntentKeys.SCREEN
import com.hieunt.base.constants.Constants.IntentKeys.SPLASH_ACTIVITY
import com.hieunt.base.firebase.ads.activity.enableResume
import com.hieunt.base.presentations.components.dialogs.LoadingDialog
import com.hieunt.base.presentations.feature.screen_base.no_internet.NoInternetActivity
import com.hieunt.base.presentations.feature.screen_base.splash.SplashActivity
import com.hieunt.base.presentations.feature.screen_base.splash.SplashActivity.Companion.appUpdateManager
import com.hieunt.base.presentations.feature.screen_base.splash.SplashActivity.Companion.installStateUpdatedListener
import com.hieunt.base.utils.LanguageUtils.setLocale
import com.hieunt.base.widget.currentBundle
import com.hieunt.base.widget.hideNavigation
import com.hieunt.base.widget.hideStatusBar
import com.hieunt.base.widget.launchActivity
import com.hieunt.base.widget.toast

abstract class BaseActivity<VB : ViewBinding>(
    private val bindingInflater: (LayoutInflater) -> VB,
) : AppCompatActivity() {
    protected lateinit var binding: VB
    private var networkCallback: NetworkCallbackHandler? = null
    private val loadingDialog by lazy { LoadingDialog(this) }
    protected open fun initView() {}
    protected open fun dataCollect() {}

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { setLocale(it) })
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        window.hideNavigation()
        window.hideStatusBar()
        super.onCreate(savedInstanceState)
        binding = bindingInflater.invoke(layoutInflater)
        setContentView(binding.root)
        //internet
        networkCallback = NetworkCallbackHandler {
            if (!it) {
                if (this !is NoInternetActivity) {
                    launchActivity(NoInternetActivity::class.java)
                }
            } else {
                if (this is NoInternetActivity && this.currentBundle()
                        ?.getString(SCREEN) != SPLASH_ACTIVITY
                ) {
                    finish()
                } else if (this is NoInternetActivity && this.currentBundle()
                        ?.getString(SCREEN) == SPLASH_ACTIVITY
                ) {
                    val myIntent = Intent(this, SplashActivity::class.java)
                    myIntent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(myIntent)
                    finishAffinity()
                }
            }
        }
        networkCallback?.register(this)
        initView()
        dataCollect()
    }

    override fun onResume() {
        super.onResume()
        window.hideStatusBar()
        window.hideNavigation()
        enableResume()
        installStateUpdatedListener?.let { appUpdateManager?.registerListener(it) }
        appUpdateManager?.appUpdateInfo?.addOnSuccessListener { appUpdateInfo ->
            // If the update is downloaded but not installed,
            // notify the user to complete the update.
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                appUpdateManager?.completeUpdate()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkCallback?.unregister()
        installStateUpdatedListener?.let { appUpdateManager?.unregisterListener(it) }
    }

    protected fun blockBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        })
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

    protected fun renderStateLoading(isShowLoading: Boolean) {
        if (isShowLoading) showLoading() else dismissLoading()
    }

    protected fun renderStateError(error: Throwable) {
        toast(error.message.toString())
    }
}
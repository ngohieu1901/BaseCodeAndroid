package com.metaldetector.detectorapp.detectorapp.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.metaldetector.detectorapp.detectorapp.base.network.NetworkCallbackHandler
import com.metaldetector.detectorapp.detectorapp.ui.dialog.LoadingDialog
import com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.no_internet.NoInternetActivity
import com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.splash.SplashActivity
import com.metaldetector.detectorapp.detectorapp.utils.PermissionUtils
import com.metaldetector.detectorapp.detectorapp.utils.SharePrefUtils
import com.metaldetector.detectorapp.detectorapp.utils.SystemUtils.setLocale
import com.metaldetector.detectorapp.detectorapp.value.Default.IntentKeys.SCREEN
import com.metaldetector.detectorapp.detectorapp.value.Default.IntentKeys.SPLASH_ACTIVITY
import com.metaldetector.detectorapp.detectorapp.widget.currentBundle
import com.metaldetector.detectorapp.detectorapp.widget.hideNavigation
import com.metaldetector.detectorapp.detectorapp.widget.hideStatusBar
import com.metaldetector.detectorapp.detectorapp.widget.launchActivity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

abstract class BaseActivity<VB : ViewBinding, VM : ViewModel> : AppCompatActivity() {
    lateinit var binding: VB
    lateinit var viewModel: VM
    private var isRegistered = false
    private var networkCallback: NetworkCallbackHandler? = null

    protected val permissionUtils by lazy { PermissionUtils(this) }

    protected val sharePref by lazy { SharePrefUtils(this) }

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
            if (!it) {
                if (this !is NoInternetActivity) {
                    launchActivity(NoInternetActivity::class.java)
                }
            } else {
                if (this is NoInternetActivity && this.currentBundle()?.getString(SCREEN) != SPLASH_ACTIVITY) {
                    finish()
                } else if (this is NoInternetActivity && this.currentBundle()?.getString(SCREEN) == SPLASH_ACTIVITY) {
                    val myIntent = Intent(this, SplashActivity::class.java)
                    myIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
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

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { setLocale(it) })
    }
}
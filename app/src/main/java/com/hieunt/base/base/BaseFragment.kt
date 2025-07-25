package com.hieunt.base.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
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
import com.hieunt.base.firebase.ads.RemoteName
import com.hieunt.base.utils.PermissionUtils
import com.hieunt.base.utils.SystemUtils.setLocale
import com.hieunt.base.widget.toast
import kotlinx.coroutines.CoroutineExceptionHandler

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected val permissionUtils by lazy { PermissionUtils(requireActivity())}

    val exceptionHandler: CoroutineExceptionHandler by lazy { CoroutineExceptionHandler { _, exception ->
        Log.e("CoroutineExceptionHandler1901", "${this::class.java.name}: ${exception.message}")
    } }

    protected abstract fun initData()
    protected abstract fun setupView()
    protected abstract fun dataCollect()

    open fun hideSoftKeyboard() {
        activity?.currentFocus?.let {
            val inputMethodManager =
                activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(setLocale(context))
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        dataCollect()
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflate(
        inflater,
        container,
        false,
    ).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    @CallSuper
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun safeNavigate(
        @IdRes resId: Int,
        args: Bundle? = null,
    ) {
        findNavControllerOrNull()?.navigate(resId, args)
    }

    fun safeNavigate(
        navDirections: NavDirections
    ) {
        findNavControllerOrNull()?.navigate(navDirections)
    }

    private fun findNavControllerOrNull(): NavController? {
        return try {
            findNavController()
        } catch (e: Exception) {
            null
        }
    }

    fun safeNavigateParentNav(
        navDirections: NavDirections
    ) {
        findParentNavController().navigate(navDirections)
    }

    private fun findParentNavController(): NavController {
        return requireActivity().findNavController(R.id.fcv_app)
    }

    fun popBackStack(
        destinationId: Int? = null,
        inclusive: Boolean = false
    ) {
        findNavControllerOrNull()?.let {
            if (destinationId != null) {
                it.popBackStack(destinationId, inclusive)
            } else {
                it.popBackStack()
            }
        }
    }

    fun replaceFragment(id: Int, fragment: Fragment) {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(id, fragment)
        ft.commit()
    }

    fun showPopupWindow(view: View, popupWindow: PopupWindow) {
        val location = IntArray(2)
        view.getLocationInWindow(location)
        val positionOfIcon = location[1]

        val displayMetrics = requireContext().resources.displayMetrics
        val height = displayMetrics.heightPixels * 2 / 3

        if (positionOfIcon > height) {
            popupWindow.showAsDropDown(view, -22, -(view.height * 7), Gravity.BOTTOM or Gravity.END)
        } else {
            popupWindow.showAsDropDown(view, -22, 0, Gravity.TOP or Gravity.END)
        }
    }

    private fun showLoading() {
        (activity as? BaseActivity<*>)?.showLoading()
    }

    private fun dismissLoading() {
        (activity as? BaseActivity<*>)?.dismissLoading()
    }

    fun renderStateLoading(isShowLoading: Boolean){
        if (isShowLoading) showLoading() else dismissLoading()
    }

    fun renderStateError(error: Throwable) {
        toast(error.message.toString())
    }

    protected fun loadBanner() {
        val frBanner = view?.findViewById<FrameLayout>(R.id.fr_banner)
        if (frBanner != null) {
            val bannerManager = BannerManager(
                requireContext(),
                frBanner.width,
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
        val frAds = binding.root.findViewById<FrameLayout>(R.id.fr_ads)
        if (frAds != null) {
            val nativeBuilder = NativeBuilder(requireContext(), frAds, idLayoutShimmer, idLayoutNative, idLayoutNative)
            nativeBuilder.listIdAdMain = AdmobApi.getInstance().getListIDByName(adsKey)
            nativeBuilder.listIdAdSecondary = AdmobApi.getInstance().getListIDByName(adsKey)
            val nativeManager = NativeManager(requireActivity(), this, nativeBuilder, adsKey)
            nativeManager.setIntervalReloadNative(RemoteConfigHelper.getInstance().get_config_long(requireContext(), RemoteName.INTERVAL_RELOAD_NATIVE) * 1000)
        }
    }

    protected fun loadNative(
        adsKey: String?,
        remoteKey: String?,
        idLayoutShimmer: Int,
        idLayoutNative: Int,
    ) {
        val frAds = binding.root.findViewById<FrameLayout>(R.id.fr_ads)
        if (frAds != null) {
            val nativeBuilder = NativeBuilder(requireContext(), frAds, idLayoutShimmer, idLayoutNative, idLayoutNative)
            nativeBuilder.listIdAdMain = AdmobApi.getInstance().getListIDByName(adsKey)
            val nativeManager = NativeManager(requireActivity(), this, nativeBuilder, remoteKey)
            nativeManager.setIntervalReloadNative(
                RemoteConfigHelper.getInstance()
                    .get_config_long(requireContext(), RemoteName.INTERVAL_RELOAD_NATIVE) * 1000
            )
        }
    }

    protected fun loadInter(adsKey: String?, remoteKey: String?) {
        InterManager.loadInterAds(requireContext(), adsKey, remoteKey)
    }

    protected fun loadInter(adsKey: String?) {
        InterManager.loadInterAds(requireContext(), adsKey, adsKey)
    }

    protected fun showInter(
        adsKey: String?,
        remoteKey: String?,
        isReloadAds: Boolean,
        onNextAction: () -> Unit,
    ) {
        InterManager.showInterAds(requireActivity(), adsKey, remoteKey, object : InterCallback() {
            override fun onNextAction() {
                super.onNextAction()
                onNextAction()
            }
        }, isReloadAds)
    }

    protected fun showInter(adsKey: String?, isReloadAds: Boolean, onNextAction: () -> Unit) {
        InterManager.showInterAds(requireActivity(), adsKey, adsKey, object : InterCallback() {
            override fun onNextAction() {
                super.onNextAction()
                onNextAction()
            }
        }, isReloadAds)
    }

    protected fun loadReward(adsKey: String?) {
        RewardManager.loadRewardAds(requireActivity(), adsKey, adsKey)
    }

    protected fun loadReward(adsKey: String?, remoteKey: String?) {
        RewardManager.loadRewardAds(requireActivity(), adsKey, remoteKey)
    }

    protected fun showReward(adsKey: String?, isReloadAds: Boolean, onNextAction: () -> Unit) {
        RewardManager.showRewardAds(requireActivity(), adsKey, adsKey, object : RewardedCallback() {
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
        RewardManager.showRewardAds(requireActivity(), adsKey, remoteKey, object : RewardedCallback() {
            override fun onNextAction() {
                super.onNextAction()
                onNextAction()
            }
        }, isReloadAds)
    }
}
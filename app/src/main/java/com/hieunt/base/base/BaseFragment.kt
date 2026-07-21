package com.hieunt.base.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.hieunt.base.R
import com.hieunt.base.utils.LanguageUtils.setLocale
import com.hieunt.base.widget.toast

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected open fun initData() {}
    protected abstract fun VB.setupView()
    protected open fun dataCollect() {}

    open fun handleOnBackPressed(): Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(setLocale(context))
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
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
        val callback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (this@BaseFragment.handleOnBackPressed()) return
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.setupView()
        dataCollect()
    }

    @CallSuper
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    protected fun safeNavigate(navDirections: NavDirections) {
        try {
            findNavControllerOrNull()?.navigate(navDirections)
        } catch (e: IllegalArgumentException) {
            Log.d("safeNavigateException", "safeNavigate: $e")
        }
    }

    private fun findNavControllerOrNull(): NavController? {
        return try {
            findNavController()
        } catch (_: Exception) {
            null
        }
    }

    protected fun safeNavigateParentNav(navDirections: NavDirections) {
        try {
            findParentNavController()?.navigate(navDirections)
        } catch (e: IllegalArgumentException) {
            Log.e("safeNavigateException", "safeNavigateParentNav: $e")
        }
    }

    private fun findParentNavController(): NavController? {
        return try {
            requireActivity().findNavController(R.id.fcv_app)
        } catch (e: IllegalStateException) {
            Log.e("findNavException", "safeNavigateParentNav: $e")
            null
        }
    }

    protected fun popBackStack(
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

    protected fun showPopupWindow(view: View, popupWindow: PopupWindow) {
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

    protected fun showLoading() {
        (activity as? BaseActivity<*>)?.showLoading()
    }

    protected fun dismissLoading() {
        (activity as? BaseActivity<*>)?.dismissLoading()
    }

    fun renderStateLoading(isShowLoading: Boolean) {
        if (isShowLoading) showLoading() else dismissLoading()
    }

    fun renderStateError(error: Throwable) {
        toast(error.message.toString())
    }
}
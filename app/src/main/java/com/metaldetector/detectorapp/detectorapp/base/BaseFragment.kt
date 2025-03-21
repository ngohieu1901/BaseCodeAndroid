package com.metaldetector.detectorapp.detectorapp.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.metaldetector.detectorapp.detectorapp.utils.PermissionUtils
import com.metaldetector.detectorapp.detectorapp.utils.SharePrefUtils
import com.metaldetector.detectorapp.detectorapp.utils.SystemUtils.setLocale
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Inject

abstract class BaseFragment<VB : ViewBinding,VM : ViewModel> : Fragment() {
    private var _binding: VB? = null
    lateinit var binding: VB
    lateinit var viewModel: VM

    protected val permissionUtils by lazy { PermissionUtils(requireActivity()) }
    protected val sharePref by lazy { SharePrefUtils(requireContext()) }

    val exceptionHandler: CoroutineExceptionHandler by lazy { CoroutineExceptionHandler { _, exception ->
        Log.e("coroutineException1901", "${exception.message}")
    } }

    protected abstract fun setViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB
    protected abstract fun initView()
    protected abstract fun initClickListener()
    protected abstract fun initViewModel(): Class<VM>
    protected abstract fun dataCollect()

    open fun hideSoftKeyboard() {
        activity?.currentFocus?.let {
            val inputMethodManager =
                activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[initViewModel()]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding?.let {
            binding = it
        } ?: run {
            _binding = setViewBinding(inflater, container)
            binding = _binding!!
        }
        initView()
        initClickListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataCollect()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    suspend fun showLoading() {
        (activity as? BaseActivity<*,*>)?.showLoading()
    }

    suspend fun dismissLoading() {
        (activity as? BaseActivity<*,*>)?.dismissLoading()
    }

    override fun onAttach(context: Context) {
        super.onAttach(setLocale(context))
    }
}
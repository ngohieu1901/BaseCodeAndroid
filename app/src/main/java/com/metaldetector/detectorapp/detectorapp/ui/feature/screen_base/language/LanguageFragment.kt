package com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.language

import android.view.LayoutInflater
import android.view.ViewGroup
import com.metaldetector.detectorapp.detectorapp.base.BaseFragment
import com.metaldetector.detectorapp.detectorapp.databinding.FragmentLanguageBinding
import com.metaldetector.detectorapp.detectorapp.utils.SystemUtils
import com.metaldetector.detectorapp.detectorapp.ui.feature.main.MainActivity
import com.metaldetector.detectorapp.detectorapp.view_model.CommonVM
import com.metaldetector.detectorapp.detectorapp.widget.invisible
import com.metaldetector.detectorapp.detectorapp.widget.launchActivity
import com.metaldetector.detectorapp.detectorapp.widget.tap
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageFragment : BaseFragment<FragmentLanguageBinding, CommonVM>() {
    private lateinit var adapter: LanguageAdapter
    private var lang = "en"

    override fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLanguageBinding {
        return FragmentLanguageBinding.inflate(layoutInflater)
    }

    override fun initView() {
        context?.let {
//            AdsHelper.loadBannerFragment(it as AppCompatActivity, binding.frBanner)
        }

        adapter = LanguageAdapter(onClick = {
            lang = it.code
            adapter.setCheck(it.code)
            selectLanguage()
        })
        binding.recyclerView.adapter = adapter
        binding.ivBack.tap {
            popBackStack()
        }
        binding.ivCheck.tap { selectLanguage() }
        binding.ivCheck.invisible()
        val list = SystemUtils.listLanguage()
        list.forEach {
            if (it.code == SystemUtils.getPreLanguage(requireContext())) {
                lang = it.code
                it.active = true
                return@forEach
            }
        }
        adapter.addList(list)
    }

    override fun initClickListener() {

    }

    override fun initViewModel(): Class<CommonVM> = CommonVM::class.java

    override fun dataCollect() {

    }

    private fun selectLanguage() {
        SystemUtils.saveLocale(requireContext(), lang)
        launchActivity(MainActivity::class.java)
        requireActivity().finishAffinity()
    }
}
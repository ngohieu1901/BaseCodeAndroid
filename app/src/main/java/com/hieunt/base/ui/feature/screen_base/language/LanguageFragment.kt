package com.hieunt.base.ui.feature.screen_base.language

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hieunt.base.R
import com.hieunt.base.base.BaseFragment
import com.hieunt.base.databinding.FragmentLanguageBinding
import com.hieunt.base.firebase.ads.RemoteName
import com.hieunt.base.ui.feature.container.ContainerActivity
import com.hieunt.base.utils.SystemUtils
import com.hieunt.base.widget.launchActivity
import com.hieunt.base.widget.tap
import com.hieunt.base.widget.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageFragment : BaseFragment<FragmentLanguageBinding>() {
    private lateinit var adapter: LanguageAdapter
    private var lang = "en"

    override fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLanguageBinding {
        return FragmentLanguageBinding.inflate(layoutInflater)
    }

    override fun initView() {
        loadNative(RemoteName.NATIVE_ALL, R.layout.shimmer_custom_all, R.layout.native_custom_all)

        adapter = LanguageAdapter(onClick = {
            lang = it.code
            adapter.setCheck(it.code)
            binding.ivCheck.visible()
        })

        binding.recyclerView.adapter = adapter
        binding.ivBack.tap {
            popBackStack()
        }
        binding.ivCheck.tap { selectLanguage() }
        val list = SystemUtils.listLanguage()
        Log.e("fjkdjfdksf", "getPreLanguage: ${SystemUtils.getPreLanguage(requireContext())}")
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

    override fun dataCollect() {

    }

    private fun selectLanguage() {
        SystemUtils.saveLocale(requireContext(), lang)
        launchActivity(ContainerActivity::class.java)
        requireActivity().finishAffinity()
    }
}
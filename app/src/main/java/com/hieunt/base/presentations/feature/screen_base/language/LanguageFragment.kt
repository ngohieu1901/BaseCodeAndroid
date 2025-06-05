package com.hieunt.base.presentations.feature.screen_base.language

import android.util.Log
import com.hieunt.base.R
import com.hieunt.base.base.BaseFragment
import com.hieunt.base.databinding.FragmentLanguageBinding
import com.hieunt.base.firebase.ads.RemoteName
import com.hieunt.base.presentations.feature.container.ContainerActivity
import com.hieunt.base.utils.SystemUtils
import com.hieunt.base.widget.launchActivity
import com.hieunt.base.widget.tap
import com.hieunt.base.widget.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageFragment : BaseFragment<FragmentLanguageBinding>(FragmentLanguageBinding::inflate) {
    private lateinit var adapter: LanguageAdapter
    private var lang = "en"

    override fun initData() {

    }

    override fun setupView() {
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

    override fun dataCollect() {

    }

    private fun selectLanguage() {
        SystemUtils.saveLocale(requireContext(), lang)
        launchActivity(ContainerActivity::class.java)
        requireActivity().finishAffinity()
    }
}
package com.hieunt.base.ui.feature.main

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hieunt.base.base.BaseFragment
import com.hieunt.base.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment: BaseFragment<FragmentMainBinding>() {
    override fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false)
    }

    override fun initView() {
    }

    override fun initClickListener() {
    }

    override fun dataCollect() {

    }
}
package com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.metaldetector.detectorapp.detectorapp.R
import com.metaldetector.detectorapp.detectorapp.base.BaseActivity
import com.metaldetector.detectorapp.detectorapp.databinding.ActivityIntroBinding
import com.metaldetector.detectorapp.detectorapp.firebase.event.AdmobEvent
import com.metaldetector.detectorapp.detectorapp.model.IntroModel
import com.metaldetector.detectorapp.detectorapp.ui.feature.main.MainActivity
import com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.permission.PermissionActivity
import com.metaldetector.detectorapp.detectorapp.utils.SharePrefUtils
import com.metaldetector.detectorapp.detectorapp.view_model.CommonVM
import com.metaldetector.detectorapp.detectorapp.widget.gone
import com.metaldetector.detectorapp.detectorapp.widget.invisible
import com.metaldetector.detectorapp.detectorapp.widget.launchActivity
import com.metaldetector.detectorapp.detectorapp.widget.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding, CommonVM>() {
    private var listIntroModel = mutableListOf<IntroModel>()
    private val sharePre: SharePrefUtils by lazy { SharePrefUtils(this) }
    var isFirst = true

    private val myPageChangeCallback: ViewPager2.OnPageChangeCallback =
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (isFirst) {
                    isFirst = false
                    return
                }
                binding.apply {
                    if (viewPager2.currentItem == listIntroModel.size - 1){
                        btnNextTutorial.text = getString(R.string.start)
                    } else {
                        btnNextTutorial.text = getString(R.string.next)
                    }
                    binding.apply {
                        when (position) {
                            0 -> {
                                frAds.visible()
                                lavIntro.invisible()
                                btnNextTutorial.visible()
                                btnNextTutorialBottom.gone()
                            }

                            1, 2 -> {
                                frAds.visible()
                                lavIntro.visible()
                                btnNextTutorial.visible()
                                btnNextTutorialBottom.gone()
                            }

                            else -> {
                                frAds.gone()
                                btnNextTutorial.invisible()
                                btnNextTutorialBottom.visible()
                            }
                        }
                    }
                }
                addBottomDots(position)
            }
        }

    override fun setViewBinding(): ActivityIntroBinding {
        return ActivityIntroBinding.inflate(LayoutInflater.from(this))
    }

    override fun initViewModel(): Class<CommonVM> = CommonVM::class.java

    override fun initView() {
        val introAdapter = IntroAdapter(initData())
        binding.viewPager2.apply {
            adapter = introAdapter
            registerOnPageChangeCallback(myPageChangeCallback)
        }
        addBottomDots(0)
        binding.btnNextTutorial.setOnClickListener {
            binding.viewPager2.currentItem += 1
        }
        binding.btnNextTutorialBottom.setOnClickListener {
            startNextScreen()
        }
    }

    override fun dataCollect() {

    }

    private fun initData(): MutableList<IntroModel> {
        addBottomDots(0)
        listIntroModel = mutableListOf<IntroModel>().apply {
            add(
                IntroModel(
                    R.drawable.img_intro_1,
                    R.string.title_intro_1,
                    R.string.content_intro_1,
                    IntroType.DEFAULT
                )
            )
            add(
                IntroModel(
                    R.drawable.img_intro_2,
                    R.string.title_intro_2,
                    R.string.content_intro_2,
                    IntroType.DEFAULT
                )
            )
            add(
                IntroModel(
                    R.drawable.img_intro_3,
                    R.string.app_name,
                    R.string.app_name,
                    IntroType.DEFAULT
                )
            )
            add(
                IntroModel(
                    R.drawable.img_intro_4,
                    R.string.title_intro_4,
                    R.string.content_intro_4,
                    IntroType.DEFAULT
                )
            )

        }
        return listIntroModel
    }

    private fun startNextScreen() {
        launchActivity(if (sharePre.isPassPermission) MainActivity::class.java else PermissionActivity::class.java)
        finishAffinity()
    }

    private fun addBottomDots(currentPage: Int) {
        binding.linearDots.removeAllViews()
        val dots = arrayOfNulls<ImageView>(listIntroModel.size)
        for (i in 0 until listIntroModel.size) {
            dots[i] = ImageView(this)
            if (i == currentPage)
                dots[i]!!.setImageResource(R.drawable.ic_intro_selected)
            else
                dots[i]!!.setImageResource(R.drawable.ic_intro_not_select)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            binding.linearDots.addView(dots[i], params)
        }
    }

    override fun onBackPressedSystem() {
        finishAffinity()
    }
}
package com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.amazic.library.Utils.RemoteConfigHelper
import com.amazic.library.ads.admob.Admob
import com.metaldetector.detectorapp.detectorapp.R
import com.metaldetector.detectorapp.detectorapp.base.BaseActivity
import com.metaldetector.detectorapp.detectorapp.databinding.ActivityIntroBinding
import com.metaldetector.detectorapp.detectorapp.firebase.ads.RemoteName.INTER_INTRO
import com.metaldetector.detectorapp.detectorapp.firebase.ads.RemoteName.NATIVE_INTRO
import com.metaldetector.detectorapp.detectorapp.firebase.ads.RemoteName.NATIVE_INTRO_FULL
import com.metaldetector.detectorapp.detectorapp.firebase.event.AdmobEvent
import com.metaldetector.detectorapp.detectorapp.model.IntroModel
import com.metaldetector.detectorapp.detectorapp.ui.feature.main.MainActivity
import com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.permission.PermissionActivity
import com.metaldetector.detectorapp.detectorapp.utils.SharePrefUtils
import com.metaldetector.detectorapp.detectorapp.view_model.CommonVM
import com.metaldetector.detectorapp.detectorapp.widget.gone
import com.metaldetector.detectorapp.detectorapp.widget.launchActivity
import com.metaldetector.detectorapp.detectorapp.widget.visible

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

                AdmobEvent.logEvent(
                    this@IntroActivity,
                    "onboarding${binding.viewPager2.currentItem + 1}_view",
                    Bundle()
                )
                binding.apply {
                    if (
                        viewPager2.currentItem == listIntroModel.size - 2
                        && RemoteConfigHelper.getInstance().get_config(this@IntroActivity, NATIVE_INTRO_FULL) && Admob.getInstance().showAllAds
                    ) {
                        listOf(linearDots, btnNextTutorial, frAds).forEach { it.gone() }
                    } else {
                        listOf(linearDots, btnNextTutorial, frAds).forEach { it.visible() }
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
        loadInter(INTER_INTRO)
        loadNative(
            NATIVE_INTRO,
            R.layout.ads_shimmer_intro,
            R.layout.ads_native_intro,
        )

        val data = initData()
        val introAdapter = IntroAdapter(this, data)
        binding.viewPager2.apply {
            adapter = introAdapter
            registerOnPageChangeCallback(myPageChangeCallback)
        }
        addBottomDots(0)
        binding.btnNextTutorial.setOnClickListener {
            AdmobEvent.logEvent(
                this@IntroActivity,
                "onboarding${binding.viewPager2.currentItem + 1}_next_click",
                Bundle()
            )
            if (binding.viewPager2.currentItem == listIntroModel.size - 1) {
                showInter(
                    INTER_INTRO,
                    isReloadAds = false,
                    onNextAction = {
                        startNextScreen()
                    })
            } else {
                binding.viewPager2.currentItem += 1
            }
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
            if (RemoteConfigHelper.getInstance().get_config(this@IntroActivity, NATIVE_INTRO_FULL) && Admob.getInstance().showAllAds) {
                add(
                    IntroModel(
                        R.drawable.ic_launcher_foreground,
                        R.string.app_name,
                        R.string.app_name,
                        IntroType.ADS
                    )
                )
            }
            add(
                IntroModel(
                    R.drawable.img_intro_3,
                    R.string.title_intro_3,
                    R.string.content_intro_3,
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
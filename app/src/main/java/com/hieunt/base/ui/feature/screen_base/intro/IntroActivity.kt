package com.hieunt.base.ui.feature.screen_base.intro

import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.amazic.library.ads.admob.Admob
import com.hieunt.base.R
import com.hieunt.base.base.BaseActivity
import com.hieunt.base.databinding.ActivityIntroBinding
import com.hieunt.base.di.MainDispatcher
import com.hieunt.base.firebase.ads.RemoteName
import com.hieunt.base.firebase.ads.RemoteName.INTER_INTRO
import com.hieunt.base.firebase.ads.RemoteName.NATIVE_INTRO
import com.hieunt.base.firebase.ads.RemoteName.NATIVE_INTRO_2
import com.hieunt.base.firebase.event.EventName
import com.hieunt.base.model.IntroModel
import com.hieunt.base.ui.feature.container.ContainerActivity
import com.hieunt.base.ui.feature.screen_base.permission.PermissionActivity
import com.hieunt.base.utils.SharePrefUtils
import com.hieunt.base.widget.gone
import com.hieunt.base.widget.launchActivity
import com.hieunt.base.widget.logEvent
import com.hieunt.base.widget.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>() {
    private var listIntroModel = mutableListOf<IntroModel>()

    @Inject
    lateinit var sharePref: SharePrefUtils

    @Inject
    @MainDispatcher
    lateinit var mainDispatcher: CoroutineDispatcher

    var isFirst = true

    private lateinit var parentJob: Job
    private lateinit var scope: CoroutineScope

    private val myPageChangeCallback: ViewPager2.OnPageChangeCallback =
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (isFirst) {
                    isFirst = false
                    return
                }

                logEvent("onboarding${binding.viewPager2.currentItem + 1}_view")

                binding.apply {
                    if (viewPager2.currentItem == listIntroModel.size - 1) {
                        btnNextTutorial.text = getString(R.string.start)
                    } else {
                        btnNextTutorial.text = getString(R.string.next)
                    }
                    if (listIntroModel[position].type == IntroType.ADS) {
                        listOf(frAds,linearDots,btnNextTutorial).forEach {
                            it.gone()
                        }
                        scope.launch {
                            launch {
                                animationView.visible()
                                delay(1000)
                                animationView.gone()
                            }
                            launch {
                                delay(4000)
                                ivClose.visible()
                            }
                            launch {
                                delay(7000)
                                viewPager2.currentItem += 1
                            }
                        }
                    } else {
                        if (parentJob.isActive) {
                            parentJob.cancel()
                            parentJob = Job()
                            scope = CoroutineScope(mainDispatcher + parentJob)
                        }
                        ivClose.gone()
                        animationView.gone()

                        listOf(frAds,linearDots,btnNextTutorial).forEach {
                            it.visible()
                        }
                    }
                }
                addBottomDots(position)
            }
        }

    override fun setViewBinding(): ActivityIntroBinding {
        return ActivityIntroBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
        logEvent(EventName.onboarding_1_view)
        logEvent(EventName.onboard_open)
        sharePref.countOpenAppTestFlow += 1
        if (sharePref.countOpenApp <= 10) {
            logEvent(EventName.onboard_open + "_" + sharePref.countOpenApp)
        }
        loadInter(INTER_INTRO)
        loadDoubleNative(
            NATIVE_INTRO,
            NATIVE_INTRO,
            NATIVE_INTRO_2,
            R.layout.ads_native_large_button_below,
            R.layout.ads_shimmer_large_button_below,
        )

        parentJob = Job()
        scope = CoroutineScope(mainDispatcher + parentJob)

        val introAdapter = IntroAdapter(this, initData())
        binding.viewPager2.apply {
            adapter = introAdapter
            registerOnPageChangeCallback(myPageChangeCallback)
        }
        addBottomDots(0)
        binding.btnNextTutorial.setOnClickListener {
            if (binding.viewPager2.currentItem == listIntroModel.size - 1) {
                showInter(
                    INTER_INTRO,
                    isReloadAds = false,
                    onNextAction = {
                        logEvent(EventName.onboarding_next_click)
                        if (sharePref.countOpenApp <= 10) {
                            logEvent(EventName.onboarding_next_click + "_" + sharePref.countOpenApp)
                        }
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
            add(
                IntroModel(
                    R.drawable.img_intro_3,
                    R.string.title_intro_3,
                    R.string.content_intro_3,
                    IntroType.DEFAULT
                )
            )
            if (Admob.getInstance().checkCondition(this@IntroActivity, RemoteName.NATIVE_INTRO_FULL)) {
                add(
                    IntroModel(
                        R.drawable.ic_logo_app,
                        R.string.app_name,
                        R.string.app_name,
                        IntroType.ADS
                    )
                )
            }
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
        launchActivity(if (sharePref.isPassPermission) ContainerActivity::class.java else PermissionActivity::class.java)
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

    override fun onStop() {
        super.onStop()
        if (parentJob.isActive) {
            parentJob.cancel()
            parentJob = Job()
            scope = CoroutineScope(mainDispatcher + parentJob)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (parentJob.isActive) {
            parentJob.cancel()
        }
    }
}
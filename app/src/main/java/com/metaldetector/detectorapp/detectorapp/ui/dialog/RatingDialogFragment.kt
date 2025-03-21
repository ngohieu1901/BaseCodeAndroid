package com.metaldetector.detectorapp.detectorapp.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar.OnRatingBarChangeListener
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.metaldetector.detectorapp.detectorapp.R
import com.metaldetector.detectorapp.detectorapp.base.BaseDialogFragment
import com.metaldetector.detectorapp.detectorapp.databinding.DialogRatingBinding
import com.metaldetector.detectorapp.detectorapp.firebase.event.AdmobEvent
import com.metaldetector.detectorapp.detectorapp.utils.SharePrefUtils
import com.metaldetector.detectorapp.detectorapp.view_model.CommonVM
import com.metaldetector.detectorapp.detectorapp.widget.tap

class RatingDialogFragment(private val isFinishActivity: Boolean, private val onClickRate: () -> Unit) : BaseDialogFragment<DialogRatingBinding, CommonVM>(true) {

    override fun getTheme(): Int {
        return R.style.BaseDialog
    }

    override fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogRatingBinding {
        return DialogRatingBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        binding.tvContent.text = getString(
            R.string.we_d_greatly_appreciate_if_you_can_rate_us,
            getString(R.string.app_name)
        )
        changeRating()
        binding.rtb.rating = 5f
        binding.apply {
            btnRate.tap {
                if (rtb.rating == 0f) {
                    Toast.makeText(activity, R.string.please_give_rating, Toast.LENGTH_SHORT)
                        .show()
                    return@tap
                }
                AdmobEvent.logEvent(context, "rate_submit", Bundle())
                dismiss()
                if (rtb.rating < 4) {
                    sendMail()
                } else {
                    sendRate()
                }
                SharePrefUtils(requireActivity()).isRated = true
                onClickRate.invoke()
            }
            btnLater.tap {
                AdmobEvent.logEvent(context, "rate_not_now", Bundle())
                dismiss()
                checkFinishApplication()
            }
        }
    }

    override fun dataCollect() {

    }

    override fun initViewModel(): Class<CommonVM> = CommonVM::class.java

    private fun checkFinishApplication() {
        if (isFinishActivity) {
            activity?.let { activity ->
                SharePrefUtils(activity).countExitApp++
                activity.finishAffinity()
            }
        }
    }

    private fun sendRate() {
        activity?.let { activity ->
            val manager: ReviewManager = ReviewManagerFactory.create(activity)
            val request: Task<ReviewInfo> = manager.requestReviewFlow()
            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val reviewInfo: ReviewInfo = task.result
                    val flow: Task<Void> =
                        manager.launchReviewFlow(activity, reviewInfo)
                    flow.addOnSuccessListener {
                        checkFinishApplication()
                    }
                } else {
                    dismiss()
                    checkFinishApplication()
                }
            }
        }
    }

    private fun sendMail() {
        activity?.let { activity ->
            Toast.makeText(
                activity,
                R.string.thank_u,
                Toast.LENGTH_SHORT
            ).show()
            checkFinishApplication()
        }
    }

    private fun changeRating() {
        binding.rtb.onRatingBarChangeListener =
            OnRatingBarChangeListener { _, rating, _ ->
                context?.let { context ->
                    when (rating) {
                        1f -> {
                            binding.btnRate.text = context.getText(R.string.thank_u)
                            binding.imgIcon.setImageResource(R.drawable.ic_star_1)
                            binding.btnLater.visibility = View.VISIBLE
                        }

                        2f -> {
                            binding.btnRate.text = context.getText(R.string.thank_u)
                            binding.imgIcon.setImageResource(R.drawable.ic_star_2)
                            binding.btnLater.visibility = View.VISIBLE
                        }

                        3f -> {
                            binding.btnRate.text = context.getText(R.string.thank_u)
                            binding.imgIcon.setImageResource(R.drawable.ic_star_3)
                            binding.btnLater.visibility = View.VISIBLE
                        }

                        4f -> {
                            binding.btnRate.text = context.getText(R.string.thank_u)
                            binding.imgIcon.setImageResource(R.drawable.ic_star_4)
                            binding.btnLater.visibility = View.GONE
                        }

                        5f -> {
                            binding.btnRate.text = context.getText(R.string.thank_u)
                            binding.imgIcon.setImageResource(R.drawable.ic_star_5)
                            binding.btnLater.visibility = View.GONE
                        }

                        else -> {
                            binding.btnRate.text = context.getText(R.string.rate)
                            binding.imgIcon.setImageResource(R.drawable.ic_star_0)
                            binding.btnLater.visibility = View.VISIBLE
                        }
                    }
                }
            }
    }
}
package com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.intro

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.metaldetector.detectorapp.detectorapp.R
import com.metaldetector.detectorapp.detectorapp.databinding.ItemIntroAdsNativeBinding
import com.metaldetector.detectorapp.detectorapp.databinding.ItemIntroBinding
import com.metaldetector.detectorapp.detectorapp.firebase.ads.AdsHelper
import com.metaldetector.detectorapp.detectorapp.firebase.ads.RemoteName.NATIVE_INTRO_FULL
import com.metaldetector.detectorapp.detectorapp.model.IntroModel

class IntroAdapter(
    private val activity: AppCompatActivity,
    private val list: List<IntroModel> = emptyList(),
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class IntroDefaultVH(val binding: ItemIntroBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class IntroAdsNativeVH(val binding: ItemIntroAdsNativeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            IntroType.DEFAULT.ordinal -> {
                val defaultBinding =
                    ItemIntroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                IntroDefaultVH(defaultBinding)
            }

            IntroType.ADS.ordinal -> {
                val adsNativeBinding = ItemIntroAdsNativeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                IntroAdsNativeVH(adsNativeBinding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].type.ordinal
    }

    @SuppressLint("InflateParams")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val introModel: IntroModel = list[position]
        when (holder.itemViewType) {
            IntroType.DEFAULT.ordinal -> {
                val viewHolderDefault = holder as IntroDefaultVH
                viewHolderDefault.binding.apply {
                    ivIntro.setImageResource(introModel.image)
                    tvTitle.setText(introModel.title)
                }
            }

            IntroType.ADS.ordinal -> {
                val viewHolderAdsNative = holder as IntroAdsNativeVH
                AdsHelper.loadNativeItem(activity, viewHolderAdsNative.binding.frAds, NATIVE_INTRO_FULL, R.layout.ads_shimmer_intro_full_screen, R.layout.ads_native_intro_full_screen){
                    notifyItemChanged(position)
                }
            }
        }
    }


}
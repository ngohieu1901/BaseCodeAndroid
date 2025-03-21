package com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.language_start.language_new

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.metaldetector.detectorapp.detectorapp.R
import com.metaldetector.detectorapp.detectorapp.databinding.ItemLanguageBinding
import com.metaldetector.detectorapp.detectorapp.model.LanguageModelNew
import com.metaldetector.detectorapp.detectorapp.widget.gone
import com.metaldetector.detectorapp.detectorapp.widget.visible

class LanguageStartNewAdapter(
    private val lists: List<LanguageModelNew>,
    private val iClickLanguage: IClickLanguage,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isClicked = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LanguageViewHolder(ItemLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = lists[position]
        if (holder is LanguageViewHolder) {
            holder.bind(data)
        }
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    inner class LanguageViewHolder(val binding: ItemLanguageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: LanguageModelNew) {
            data.image?.let { binding.civLogo.setImageResource(it) }
            binding.tvName.text = data.languageName

            binding.layoutItem.setOnClickListener { onItemClick(data) }

            if (data.isCheck) {
                binding.layoutItem.setBackgroundResource(R.drawable.bg_item_language_select)
                binding.tvName.typeface = ResourcesCompat.getFont(binding.root.context, R.font.poppins_semi_bold)
                binding.tvName.setTextColor(Color.parseColor("#FFFFFF"))
            } else {
                binding.layoutItem.setBackgroundResource(R.drawable.bg_item_language)
                binding.tvName.typeface = ResourcesCompat.getFont(binding.root.context, R.font.poppins_regular)
                binding.tvName.setTextColor(Color.parseColor("#374151"))
            }

            if (bindingAdapterPosition == 3 && !isClicked) {
                binding.animationView.visible()
                binding.animationView.playAnimation()
            } else {
                binding.animationView.gone()
                binding.animationView.pauseAnimation()
            }
        }

        private fun onItemClick(data: LanguageModelNew) {
            isClicked = true
            iClickLanguage.onClick(data)
            for (item in lists) {
                item.isCheck = item.languageName == data.languageName
            }
            notifyDataSetChanged()
        }
    }
}
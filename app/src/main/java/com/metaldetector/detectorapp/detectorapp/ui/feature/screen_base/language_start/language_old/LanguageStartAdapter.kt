package com.metaldetector.detectorapp.detectorapp.ui.feature.screen_base.language_start.language_old

import android.view.ViewGroup
import com.metaldetector.detectorapp.detectorapp.R
import com.metaldetector.detectorapp.detectorapp.base.BaseAdapter
import com.metaldetector.detectorapp.detectorapp.base.BaseViewHolder
import com.metaldetector.detectorapp.detectorapp.databinding.ItemLanguageBinding
import com.metaldetector.detectorapp.detectorapp.model.LanguageModel
import com.metaldetector.detectorapp.detectorapp.widget.layoutInflate

class LanguageStartAdapter(
    val onClick: (lang: LanguageModel) -> Unit
) : BaseAdapter<LanguageModel, LanguageStartAdapter.LanguageVH>() {

    inner class LanguageVH(binding: ItemLanguageBinding) :
        BaseViewHolder<LanguageModel, ItemLanguageBinding>(binding) {
        override fun bindData(data: LanguageModel) {
            super.bindData(data)
            binding.apply {
                tvName.text = data.name
                if (data.active)
                    layoutItem.setBackgroundResource(R.drawable.border_item_language_select)
                else
                    layoutItem.setBackgroundResource(R.drawable.border_item_language_default)
                civLogo.apply {
                    when (data.code) {
                        "en" -> setImageResource(R.drawable.flag_en)
                        "de" -> setImageResource(R.drawable.flag_ger)
                        "es" -> setImageResource(R.drawable.flag_span)
                        "fr" -> setImageResource(R.drawable.flag_fra)
                        "hi" -> setImageResource(R.drawable.flag_hindi)
                        "in" -> setImageResource(R.drawable.flag_indonesia)
                        "pt" -> setImageResource(R.drawable.flag_port)
                        "zh" -> setImageResource(R.drawable.flag_china)
                    }
                }
            }
        }

        override fun onItemClickListener(data: LanguageModel) {
            super.onItemClickListener(data)
            onClick.invoke(data)

        }
    }

    fun setCheck(code: String) {
        val oldIndex = listData.indexOfFirst { it.active }
        val newIndex = listData.indexOfFirst { it.code == code }

        for (item in listData) {
            item.active = item.code == code
        }

        if (oldIndex != -1) {
            notifyItemChanged(oldIndex)
        }
        notifyItemChanged(newIndex)
    }

    override fun viewHolder(viewType: Int, parent: ViewGroup): LanguageVH = LanguageVH(
        ItemLanguageBinding.inflate(parent.layoutInflate(), parent, false)
    )

    override fun layout(position: Int): Int = R.layout.item_language
}
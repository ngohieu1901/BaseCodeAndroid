package com.metaldetector.detectorapp.detectorapp.ui.feature.uninstall

import android.view.ViewGroup
import com.metaldetector.detectorapp.detectorapp.R
import com.metaldetector.detectorapp.detectorapp.base.BaseAdapter
import com.metaldetector.detectorapp.detectorapp.base.BaseViewHolder
import com.metaldetector.detectorapp.detectorapp.databinding.ItemAnswerBinding
import com.metaldetector.detectorapp.detectorapp.model.AnswerModel
import com.metaldetector.detectorapp.detectorapp.widget.layoutInflate

class UninstallAdapter(private val onClick: (AnswerModel, position: Int) -> Unit): BaseAdapter<AnswerModel, UninstallAdapter.AnswerVH>() {
    inner class AnswerVH(binding: ItemAnswerBinding): BaseViewHolder<AnswerModel, ItemAnswerBinding>(binding){
        override fun bindData(data: AnswerModel) {
            super.bindData(data)
            binding.apply {
                tvName.text = context.getString(data.name)
                if (data.isSelected) {
                    rdSelect.setImageResource(R.drawable.rd_select_why)
                } else {
                    rdSelect.setImageResource(R.drawable.rd_un_select_why)
                }
            }
        }

        override fun onItemClickListener(data: AnswerModel) {
            super.onItemClickListener(data)
            onClick(data.copy(isSelected = true), bindingAdapterPosition)
        }
    }

    override fun viewHolder(viewType: Int, parent: ViewGroup): AnswerVH {
        return AnswerVH(ItemAnswerBinding.inflate(parent.layoutInflate(), parent,false))
    }

    override fun layout(position: Int): Int {
        return R.layout.item_answer
    }
}
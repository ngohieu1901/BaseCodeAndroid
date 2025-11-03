package com.hieunt.base.presentations.feature.screen_base.language_start_new

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.hieunt.base.R
import com.hieunt.base.databinding.ItemLanguageNewBinding
import com.hieunt.base.domain.model.LanguageParentModel
import com.hieunt.base.domain.model.LanguageSubModel
import com.hieunt.base.widget.tap

class LanguageStartNewAdapter(
    private val lists: List<LanguageParentModel>?,
    private val onClickSubLanguage: (LanguageSubModel) -> Unit,
    private val onClickDropDown: (LanguageParentModel) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var languageSubAdapter: LanguageSubAdapter? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        return LanguageViewHolder(ItemLanguageNewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        val data = lists!![position]
        if (holder is LanguageViewHolder) {
            holder.bind(data, position)
        }
    }

    override fun getItemCount(): Int {
        return lists?.size ?: 0
    }

    inner class LanguageViewHolder(private var binding: ItemLanguageNewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: LanguageParentModel, position: Int) {
            if (data.isExpand) {
                binding.tvTitle.setTextColor("#FFFFFF".toColorInt())
                binding.imgDropdown.setImageResource(R.drawable.ic_arrow_down)
                binding.recyclerView.visibility = View.VISIBLE
                binding.rlLayoutParent.setBackgroundResource(R.drawable.bg_item_language_select)
            } else {
                binding.tvTitle.setTextColor("#2C2C2E".toColorInt())
                binding.imgDropdown.setImageResource(R.drawable.ic_arrow_right)
                binding.recyclerView.visibility = View.GONE
                binding.rlLayoutParent.setBackgroundResource(R.drawable.bg_item_language)

            }
            data.image?.let { binding.imgAvatar.setImageResource(it) }
            binding.tvTitle.text = data.languageName

            languageSubAdapter = LanguageSubAdapter(data.listLanguageSubModel,
                onClickSubLanguage = {
                    notifyItemChanged(findSelectedLanguageIndex())
                    it.isCheck = true
                    notifyItemChanged(position)
                    onClickSubLanguage.invoke(it)
                }
            )
            binding.recyclerView.adapter = languageSubAdapter
            binding.root.tap {
                val isCurrentlyExpanded = data.isExpand

                lists?.forEachIndexed { index, item ->
                    if (item.isExpand && index != position) {
                        item.isExpand = false
                        notifyItemChanged(index)
                    }
                }
                data.isExpand = !isCurrentlyExpanded
                notifyItemChanged(position)
                onClickDropDown.invoke(data)
            }
        }
    }

    fun findSelectedLanguageIndex(): Int {
        lists?.forEachIndexed { index, languageModel ->
            languageModel.listLanguageSubModel.forEach {
                if (it.isCheck) {
                    it.isCheck = false
                    return index
                }
            }
        }
        return -1
    }
}
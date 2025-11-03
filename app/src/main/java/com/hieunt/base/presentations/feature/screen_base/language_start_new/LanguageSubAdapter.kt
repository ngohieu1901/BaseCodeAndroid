package com.hieunt.base.presentations.feature.screen_base.language_start_new

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hieunt.base.databinding.ItemLanguageSubBinding
import com.hieunt.base.domain.model.LanguageSubModel

class LanguageSubAdapter(
    private val lists: List<LanguageSubModel>,
    private val onClickSubLanguage: (LanguageSubModel) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LanguageViewHolder(
            ItemLanguageSubBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = lists[position]
        if (holder is LanguageViewHolder) {
            holder.bind(data, position)
        }
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    inner class LanguageViewHolder(private val binding: ItemLanguageSubBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: LanguageSubModel, position: Int) {
            binding.ivFlag.setImageResource(data.flag)
            binding.tvLanguage.text = data.languageName
            binding.rbLanguage.isChecked = data.isCheck == true
            binding.root.setOnClickListener {
                onClickSubLanguage.invoke(data)
                notifyItemChanged(position)
            }
            binding.rbLanguage.setOnClickListener {
                onClickSubLanguage.invoke(data)
            }
            binding.rbLanguage.isChecked = data.isCheck
        }
    }
}
package com.metaldetector.detectorapp.detectorapp.base

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.metaldetector.detectorapp.detectorapp.widget.tap

abstract class BaseViewHolder<M : Any, VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root) {
    val context: Context = binding.root.context
    companion object {
        private var isItemClickable = false
    }

    open fun bindData(data: M) {
        binding.root.tap {
            if (!isItemClickable) {
                isItemClickable = true
                onItemClickListener(data)
                Handler(Looper.getMainLooper()).postDelayed({
                    isItemClickable = false
                }, 300)
            }
        }
    }

    open fun onItemClickListener(data: M) = Unit
}

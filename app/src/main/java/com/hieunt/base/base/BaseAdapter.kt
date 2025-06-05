package com.hieunt.base.base

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<M : Any, VH : BaseViewHolder<M, *>> : RecyclerView.Adapter<VH>() {
    protected val listData = mutableListOf<M>()

    protected abstract fun createViewHolder(viewType: Int, parent: ViewGroup): VH

    protected abstract fun layoutResource(position: Int): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        createViewHolder(viewType, parent)

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bindData(listData[position])
    }

    override fun getItemViewType(position: Int): Int = layoutResource(position)

    override fun getItemCount(): Int = listData.size

    @SuppressLint("NotifyDataSetChanged")
    fun addList(list: List<M>) {
        listData.clear()
        listData.addAll(list)
        notifyDataSetChanged()
    }

    fun submitList(newList: List<M>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = listData.size

            override fun getNewListSize() = newList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                listData[oldItemPosition] == newList[newItemPosition]

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                listData[oldItemPosition] == newList[newItemPosition]
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listData.clear()
        listData.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

}


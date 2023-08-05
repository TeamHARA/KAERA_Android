package com.hara.kaera.presentation.storage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hara.kaera.databinding.ItemStorageGridBinding
import com.hara.kaera.presentation.storage.data.Worry
import com.hara.kaera.presentation.util.GlobalDiffCallBack

class StorageGridAdapter() :
    ListAdapter<Worry, StorageGridAdapter.ItemViewHolder>(GlobalDiffCallBack()) {
    private lateinit var inflater: LayoutInflater

    class ItemViewHolder(val binding: ItemStorageGridBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
        return ItemViewHolder(ItemStorageGridBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val curItem = getItem(position)
        with(holder.binding) {
            this.worryData = currentList[position]
            this.templateId = curItem.templateId
        }
    }
}

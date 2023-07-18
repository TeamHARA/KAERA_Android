package com.hara.kaera.presentation.write

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hara.kaera.databinding.ItemTemplateBinding
import com.hara.kaera.presentation.util.GlobalDiffCallBack

class TemplateChoiceAdapter() :
    ListAdapter<TemplateData, TemplateChoiceAdapter.ItemViewHolder>(GlobalDiffCallBack()) {

    private lateinit var inflater: LayoutInflater

    class ItemViewHolder(val binding: ItemTemplateBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
        return ItemViewHolder(ItemTemplateBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val curItem = getItem(position)
        with(holder as ItemViewHolder) {
            this.binding.templatedata = curItem
        }
    }

}
package com.hara.kaera.feature.storage.worrytemplate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hara.kaera.databinding.ItemWorryTemplateBinding
import com.hara.kaera.domain.entity.TemplateTypesEntity
import com.hara.kaera.feature.util.GlobalDiffCallBack

class WorryTemplateAdapter() :
    ListAdapter<TemplateTypesEntity.Template, WorryTemplateAdapter.ItemViewHolder>(GlobalDiffCallBack()) {
    private lateinit var inflater: LayoutInflater

    class ItemViewHolder(val binding: ItemWorryTemplateBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
        return ItemViewHolder(ItemWorryTemplateBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val curItem = getItem(position)
        with(holder.binding) {
            this.template = curItem

            ivArrow.setOnClickListener {
                val currentState = this.isExpand ?: false
                ExpandAnimation.toggleArrow(ivArrow, !currentState)
                this.isExpand = !currentState
            }
        }
    }
}

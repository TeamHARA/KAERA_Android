package com.hara.kaera.presentation.storage.worrytemplate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hara.kaera.databinding.ItemWorryTemplateBinding
import com.hara.kaera.presentation.util.GlobalDiffCallBack

class WorryTemplateAdapter() :
    ListAdapter<DummyTemplateList.Data, WorryTemplateAdapter.ItemViewHolder>(GlobalDiffCallBack()) {
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
            this.template = currentList[position]

            ivArrow.setOnClickListener {
                ExpandAnimation.toggleArrow(ivArrow, !curItem.expand)
                if (!curItem.expand) {
                    ExpandAnimation.expand(clContent)
//                    TransitionManager.beginDelayedTransition(binding.clRoot)
//                    binding.clContent.visibility = View.VISIBLE
//                    이렇게 하려면  android:animateLayoutChanges="true" 속성적용
                } else {
                    ExpandAnimation.collapse(clContent)
//                    TransitionManager.beginDelayedTransition(binding.clRoot)
//                    binding.clContent.visibility = View.GONE
                }
                curItem.expand = !curItem.expand
            }
        }
    }
}

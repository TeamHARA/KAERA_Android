package com.hara.kaera.feature.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hara.kaera.databinding.ItemHomeGemBinding
import com.hara.kaera.domain.entity.HomeWorryListEntity
import com.hara.kaera.presentation.home.FloatingAnimation
import com.hara.kaera.presentation.util.GlobalDiffCallBack

class HomeJewelAdapter() :
    ListAdapter<HomeWorryListEntity.HomeWorry, HomeJewelAdapter.HomeJewelViewHolder>(
        GlobalDiffCallBack()
    ) {

    private lateinit var inflater: LayoutInflater

    class HomeJewelViewHolder(val binding: ItemHomeGemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeJewelViewHolder {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
        return HomeJewelViewHolder(ItemHomeGemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: HomeJewelViewHolder, position: Int) {
        val curItem = getItem(position)
        with(holder.binding) {
            isSolved = true // 보석
            gemData = curItem

            val delay = (0..700).random()
            FloatingAnimation.floatingAnimationWithValueAnimator(
                delay,
                900,
                this.root,
                -30F
            ).start()
        }
    }
}
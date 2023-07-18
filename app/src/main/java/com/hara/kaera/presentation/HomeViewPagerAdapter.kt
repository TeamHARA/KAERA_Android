package com.hara.kaera.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hara.kaera.databinding.ItemHomeGemstoneBinding

class HomeViewPagerAdapter : ListAdapter<Gem, HomeViewPagerAdapter.HomeItemViewHolder>(HomeListDiffCallback) {
    private lateinit var binding: ItemHomeGemstoneBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemViewHolder {
        binding = ItemHomeGemstoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeItemViewHolder, position: Int) {
        //holder.bind(getItem(position))
        val rowNum = position / 3
        val linearLayout = binding.root.getLayoutaT
        val childView = binding.
    }

    class HomeItemViewHolder(private val binding: ItemHomeGemstoneBinding)
        : RecyclerView.ViewHolder(binding.root) {
            fun bind(item: Gem) {
                binding.viewGemstone1.tvGemstoneLabel.text = item.title
            }
    }

    object HomeListDiffCallback: DiffUtil.ItemCallback<Gem>() {
        override fun areItemsTheSame(oldItem: Gem, newItem: Gem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Gem, newItem: Gem): Boolean {
            return oldItem.title == newItem.title
        }
    }
}
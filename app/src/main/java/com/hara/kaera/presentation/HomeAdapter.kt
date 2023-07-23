package com.hara.kaera.presentation

import android.annotation.SuppressLint
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.TypedArrayUtils.getResourceId
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hara.kaera.R
import com.hara.kaera.databinding.ItemHomeGemsBinding
import timber.log.Timber

// [HomeFragment] ViewPager2 Adapter
class HomeAdapter : ListAdapter<List<Gem>, RecyclerView.ViewHolder>(HomeListDiffCallback) {
    private lateinit var binding: ItemHomeGemsBinding

    class HomeItemViewHolder(val binding: ItemHomeGemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemHomeGemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeItemViewHolder(binding)
    }

    @SuppressLint("RestrictedApi")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val curList = currentList[position]
        if (curList.isEmpty()) { // empty view
            binding.rootGems.visibility = View.GONE
            binding.rootEmpty.visibility = View.VISIBLE
            when (position) {
                0 -> { // 원석
                    with (binding) {
                        ivEmpty.setImageResource(R.drawable.empty_stone)
                        Timber.e("${Resources.getSystem().getString(R.string.home_gemstone_empty_title)}")
                        Timber.e("${Resources.getSystem().getString(R.string.home_gemstone_empty_content)}")
                        tvEmptyTitle.text = Resources.getSystem().getString(R.string.home_gemstone_empty_title)
                        tvEmptyContent.text = Resources.getSystem().getString(R.string.home_gemstone_empty_content)
                    }
                }
                1 -> { // 보석
                    with (binding) {
                        ivEmpty.setImageResource(R.drawable.gem_blue_s_off)
                        tvEmptyTitle.text = Resources.getSystem().getString(R.string.home_jewel_empty_title)
                        tvEmptyContent.text = Resources.getSystem().getString(R.string.home_jewel_empty_content)
                    }
                }
            }
        } else {
            binding.rootGems.visibility = View.VISIBLE
            binding.rootEmpty.visibility = View.GONE
            with((holder as HomeItemViewHolder).binding) {
                for (p in curList.indices) {
                    val childView = binding.rootGems.getChildAt(p)
                    if (childView is ConstraintLayout) {
                        //(childView.getChildAt(0) as ImageView).load(cur.image)
                        (childView.getChildAt(0) as ImageView).setImageResource(curList[p].image)
                        (childView.getChildAt(1) as TextView).text = curList[p].title
                    }
                }
            }
        }
    }
}

object HomeListDiffCallback : DiffUtil.ItemCallback<List<Gem>>() {
    override fun areItemsTheSame(oldItem: List<Gem>, newItem: List<Gem>): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: List<Gem>, newItem: List<Gem>): Boolean {
        return oldItem[0] == newItem[0]
    }
}
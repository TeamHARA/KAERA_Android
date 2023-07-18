package com.hara.kaera.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hara.kaera.databinding.ItemHomeGemsBinding

// [HomeFragment] ViewPager2 Adapter
class HomeAdapter : ListAdapter<List<Gem>, RecyclerView.ViewHolder>(HomeListDiffCallback) {
    private lateinit var binding: ItemHomeGemsBinding

    class HomeItemViewHolder(val binding: ItemHomeGemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemHomeGemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val curList = currentList[position]
        with((holder as HomeItemViewHolder).binding) {
            for (p in curList.indices) {
                val childView = root.getChildAt(p)
                if (childView is ConstraintLayout) {
                    //(childView.getChildAt(0) as ImageView).load(cur.image)
                    (childView.getChildAt(0) as ImageView).setImageResource(curList[p].image)
                    (childView.getChildAt(1) as TextView).text = curList[p].title
                }
            }
            /*
            val childView = root.getChildAt(position)
            if (childView is ConstraintLayout) {
                //(childView.getChildAt(0) as ImageView).load(cur.image)
                (childView.getChildAt(0) as ImageView).setImageResource(cur.image)
                (childView.getChildAt(1) as TextView).text = cur.title
            }
            */
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
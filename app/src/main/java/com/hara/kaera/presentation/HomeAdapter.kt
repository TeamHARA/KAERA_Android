package com.hara.kaera.presentation

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hara.kaera.R
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
        if (curList.isEmpty()) { // 1. empty view
            binding.rootGems.visibility = View.GONE
            binding.rootEmpty.visibility = View.VISIBLE
            when (position) {
                0 -> { // 원석
                    with(binding) {
                        ivEmpty.setImageResource(R.drawable.empty_stone)
                        tvEmptyTitle.text =
                            Resources.getSystem().getString(R.string.home_gemstone_empty_title)
                        tvEmptyContent.text =
                            Resources.getSystem().getString(R.string.home_gemstone_empty_content)
                    }
                }

                1 -> { // 보석
                    with(binding) {
                        ivEmpty.setImageResource(R.drawable.gem_blue_s_off)
                        tvEmptyTitle.text =
                            Resources.getSystem().getString(R.string.home_jewel_empty_title)
                        tvEmptyContent.text =
                            Resources.getSystem().getString(R.string.home_jewel_empty_content)
                    }
                }
            }
        } else { // 2. 원석/보석이 존재한다
            binding.rootGems.visibility = View.VISIBLE
            binding.rootEmpty.visibility = View.GONE
            with((holder as HomeItemViewHolder).binding) {
                for (p in curList.indices) {
                    val childView = binding.rootGems.getChildAt(p)
                    if (childView is ConstraintLayout) {
                        //(childView.getChildAt(0) as ImageView).load(cur.image)
                        val delay = (0..1000).random()
                        floatingAnimationWithValueAnimator(delay, 1000, childView, -20F).start()
                        with(childView.getChildAt(0) as ImageView) {
                            setImageResource(curList[p].image)
                            // 세트(보석+라벨)가 움직이는 것에 추가로 보석을 움직이게 한다
                            //floatingAnimationWithValueAnimator(delay, 1000, this@with,-20F).start()
                        }
                        (childView.getChildAt(1) as TextView).text = curList[p].title
                    }
                }
            }
        }
    }

    private fun floatingAnimationWithValueAnimator(
        start: Int,
        dur: Long,
        view: View,
        movement: Float
    ): ValueAnimator {
        return ValueAnimator.ofFloat(0f, 1f).apply {
            duration = dur
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
            startDelay = start.toLong()
            addUpdateListener {
                view.translationY = animatedValue as Float * movement
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
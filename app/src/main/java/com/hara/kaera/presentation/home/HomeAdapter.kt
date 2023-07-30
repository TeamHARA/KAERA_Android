package com.hara.kaera.presentation.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hara.kaera.R
import com.hara.kaera.databinding.LayoutHomeGemsBinding
import com.hara.kaera.presentation.home.model.Gem

// [HomeFragment] ViewPager2 Adapter
class HomeAdapter : ListAdapter<List<Gem>, RecyclerView.ViewHolder>(HomeListDiffCallback) {
    private lateinit var binding: LayoutHomeGemsBinding

    class HomeItemViewHolder(val binding: LayoutHomeGemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = LayoutHomeGemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
                        // Resources.getSystem().getString(R.string.home_jewel_empty_content)
                        tvEmptyTitle.text = "아직 고민 원석이 없네요!"
                        tvEmptyContent.text = "+ 버튼을 클릭해 고민을 작성해보세요."
                    }
                }

                1 -> { // 보석
                    with(binding) {
                        ivEmpty.setImageResource(R.drawable.gem_blue_s_off)
                        tvEmptyTitle.text = "아직 고민 보석이 없네요!"
                        tvEmptyContent.text = "원석을 터치해 고민 보석을 캐내보세요."
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
                        val delay = (0..700).random()
                        FloatingAnimation.floatingAnimationWithValueAnimator(
                            delay,
                            900,
                            childView,
                            -30F
                        ).start()
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
}

object HomeListDiffCallback : DiffUtil.ItemCallback<List<Gem>>() {
    override fun areItemsTheSame(oldItem: List<Gem>, newItem: List<Gem>): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: List<Gem>, newItem: List<Gem>): Boolean {
        return oldItem[0] == newItem[0]
    }
}
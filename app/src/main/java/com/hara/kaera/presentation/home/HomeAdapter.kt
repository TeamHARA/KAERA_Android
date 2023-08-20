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
import com.hara.kaera.domain.entity.HomeWorryListEntity

// [HomeFragment] ViewPager2 Adapter
class HomeAdapter : ListAdapter<List<HomeWorryListEntity.HomeWorry>, RecyclerView.ViewHolder>(HomeListDiffCallback) {
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
                    val childView = rootGems.getChildAt(curList[p].homeIndex) as ConstraintLayout

                    //(childView.getChildAt(0) as ImageView).load(cur.image)
                    val delay = (0..700).random()
                    FloatingAnimation.floatingAnimationWithValueAnimator(
                        delay,
                        900,
                        childView as View,
                        -30F
                    ).start()
//                        with(childView.getChildAt(0) as ImageView) {
//                            setImageResource(curList[p].image)
//                            // 세트(보석+라벨)가 움직이는 것에 추가로 보석을 움직이게 한다
//                            //floatingAnimationWithValueAnimator(delay, 1000, this@with,-20F).start()
//                        }

                    mapGemsImage((childView.getChildAt(0) as ImageView), position, curList[p].templateId)
                    (childView.getChildAt(1) as TextView).text = curList[p].title

                    // 추후 구조 갈아 엎으면 써야지 ^^
//                    (childView as LayoutHomeGemItemBinding).gemData = curList[p]
//                    (childView as LayoutHomeGemItemBinding).isSolved = position
                }
            }
        }
    }

    private fun mapGemsImage(iv: ImageView, isSolved: Int, templateId: Int) {
        if (isSolved == 0) { // 원석 이미지
            when (templateId) {
                1 -> { iv.setImageResource(R.drawable.gemstone_blue) }
                2 -> { iv.setImageResource(R.drawable.gemstone_red) }
                3 -> { iv.setImageResource(R.drawable.gemstone_orange) }
                4 -> { iv.setImageResource(R.drawable.gemstone_green) }
                5 -> { iv.setImageResource(R.drawable.gemstone_pink) }
                6 -> { iv.setImageResource(R.drawable.gemstone_yellow) }
                else -> { iv.setImageResource(R.drawable.icn_warningrt) }
            }
        }
        else { // 보석 이미지
            when(templateId) {
                1 -> { iv.setImageResource(R.drawable.gem_blue_s_on) }
                2 -> { iv.setImageResource(R.drawable.gem_red_s_on) }
                3 -> { iv.setImageResource(R.drawable.gem_orange_s_on) }
                4 -> { iv.setImageResource(R.drawable.gem_green_s_on) }
                5 -> { iv.setImageResource(R.drawable.gem_pink_s_on) }
                6 -> { iv.setImageResource(R.drawable.gem_yellow_s_on) }
                else -> { iv.setImageResource(R.drawable.icn_warningrt) }
            }
        }
    }
}

object HomeListDiffCallback : DiffUtil.ItemCallback<List<HomeWorryListEntity.HomeWorry>>() {
    override fun areItemsTheSame(oldItem: List<HomeWorryListEntity.HomeWorry>, newItem: List<HomeWorryListEntity.HomeWorry>): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: List<HomeWorryListEntity.HomeWorry>, newItem: List<HomeWorryListEntity.HomeWorry>): Boolean {
        return oldItem[0] == newItem[0]
    }
}
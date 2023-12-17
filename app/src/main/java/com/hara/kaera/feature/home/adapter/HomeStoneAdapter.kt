package com.hara.kaera.feature.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hara.kaera.databinding.ItemHomeGemBinding
import com.hara.kaera.domain.entity.HomeWorryListEntity
import com.hara.kaera.feature.home.FloatingAnimation
import com.hara.kaera.feature.util.Constant
import com.hara.kaera.feature.util.GlobalDiffCallBack
import timber.log.Timber

class HomeStoneAdapter(
    private val goToDetailBeforeActivity: (worryId: Int) -> Unit
) :
    ListAdapter<HomeWorryListEntity.HomeWorry, HomeStoneAdapter.HomeStoneViewHolder>(
        GlobalDiffCallBack()
    ) {

    private lateinit var inflater: LayoutInflater

    class HomeStoneViewHolder(val binding: ItemHomeGemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeStoneViewHolder {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
        return HomeStoneViewHolder(ItemHomeGemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: HomeStoneViewHolder, position: Int) {
        val curItem = getItem(position)
        Timber.e("[ABC] [HomeStoneAdapter/onBindViewHolder()] hash ${curItem.hashCode()} / pos ${position} / curItem ${curItem}")
        with(holder.binding) {
            isSolved = false // 원석
            gemData = curItem

            val delay = (0..700).random()
            FloatingAnimation.floatingAnimationWithValueAnimator(
                delay,
                900,
                this.root,
                -30F
            ).start()

            // 고민 전 상세보기로 이동
            if (curItem.worryId != Constant.dummyGemStoneId)
                root.setOnClickListener { stone ->
                    goToDetailBeforeActivity(curItem.worryId)
                }
        }
    }
}